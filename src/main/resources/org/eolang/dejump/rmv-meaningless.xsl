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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="rmv" version="2.0">
  <!--
    Removes meaningless attributes/names: (in "{}" brackets - first occurrence)

    @which - name of GOTO argument, which calls the corresponding jump             { change-condition-of-jump.xsl }
    @tt - type of jump 'f'/'b' for ".if" object, which encapsulates it             { change-condition-of-jump.xsl }
    @fl - flagName for ".if", containing .forward/.backward                        { add-fl.xsl }
    @cnt - for counting preceding return-values for ".forward" jump                { add-fl.xsl }
    @rem - same as @which                                                          { add-fl.xsl }
    @uniq - unique name for each "goto" object to remind its result of dataization { add-fl.xsl }
    @ww - marks unprocessed ".while" objects                                       { add-order-for-while.xsl }
    @temp - flagName for other flags (which are don't replace jumps)               { goto-to-while.xsl }
    @conv - marks ".while" objects, that was "goto" objects                        { goto-to-while.xsl }

    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="@fl">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@which">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@tt">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@rem">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@temp">
    <!-- Not important -->
  </xsl:template>
  <xsl:template match="@ww">
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
