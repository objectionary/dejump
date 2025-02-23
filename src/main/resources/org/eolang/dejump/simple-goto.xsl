<?xml version="1.0" encoding="UTF-8"?>
<!--
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="SG" version="2.0">
  <!--
    Simple goto + adding "seq > @" inside "goto"
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="o[@base=&quot;.forward&quot; or @base=&quot;.backward&quot;]">
    <xsl:choose>
      <xsl:when test="parent::o[@base=&quot;.if&quot;][o[2][@base=&quot;.forward&quot; or @base=&quot;.backward&quot;]]">
        <xsl:copy-of select="."/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:element name="o">
          <xsl:attribute name="base">
            <xsl:text>.if</xsl:text>
          </xsl:attribute>
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:text>org.eolang.bool</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="data">
              <xsl:text>bool</xsl:text>
            </xsl:attribute>
            <xsl:text>true</xsl:text>
          </xsl:element>
          <xsl:copy-of select="."/>
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:text>org.eolang.bool</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="data">
              <xsl:text>bool</xsl:text>
            </xsl:attribute>
            <xsl:text>true</xsl:text>
          </xsl:element>
        </xsl:element>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="o[ends-with(@base,&quot;goto&quot;) and not(ends-with(o[1]/o[2]/@base,&quot;seq&quot;))]/o[1]/o[2]">
    <xsl:element name="o">
      <xsl:attribute name="base">
        <xsl:text>org.eolang.seq</xsl:text>
      </xsl:attribute>
      <xsl:attribute name="name">
        <xsl:text>@</xsl:text>
      </xsl:attribute>
      <xsl:apply-templates select="node()|@* except @name"/>
    </xsl:element>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
