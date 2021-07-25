<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:ns="http://bmstu.ru/logsearch">

    <xsl:output method="xml" omit-xml-declaration="no" indent="yes" encoding="UTF-8"/>

    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4-portrait" page-height="29.7cm" page-width="21.0cm" margin="2cm">
                    <fo:region-body/>
                    <fo:region-after/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4-portrait">
                <fo:flow flow-name="xsl-region-body" font-family="Arial">
                    <fo:block font-size="20px" font-weight="bold" color="red">Данные запроса</fo:block>
                    <fo:block>Регулярное выражение:
                        <xsl:value-of select="ns:Logs/ns:SearchInfo/ns:regularExpression"/>
                    </fo:block>
                    <fo:block>Директория:
                        <xsl:value-of select="ns:Logs/ns:SearchInfo/ns:location"/>
                    </fo:block>
                    <fo:block>Интервалы:</fo:block>
                    <xsl:for-each select="ns:Logs/ns:SearchInfo/ns:DateIntervals">
                        <fo:block>
                            С:
                            <xsl:value-of select="ns:dateFrom"/> По:
                            <xsl:value-of select="ns:dateTo"/>
                        </fo:block>
                    </xsl:for-each>
                    <fo:block font-size="20px" font-weight="bold" color="red" margin-top="10mm">Результат поиска</fo:block>
                    <xsl:for-each select="ns:Logs/ns:SearchInfoResult/ns:ResultLogs">
                        <fo:block font-size="10px">
                            Дата и время:
                            <xsl:value-of select="ns:timeMoment"/>
                        </fo:block>
                        <fo:block font-size="10px">
                            Имя файла:
                            <xsl:value-of select="ns:fileName"/>
                        </fo:block>
                        <fo:block margin-bottom="5mm" font-size="10px">
                            Содержимое:
                            <xsl:value-of select="ns:content"/>
                        </fo:block>
                    </xsl:for-each>
                    <xsl:if test="count(ns:Logs/ns:SearchInfoResult/ns:ResultLogs) = 0">
                        <fo:block>
                            <xsl:value-of select="ns:Logs/ns:SearchInfoResult/ns:emptyResultMessage"/>
                        </fo:block>
                    </xsl:if>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

</xsl:stylesheet>