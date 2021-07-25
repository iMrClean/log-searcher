<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://bmstu.ru/logsearch">

    <xsl:output method="html" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>Результат</title>
            </head>
            <body>
                <div>
                    <table border="1" width="100%">
                        <caption>
                            <b>Данные запроса</b>
                        </caption>
                        <tr bgcolor="#9acd32">
                            <th>Регулярное выражение</th>
                            <th>Директория</th>
                            <th>С</th>
                            <th>По</th>
                        </tr>
                            <tr>
                                <td>
                                    <xsl:value-of select="ns:regularExpression"/>
                                </td>
                                <td>
                                    <xsl:value-of select="ns:location"/>
                                </td>
                                <xsl:for-each select="ns:Logs/ns:SearchInfo/ns:DateIntervals">
                                    <td>
                                        <xsl:value-of select="ns:dateFrom"/>
                                    </td>
                                    <td>
                                        <xsl:value-of select="ns:dateTo"/>
                                    </td>
                                </xsl:for-each>
                            </tr>
                    </table>
                </div>
                <div>
                    <table border="1" width="100%">
                        <caption>
                            <b>Результат поиска</b>
                        </caption>
                        <tr bgcolor="#9acd32">
                            <th>Дата и время</th>
                            <th>Имя файла</th>
                            <th>Содержимое</th>
                        </tr>
                        <xsl:for-each select="ns:Logs/ns:SearchInfoResult/ns:ResultLogs">
                            <tr>
                                <td width="10%">
                                    <xsl:value-of select="ns:timeMoment"/>
                                </td>
                                <td width="10%">
                                    <xsl:value-of select="ns:fileName"/>
                                </td>
                                <td width="85%">
                                    <xsl:value-of select="ns:content"/>
                                </td>
                            </tr>
                        </xsl:for-each>
                        <xsl:if test="count(ns:Logs/ns:SearchInfoResult/ns:ResultLogs) = 0">
                            <tr>
                                <td colspan="3" align="center">
                                    <xsl:value-of select="ns:Logs/ns:SearchInfoResult/ns:emptyResultMessage"/>
                                </td>
                            </tr>
                        </xsl:if>
                    </table>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>