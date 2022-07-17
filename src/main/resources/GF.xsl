<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="GF" version="2.0">
    <!--
    Goto forward
    -->
    <xsl:output indent="yes" method="xml"/>
    <xsl:strip-space elements="*"/>

    <xsl:variable name="gotos" select='//o[@base="goto"]'/>
    <xsl:variable name="g" select='$gotos/o/o[1]/@name'/>

    <xsl:template name="flag">
        <xsl:variable name="cur" select="."/>
        <xsl:variable name="rem">
            <xsl:choose>
                <xsl:when test="$cur/o[1][text()]">
                    <xsl:value-of select="number($cur/o[1]/text())+1"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="0"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <o base="memory">
            <xsl:attribute name="name">
                <xsl:value-of select='concat(flag,$rem)'/>
            </xsl:attribute>
            <o base="int" data="int">
                <xsl:value-of select="$rem"/>
            </o>
        </o>
    </xsl:template>

    <xsl:template match='objects'>
        <xsl:variable name="o" select="."/>
        <xsl:for-each select="//o[@base=$g]">
            <xsl:variable name="cur" select="."/>
            <xsl:copy>
                <xsl:element name="cnt">
                    <xsl:value-of select="0"/>
                </xsl:element>
                <xsl:apply-templates select='node()|@*'/>
            </xsl:copy>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match='node()|@*'>
        <xsl:copy>
            <xsl:apply-templates select='node()|@*'/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>