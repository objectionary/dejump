<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="GF_1" version="2.0">
    <!--
    Goto forward
    Changing object ".if" with ".forward"
    -->
    <xsl:output indent="yes" method="xml"/>
    <xsl:strip-space elements="*"/>
    <xsl:variable name="gotos" select='//o[@base="goto"]'/>
    <xsl:variable name="g" select='$gotos/o/o[1]/@name'/>
    <xsl:template match='o[@base=$g and following-sibling::o[1][@base=".forward"]]'>
        <xsl:variable name="flag" select="concat('flag_',generate-id())"/>
        <xsl:element name="o">
            <xsl:attribute name="base"><xsl:text>seq</xsl:text></xsl:attribute>
            <xsl:copy-of select="following-sibling::o[1]/o"/>
            <xsl:element name="o">
                <xsl:attribute name="temp"><xsl:value-of select="@base"/></xsl:attribute>
                <xsl:attribute name="base"><xsl:value-of select="$flag"/></xsl:attribute>
            </xsl:element>
            <xsl:element name="o">
                <xsl:attribute name="base"><xsl:text>.write</xsl:text></xsl:attribute>
                <xsl:attribute name="method"/>
                <xsl:element name="o">
                    <xsl:attribute name="base"><xsl:text>int</xsl:text></xsl:attribute>
                    <xsl:attribute name="data"><xsl:text>int</xsl:text></xsl:attribute>
                    <xsl:value-of select="1"/>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    <xsl:template match='o[@base=".forward"]'/>
    <xsl:template match='node()|@*'>
        <xsl:copy>
            <xsl:apply-templates select='node()|@*'/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>