<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <!--
    Recalculating flag values in ".forward" jump
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="o[@tt=&quot;f&quot;]/o[2]/o[2]/o[2]">
    <xsl:variable name="current" select="."/>
    <xsl:element name="o">
      <xsl:attribute name="base">
        <xsl:text>org.eolang.int</xsl:text>
      </xsl:attribute>
      <xsl:attribute name="data">
        <xsl:text>int</xsl:text>
      </xsl:attribute>
      <xsl:value-of select="count($current/preceding::o[@cnt=$current/parent::o/parent::o/parent::o/@which])+1"/>
    </xsl:element>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
