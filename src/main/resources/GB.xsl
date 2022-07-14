<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="GB" version="2.0">
    <!--
    Goto Backward
    -->
    <xsl:output indent="no" method="text"/>
    <xsl:strip-space elements="*"/>

    <xsl:template match='node()|@*'>
        <xsl:copy>
            <xsl:apply-templates select='@*|node()'/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match='//o[@base="goto"]'>
        <o base=".while" line="{@line}" pos="{@pos}">
            <o base=".eq" line="{@line}" pos="{@pos}">
                <o base="flag" line="{@line}" pos="{@pos}"/>
                <o base="int" data="int" line="{@line}" pos="{@pos}">0</o>
                <xsl:apply-templates/>
            </o>
        </o>
    </xsl:template>



</xsl:stylesheet>