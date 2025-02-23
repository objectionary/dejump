<?xml version="1.0"?>
<!--
 * Copyright (c) 2022 Mikhail Lipanin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="add-fl" version="2.0">
  <!--
    Add "fl" attribute to each object ".if" that encapsulates ".forward" or ".backward"
    and "uniq" attribute to each object "goto"
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
