<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:w="http://schemas.microsoft.com/office/word/2003/wordml"
                xmlns:ns="http://bmstu.ru/logsearch">

    <xsl:template match="/">
        <w:wordDocument>
            <w:styles>
                <w:style w:type="paragraph" w:styleId="Heading3">
                    <w:name w:val="heading 3"/>
                    <w:pPr>
                        <w:pStyle w:val="Heading3"/>
                        <w:keepNext/>
                        <w:spacing w:before="240" w:after="60"/>
                        <w:outlineLvl w:val="2"/>
                    </w:pPr>
                </w:style>
            </w:styles>
            <w:body>
                <w:p>
                    <w:pPr>
                        <w:pStyle w:val="Heading3"/>
                    </w:pPr>
                    <w:r>
                        <w:t>Данные запроса</w:t>
                    </w:r>
                </w:p>
                <w:p/>
                <w:p>
                    <w:r>
                        <w:t>Регулярное выражение:
                            <xsl:value-of select="ns:Logs/ns:SearchInfo/ns:regularExpression"/>
                        </w:t>
                    </w:r>
                </w:p>
                <w:p>
                    <w:r>
                        <w:t>Директория:
                            <xsl:value-of select="ns:Logs/ns:SearchInfo/ns:location"/>
                        </w:t>
                    </w:r>
                </w:p>
                <w:p>
                    <w:r>
                        <w:t>Интервалы:</w:t>
                    </w:r>
                </w:p>
                <xsl:for-each select="ns:Logs/ns:SearchInfo/ns:DateIntervals">
                    <w:p>
                        <w:r>
                            <w:t>
                                С: <xsl:value-of select="ns:dateFrom"/>
                                По:<xsl:value-of select="ns:dateTo"/>
                            </w:t>
                        </w:r>
                    </w:p>
                </xsl:for-each>
                <w:p/>
                <w:p>
                    <w:pPr>
                        <w:pStyle w:val="Heading3"/>
                    </w:pPr>
                    <w:r>
                        <w:t>Результат поиска</w:t>
                    </w:r>
                </w:p>
                <w:p/>
                <xsl:for-each select="ns:Logs/ns:SearchInfoResult/ns:ResultLogs">
                    <w:p>
                        <w:r>
                            <w:t>Дата и время:       <xsl:value-of select="ns:timeMoment"/></w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:r>
                            <w:t>Имя файла:     <xsl:value-of select="ns:fileName"/></w:t>
                        </w:r>
                    </w:p>
                    <w:p>
                        <w:r>
                            <w:t>Содержимое:       <xsl:value-of select="ns:content"/></w:t>
                        </w:r>
                    </w:p>
                    <w:p/>
                </xsl:for-each>
                <xsl:if test="count(ns:Logs/ns:SearchInfoResult/ns:ResultLogs) = 0">
                    <w:p>
                        <w:r>
                            <w:t><xsl:value-of select="ns:Logs/ns:SearchInfoResult/ns:emptyResultMessage"/></w:t>
                        </w:r>
                    </w:p>
                </xsl:if>
            </w:body>
        </w:wordDocument>
    </xsl:template>

</xsl:stylesheet>