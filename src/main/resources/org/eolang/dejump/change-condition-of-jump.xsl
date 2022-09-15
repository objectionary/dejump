<?xml version="1.0" encoding="UTF-8"?>
<!--
The MIT License (MIT)

Copyright (c) 2022 Mikhail Lipanin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
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
