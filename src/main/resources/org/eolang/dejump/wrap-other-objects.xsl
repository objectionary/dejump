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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="wrap" version="2.0">
  <!--
    Encapsulating objects, which are inside "goto" and follows ".forward"/".backward", into ".if" object
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:variable name="curNode" select="//objects/descendant::*[@which][1]"/>
  <xsl:variable name="curName" select="$curNode/@which"/>
  <xsl:variable name="curFlag" select="$curNode/@fl"/>
  <xsl:variable name="curType" select="$curNode/@tt"/>
  <xsl:template match="o[ends-with(@base,&quot;goto&quot;) and o[1]/o[1][@name=$curName]]//o" priority="1">
    <xsl:variable name="current" select="."/>
    <xsl:choose>
      <xsl:when test="$current/preceding::o[generate-id(.)=generate-id($curNode)]">
        <xsl:choose>
          <xsl:when test="not(preceding-sibling::o[preceding::o[generate-id(.)=generate-id($curNode)]]) and not(ancestor::o[preceding::o[generate-id(.)=generate-id($curNode)]])">
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
                    <xsl:value-of select="$curFlag"/>
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
                    <xsl:when test="$curType=&quot;b&quot;">
                      <xsl:text>1</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:text>0</xsl:text>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:element>
              </xsl:element>
              <xsl:element name="o">
                <xsl:attribute name="base">
                  <xsl:text>org.eolang.seq</xsl:text>
                </xsl:attribute>
                <xsl:copy-of select="$current"/>
                <xsl:copy-of select="$current/following-sibling::o"/>
              </xsl:element>
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
          </xsl:when>
          <xsl:otherwise>
            <!--Remove original ones-->
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:otherwise>
        <xsl:copy>
          <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="objects/descendant::o[@which][1]" priority="2">
    <xsl:copy>
      <xsl:apply-templates select="node()|@* except @which"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
