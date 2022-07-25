<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="GF_1" version="2.0">
    <!--
    Goto forward
    Changes object ".if" with ".forward" inside
    -->
    <xsl:output indent="yes" method="xml"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match='o[@base=".if" and o[2][@base=".forward"]]'>
        <xsl:element name="o">
            <xsl:attribute name="base"><xsl:value-of select="@base"/></xsl:attribute>
            <xsl:copy-of select="o[1]"/>
            <xsl:element name="o">
                <xsl:attribute name="base"><xsl:text>org.eolang.seq</xsl:text></xsl:attribute>
                <xsl:copy-of select='o[2]/o[2]'/>
                <xsl:element name="o">
                    <xsl:attribute name="base">.write</xsl:attribute>
                    <xsl:element name="o">
                        <xsl:attribute name="base"><xsl:value-of select="concat('flag_',generate-id())"/></xsl:attribute>
                    </xsl:element>
                    <xsl:element name="o">
                        <xsl:attribute name="base"><xsl:text>org.eolang.int</xsl:text></xsl:attribute>
                        <xsl:attribute name="data"><xsl:text>int</xsl:text></xsl:attribute>
                        <xsl:text>1</xsl:text>
                    </xsl:element>
                </xsl:element>
            </xsl:element>
            <xsl:copy-of select="o[3]"/>
        </xsl:element>
    </xsl:template>
    <xsl:template match='node()|@*'>
        <xsl:copy>
            <xsl:apply-templates select='node()|@*'/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>