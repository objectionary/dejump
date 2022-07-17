<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="SG" version="2.0">
    <!--
    Simple goto
    -->
    <xsl:output indent="yes" method="xml"/>
    <xsl:strip-space elements="*"/>
    <xsl:variable name="gotos" select='//o[@base="goto"]'/>
    <xsl:variable name="g" select='$gotos/o/o[1]/@name'/>
    <xsl:template match="o[@base=$g]">
        <xsl:variable name="cur" select="following-sibling::o[1]/@base"/>
        <xsl:choose>
            <xsl:when test='not(parent::o[@base=".if"] and parent::o/o[3][@base=$cur])'>
                <o base=".if">
                    <o base="bool" data="bool">true</o>
                    <xsl:copy-of select="."/>
                    <xsl:copy-of select="following-sibling::o[1]"/>
                    <o base="bool" data="bool">true</o>
                </o>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="."/>
                <xsl:copy-of select="following-sibling::o[1]"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match='o[@base=".backward" or @base=".forward"]'/>
    <xsl:template match='node()|@*'>
        <xsl:copy>
            <xsl:apply-templates select='node()|@*'/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>