<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ns="http://bmstu.ru/logsearch">

    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

    <xsl:template match="/">
        <xsl:copy-of select="ns:Logs"/>
    </xsl:template>

</xsl:stylesheet>