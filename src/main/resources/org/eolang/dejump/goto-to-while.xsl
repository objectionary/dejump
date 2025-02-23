<?xml version="1.0" encoding="UTF-8"?>
<!--
 * SPDX-FileCopyrightText: Copyright (c) 2022 Mikhail Lipanin
 * SPDX-License-Identifier: MIT
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
  <!--
    Transforms "goto" objects to ".while"
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="o[ends-with(@base,&quot;goto&quot;)]">
    <xsl:variable name="curName" select="o[1]/o[1]/@name"/>
    <xsl:variable name="current" select="."/>
    <xsl:element name="o">
      <xsl:attribute name="base">
        <xsl:text>.while</xsl:text>
      </xsl:attribute>
      <xsl:attribute name="name">
        <xsl:value-of select="$current/@name"/>
      </xsl:attribute>
      <xsl:attribute name="const">
        <xsl:value-of select="$current/@const"/>
      </xsl:attribute>
      <xsl:attribute name="conv">
        <xsl:text>CONVERTED</xsl:text>
      </xsl:attribute>
      <xsl:choose>
        <xsl:when test="$current//o[@rem=$curName]">
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:text>.or</xsl:text>
            </xsl:attribute>
            <xsl:for-each select="$current//o[@rem=$curName]">
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
                      <xsl:text>-1</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:text>0</xsl:text>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:element>
              </xsl:element>
            </xsl:for-each>
          </xsl:element>
          <xsl:element name="o">
            <xsl:attribute name="abstract"/>
            <xsl:element name="o">
              <xsl:attribute name="name">
                <xsl:text>i</xsl:text>
              </xsl:attribute>
            </xsl:element>
            <xsl:element name="o">
              <xsl:attribute name="base">
                <xsl:text>org.eolang.seq</xsl:text>
              </xsl:attribute>
              <xsl:attribute name="name">
                <xsl:text>@</xsl:text>
              </xsl:attribute>
              <xsl:for-each select="$current//o[@rem=$curName]">
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
                        <xsl:text>0</xsl:text>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:text>1</xsl:text>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                </xsl:element>
              </xsl:for-each>
              <xsl:apply-templates select="$current/o[1]/o[2]"/>
            </xsl:element>
          </xsl:element>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="tempFlag" select="$current/@temp"/>
          <xsl:element name="o">
            <xsl:attribute name="base">
              <xsl:text>.eq</xsl:text>
            </xsl:attribute>
            <xsl:element name="o">
              <xsl:attribute name="base">
                <xsl:value-of select="$tempFlag"/>
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
          <xsl:element name="o">
            <xsl:attribute name="abstract"/>
            <xsl:element name="o">
              <xsl:attribute name="name">
                <xsl:text>i</xsl:text>
              </xsl:attribute>
            </xsl:element>
            <xsl:element name="o">
              <xsl:attribute name="base">
                <xsl:text>org.eolang.seq</xsl:text>
              </xsl:attribute>
              <xsl:attribute name="name">
                <xsl:text>@</xsl:text>
              </xsl:attribute>
              <xsl:element name="o">
                <xsl:attribute name="base">
                  <xsl:text>.write</xsl:text>
                </xsl:attribute>
                <xsl:element name="o">
                  <xsl:attribute name="base">
                    <xsl:value-of select="$tempFlag"/>
                  </xsl:attribute>
                  <xsl:attribute name="temp">
                    <xsl:value-of select="$tempFlag"/>
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
              <xsl:apply-templates select="$current/o[1]/o[2]"/>
            </xsl:element>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
  </xsl:template>
  <xsl:template match="o[ends-with(@base,&quot;goto&quot;)]/o[1]/o[2]/@name">
    <!--Delete @-attribute from object-->
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
