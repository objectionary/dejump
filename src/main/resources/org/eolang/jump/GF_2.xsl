<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="GF_2" version="2.0">
    <!--
    Goto forward
    Encapsulating objects that follows ".forward" in ".if" object
    -->
    <xsl:output indent="yes" method="xml"/>
    <xsl:strip-space elements="*"/>
    <xsl:variable name="gotos" select='//o[@base="goto"]'/>
    <xsl:variable name="g" select='$gotos/o/o[1]/@name'/>
    <xsl:template match='o[@base="goto"]//*[preceding::o[@base=".if"]/o[2]/o[2][@temp=$g]][not(preceding-sibling::*[preceding::o[@base=".if"]/o[2]/o[2][@temp=$g]])][not(ancestor::*[preceding::o[@base=".if"]/o[2]/o[2][@temp=$g]])]'>
        <xsl:variable name="cur" select="."/>
        <xsl:variable name="curName" select='ancestor::*[@base="goto"][1]/o/o[1]/@name'/>
        <xsl:variable name="flags" select='ancestor::*[@base="goto"][1]//*[@temp=$curName]'/>
        <!--<xsl:copy>
            <xsl:attribute name="LOL">
                <xsl:value-of select="$curName"/>
            </xsl:attribute>
            <xsl:attribute name="KEK">
                <xsl:value-of select="count($flags)"/>
            </xsl:attribute>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>-->

        <xsl:for-each select="$flags">
            <xsl:variable name="flag" select="@base"/>
            <xsl:element name="o">
                <xsl:attribute name="base"><xsl:text>.if</xsl:text></xsl:attribute>
                <xsl:element name="o">
                    <xsl:attribute name="base"><xsl:text>.eq</xsl:text></xsl:attribute>
                    <xsl:element name="o">
                        <xsl:attribute name="base"><xsl:value-of select="$flag"/></xsl:attribute>
                    </xsl:element>
                    <xsl:element name="o">
                        <xsl:attribute name="base"><xsl:text>int</xsl:text></xsl:attribute>
                        <xsl:attribute name="data"><xsl:text>int</xsl:text></xsl:attribute>
                        <xsl:value-of select="0"/>
                    </xsl:element>
                </xsl:element>
                <xsl:element name="o">
                    <xsl:attribute name="base"><xsl:text>seq</xsl:text></xsl:attribute>
                    <xsl:copy-of select="$cur"/>
                    <xsl:copy-of select='$cur/following-sibling::*'/>
                </xsl:element>
                <xsl:element name="o">
                    <xsl:attribute name="base"><xsl:text>bool</xsl:text></xsl:attribute>
                    <xsl:attribute name="data"><xsl:text>bool</xsl:text></xsl:attribute>
                    <xsl:text>true</xsl:text>
                </xsl:element>
            </xsl:element>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match='o[@base="goto"]//*[preceding::o[@base=".if"]/o[2]/o[2][@temp=$g]][preceding-sibling::*[preceding::o[@base=".if"]/o[2]/o[2][@temp=$g]]]'/>
    <xsl:template match='o[@base="goto"]//*[preceding::o[@base=".if"]/o[2]/o[2][@temp=$g]][ancestor::*[preceding::o[@base=".if"]/o[2]/o[2][@temp=$g]]]'/>
    <xsl:template match='node()|@*'>
        <xsl:copy>
            <xsl:apply-templates select='node()|@*'/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>