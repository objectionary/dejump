<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <!--
    If no jumps are done, dataize last object in "goto"
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="o[ends-with(@base,&quot;seq&quot;) and o[1]/@conv]/o[last()]">
    <xsl:choose>
      <xsl:when test="@base=&quot;.if&quot; and starts-with(o[1]/o[1]/@base,&quot;flag&quot;)">
        <xsl:element name="o">
          <xsl:attribute name="base">
            <xsl:text>.if</xsl:text>
          </xsl:attribute>
          <xsl:copy-of select="o[1]"/>
          <xsl:copy-of select="o[2]"/>
          <xsl:copy-of select="parent::o/o[1]/o[2]/o[2]/o[last()]"/>
        </xsl:element>
      </xsl:when>
      <xsl:otherwise>
        <copy>
          <xsl:apply-templates select="node()|@*"/>
        </copy>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
