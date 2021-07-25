<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://bmstu.ru/logsearch">

    <xsl:output method="text" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>

    <xsl:template match="/">

        Данные запроса:
        Регулярное выражение:   <xsl:value-of select="ns:Logs/ns:SearchInfo/ns:regularExpression"/>
        Директория:             <xsl:value-of select="ns:Logs/ns:SearchInfo/ns:location"/>
        Интервалы:
        <xsl:for-each select="ns:Logs/ns:SearchInfo/ns:DateIntervals">
            С:    <xsl:value-of select="ns:dateFrom"/>
            По:   <xsl:value-of select="ns:dateTo"/>
        </xsl:for-each>

        Результат поиска:
        <xsl:for-each select="ns:Logs/ns:SearchInfoResult/ns:ResultLogs">
            Дата и время:   <xsl:value-of select="ns:timeMoment"/>
            Имя файла:      <xsl:value-of select="ns:fileName"/>
            Содержимое:     <xsl:value-of select="ns:content"/>
        </xsl:for-each>

        <xsl:if test="count(ns:Logs/ns:SearchInfoResult/ns:ResultLogs) = 0">
            <xsl:value-of select="ns:Logs/ns:SearchInfoResult/ns:emptyResultMessage"/>
        </xsl:if>

    </xsl:template>

</xsl:stylesheet>