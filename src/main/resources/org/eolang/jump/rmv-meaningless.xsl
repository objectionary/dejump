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
    <xsl:template match='node()|@*'>
        <xsl:copy>
            <xsl:apply-templates select='@*|node()'/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>