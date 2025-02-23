<?xml version="1.0"?>
<!--
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <!--
    Adds "temp" attribute
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="o[ends-with(@base,&quot;goto&quot;)]">
    <xsl:variable name="curName" select="o[1]/o[1]/@name"/>
    <xsl:variable name="current" select="."/>
    <xsl:choose>
      <xsl:when test="$current//o[@rem=$curName]">
        <xsl:copy>
          <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
      </xsl:when>
      <xsl:otherwise>
        <xsl:variable name="tempFlag" select="concat(&quot;flag_temp_&quot;,generate-id())"/>
        <xsl:copy>
          <xsl:attribute name="temp">
            <xsl:value-of select="$tempFlag"/>
          </xsl:attribute>
          <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
