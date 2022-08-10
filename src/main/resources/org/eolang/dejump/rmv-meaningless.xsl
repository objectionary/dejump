<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="rmv" version="2.0">
    <!--
    Removes meaningless attributes/names
    -->
    <xsl:output indent="yes" method="xml"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match="@fl">
        <!-- Not important -->
    </xsl:template>
    <xsl:template match="@which">
        <!-- Not important -->
    </xsl:template>
    <xsl:template match="@tt">
        <!-- Not important -->
    </xsl:template>
    <xsl:template match="@rem">
        <!-- Not important -->
    </xsl:template>
    <xsl:template match="@temp">
        <!-- Not important -->
    </xsl:template>
    <xsl:template match="@ww">
        <!-- Not important -->
    </xsl:template>
    <xsl:template match='o[starts-with(@base,"org.eolang.")]'>
        <xsl:variable name="pack" select="@base"/>
        <xsl:copy>
            <xsl:attribute name="base"><xsl:value-of select="substring($pack,12)"/></xsl:attribute>
            <xsl:apply-templates select="node()|@* except @base"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match='node()|@*'>
        <xsl:copy>
            <xsl:apply-templates select='node()|@*'/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>