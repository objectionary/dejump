<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <!--
    Initializes "flags"
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="objects/o[1]">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
      <xsl:for-each select="//o[@fl]">
        <xsl:element name="o">
          <xsl:attribute name="base">
            <xsl:text>org.eolang.memory</xsl:text>
          </xsl:attribute>
          <xsl:attribute name="name">
            <xsl:value-of select="@fl"/>
          </xsl:attribute>
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:text>org.eolang.int</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="data">
              <xsl:text>int</xsl:text>
            </xsl:attribute>
            <xsl:choose>
              <xsl:when test="@tt=&quot;f&quot;">
                <xsl:text>-1</xsl:text>
              </xsl:when>
              <xsl:otherwise>
                <xsl:text>0</xsl:text>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
      <xsl:for-each select="//o[@temp]">
        <xsl:element name="o">
          <xsl:attribute name="base">
            <xsl:text>org.eolang.memory</xsl:text>
          </xsl:attribute>
          <xsl:attribute name="name">
            <xsl:value-of select="@base"/>
          </xsl:attribute>
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:text>org.eolang.int</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="data">
              <xsl:text>int</xsl:text>
            </xsl:attribute>
            <xsl:text>0</xsl:text>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
