<?xml version="1.0" encoding="UTF-8"?>
<!--
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="change-condition-of-jump" version="2.0">
  <!--
    Changes object ".if" with ".forward" or ".backward" inside
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="o[@base=&quot;.if&quot; and o[2][@base=&quot;.forward&quot;]]">
    <xsl:element name="o">
      <xsl:attribute name="base">
        <xsl:value-of select="@base"/>
      </xsl:attribute>
      <xsl:if test="@name">
        <xsl:attribute name="name">
          <xsl:value-of select="@name"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:attribute name="which">
        <xsl:value-of select="o[2]/o[1]/@base"/>
      </xsl:attribute>
      <xsl:attribute name="tt">
        <xsl:text>f</xsl:text>
      </xsl:attribute>
      <xsl:copy-of select="o[1]"/>
      <xsl:element name="o">
        <xsl:attribute name="base">
          <xsl:text>org.eolang.seq</xsl:text>
        </xsl:attribute>
        <xsl:copy-of select="o[2]/o[2]"/>
        <xsl:element name="o">
          <xsl:attribute name="base">.write</xsl:attribute>
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:value-of select="concat('flag_',generate-id())"/>
            </xsl:attribute>
          </xsl:element>
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:text>org.eolang.int</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="data">
              <xsl:text>int</xsl:text>
            </xsl:attribute>
            <xsl:text>1</xsl:text>
          </xsl:element>
        </xsl:element>
      </xsl:element>
      <xsl:copy-of select="o[3]"/>
    </xsl:element>
  </xsl:template>
  <xsl:template match="o[@base=&quot;.if&quot; and o[2][@base=&quot;.backward&quot;]]">
    <xsl:element name="o">
      <xsl:attribute name="base">
        <xsl:value-of select="@base"/>
      </xsl:attribute>
      <xsl:if test="@name">
        <xsl:attribute name="name">
          <xsl:value-of select="@name"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:attribute name="which">
        <xsl:value-of select="o[2]/o[1]/@base"/>
      </xsl:attribute>
      <xsl:attribute name="tt">
        <xsl:text>b</xsl:text>
      </xsl:attribute>
      <xsl:copy-of select="o[1]"/>
      <xsl:element name="o">
        <xsl:attribute name="base">
          <xsl:text>org.eolang.seq</xsl:text>
        </xsl:attribute>
        <xsl:element name="o">
          <xsl:attribute name="base">.write</xsl:attribute>
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:value-of select="concat('flag_',generate-id())"/>
            </xsl:attribute>
          </xsl:element>
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
      </xsl:element>
      <xsl:copy-of select="o[3]"/>
    </xsl:element>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
