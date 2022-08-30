<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="rmv" version="2.0">
  <!--
    Removes meaningless attributes/names:

    @fl - flagName for ".if", containing .forward/.backward
    @which - name of GOTO argument, which calls the corresponding jump
    @rem - same as @which
    @tt - type of jump 'f'/'b' for ".if" object, which encapsulates it
    @temp - flagName for other flags (which are don't replace jumps)
    @ww - marks unprocessed ".while" objects
    @cnt - for counting preceding return-values for ".forward" jump
    @conv - marks ".while" objects, that was "goto" objects
    @uniq - unique name for each "goto" object to remind its result of dataization

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
