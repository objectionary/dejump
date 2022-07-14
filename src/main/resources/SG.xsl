<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="SG" version="2.0">
    <!--
    Simple goto
    -->
    <xsl:output indent="no" method="text"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match='node()|@*'>
        <xsl:copy>
            <xsl:apply-templates select='@*|node()'/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match='//o[@base="bacward"]'>
        <xsl:choose>
            <xsl:when test=".."></xsl:when>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>