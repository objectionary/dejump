<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="add-fl" version="2.0">
  <!--
    Add "fl" attribute to each object ".if" that encapsulates ".forward" or ".backward"
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="o[@tt]">
    <xsl:element name="o">
      <xsl:attribute name="base" select="@base"/>
      <xsl:if test="@name">
        <xsl:attribute name="name">
          <xsl:value-of select="@name"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:attribute name="which" select="@which"/>
      <xsl:attribute name="rem" select="@which"/>
      <xsl:attribute name="tt" select="@tt"/>
      <xsl:attribute name="fl">
        <xsl:choose>
          <xsl:when test="@tt=&quot;f&quot;">
            <xsl:value-of select="o[2]/o[2]/o[1]/@base"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="o[2]/o[1]/o[1]/@base"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>
  <xsl:template match="o[ends-with(@base,&quot;goto&quot;)]">
    <xsl:copy>
      <xsl:attribute name="uniq">
        <xsl:value-of select="concat(&quot;g_&quot;,generate-id())"/>
      </xsl:attribute>
      <!--<xsl:attribute name="const">
        <xsl:text>""</xsl:text>
      </xsl:attribute>-->
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="o[@tt=&quot;f&quot;]/o[2]/o[1]">
    <xsl:copy>
      <xsl:attribute name="cnt">
        <xsl:value-of select="parent::o/parent::o/@which"/>
      </xsl:attribute>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
