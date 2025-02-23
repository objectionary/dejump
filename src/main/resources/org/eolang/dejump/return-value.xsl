<?xml version="1.0"?>
<!--
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="return-value" version="2.0">
  <!--
    Adds argument of ".forward" object after "goto"
    this one:
      goto > uniq!
        [g]
          seq > @
            ...
            flag.write 0
            ...
    maps to:
      seq
        flag.write -1
        goto > uniq!
          [g]
            seq > @
              ...
              flag.write 1
              ...
        if.
          flag.eq 1
          smth
          uniq
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="o[ends-with(@base,&quot;goto&quot;)]" priority="2">
    <xsl:variable name="curGOTO" select="."/>
    <xsl:element name="o">
      <xsl:attribute name="base">
        <xsl:text>org.eolang.seq</xsl:text>
      </xsl:attribute>
      <xsl:if test="$curGOTO[@name]">
        <xsl:attribute name="name">
          <xsl:value-of select="$curGOTO/@name"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:choose>
        <xsl:when test="not($curGOTO[@temp])">
          <xsl:for-each select="$curGOTO//o[@rem=$curGOTO/o[1]/o[1]/@name]">
            <xsl:element name="o">
              <xsl:attribute name="base">
                <xsl:text>.write</xsl:text>
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
                    <xsl:text>-1</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:text>0</xsl:text>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:element>
            </xsl:element>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:text>.write</xsl:text>
            </xsl:attribute>
            <xsl:element name="o">
              <xsl:attribute name="base">
                <xsl:value-of select="@temp"/>
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
        </xsl:otherwise>
      </xsl:choose>
      <xsl:copy>
        <xsl:attribute name="name">
          <xsl:value-of select="$curGOTO/@uniq"/>
        </xsl:attribute>
        <xsl:attribute name="const">
          <xsl:text/>
        </xsl:attribute>
        <xsl:apply-templates select="node()|@* except @name"/>
      </xsl:copy>
      <xsl:for-each select="descendant::o[@tt=&quot;f&quot; and @rem=$curGOTO/o[1]/o[1]/@name and o[2]/o[1]/@base!=&quot;.write&quot;]">
        <xsl:variable name="current" select="."/>
        <xsl:element name="o">
          <xsl:attribute name="base">
            <xsl:text>.if</xsl:text>
          </xsl:attribute>
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:text>.eq</xsl:text>
            </xsl:attribute>
            <xsl:element name="o">
              <xsl:attribute name="base">
                <xsl:value-of select="$current/@fl"/>
              </xsl:attribute>
            </xsl:element>
            <xsl:element name="o">
              <xsl:attribute name="base">
                <xsl:text>org.eolang.int</xsl:text>
              </xsl:attribute>
              <xsl:attribute name="data">
                <xsl:text>int</xsl:text>
              </xsl:attribute>
              <xsl:value-of select="$current/o[2]/o[2]/o[2]/text()"/>
            </xsl:element>
          </xsl:element>
          <xsl:copy-of select="$current/o[2]/o[1]"/>
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:value-of select="$curGOTO/@uniq"/>
            </xsl:attribute>
          </xsl:element>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
  <xsl:template match="o[@tt=&quot;f&quot; and o[2]/o[1]/@base!=&quot;.write&quot;]/o[2]/o[1]" priority="1">
    <!--Remove original ones-->
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
