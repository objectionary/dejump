<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="TW" version="3.0">
  <!--
    Adds "flag-check" condition to object "while."
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:variable name="curNode" select="//o[@ww][1]"/>
  <xsl:template match="objects/descendant::o[@ww][1]" priority="2">
    <xsl:variable name="current" select="."/>
    <xsl:element name="o">
      <xsl:attribute name="base">
        <xsl:value-of select="@base"/>
      </xsl:attribute>
      <xsl:element name="o">
        <xsl:attribute name="base">
          <xsl:text>.and</xsl:text>
        </xsl:attribute>
        <xsl:for-each select="$current/descendant::o[@fl]">
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:text>.eq</xsl:text>
            </xsl:attribute>
            <xsl:element name="o">
              <xsl:attribute name="base">
                <xsl:value-of select="@fl"/>
              </xsl:attribute>
            </xsl:element>
            <xsl:element name="o">
              <xsl:attribute name="base">
                <xsl:text>org.eolang.int</xsl:text>
              </xsl:attribute>
              <xsl:attribute name="data">
                <xsl:text>int</xsl:text>
              </xsl:attribute>
              <xsl:choose>
                <xsl:when test="@tt=&quot;f&quot;">
                  <xsl:text>0</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:text>1</xsl:text>
                </xsl:otherwise>
              </xsl:choose>
            </xsl:element>
          </xsl:element>
        </xsl:for-each>
        <xsl:copy-of select="o[1]"/>
      </xsl:element>
      <xsl:copy-of select="o[2]"/>
    </xsl:element>
  </xsl:template>
  <xsl:template match="objects/descendant::o[@ww][1]" priority="1">
    <xsl:copy>
      <xsl:apply-templates select="node()|@* except @ww"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
