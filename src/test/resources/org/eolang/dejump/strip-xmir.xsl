<?xml version="1.0" encoding="UTF-8"?>
<!--
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <!--
    This one removes all 'meaningless' elements from XMIR. We
    use this one to compare two XMIR documents for semantic
    equivalence.
    -->
  <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="/program/@*">
    <!-- Program attributes are not important -->
  </xsl:template>
  <xsl:template match="/program/listing">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="/program/errors">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="/program/sheets">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@alias">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@line">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@cut">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@pos">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@ref">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@which">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@fl">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@tt">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@rem">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@ww">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@temp">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@cnt">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@conv">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@uniq">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="o[starts-with(@base, &quot;flag_&quot;)]" priority="1">
    <xsl:copy>
      <xsl:attribute name="base">
        <xsl:text>flag</xsl:text>
      </xsl:attribute>
      <xsl:apply-templates select="node()|@* except @base"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="o[starts-with(@name, &quot;flag_&quot;)]" priority="2">
    <xsl:copy>
      <xsl:attribute name="name">
        <xsl:text>flag</xsl:text>
      </xsl:attribute>
      <xsl:apply-templates select="node()|@* except @name"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="o[starts-with(@base, &quot;g_&quot;)]" priority="1">
    <xsl:copy>
      <xsl:attribute name="base">
        <xsl:text>g_</xsl:text>
      </xsl:attribute>
      <xsl:apply-templates select="node()|@* except @base"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="o[starts-with(@name, &quot;g_&quot;)]" priority="2">
    <xsl:copy>
      <xsl:attribute name="name">
        <xsl:text>g_</xsl:text>
      </xsl:attribute>
      <xsl:apply-templates select="node()|@* except @name"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="o[starts-with(@base,&quot;org.eolang.&quot;)]">
    <xsl:variable name="pack" select="@base"/>
    <xsl:copy>
      <xsl:attribute name="base">
        <xsl:value-of select="substring($pack,12)"/>
      </xsl:attribute>
      <xsl:apply-templates select="node()|@* except @base"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
