<?xml version="1.0" encoding="UTF-8"?>
<!--
The MIT License (MIT)

Copyright (c) 2016-2022 Yegor Bugayenko

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
    <xsl:template match="program/sheets">
        <!-- Not important -->
    </xsl:template>
    <xsl:template match="@line">
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
    <xsl:template match='o[starts-with(@base, "flag_")]' priority="1">
        <xsl:copy>
            <xsl:attribute name="base"><xsl:text>flag</xsl:text></xsl:attribute>
            <xsl:apply-templates select="node()|@* except @base"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match='o[starts-with(@name, "flag_")]' priority="2">
        <xsl:copy>
            <xsl:attribute name="name"><xsl:text>flag</xsl:text></xsl:attribute>
            <xsl:apply-templates select="node()|@* except @name"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match='o[starts-with(@base,"org.eolang.")]'>
        <xsl:variable name="pack" select="@base"/>
        <xsl:copy>
            <xsl:attribute name="base"><xsl:value-of select="substring($pack,12)"/></xsl:attribute>
            <xsl:apply-templates select="node()|@* except @base"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="node()|@*">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>