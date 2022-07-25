<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="wrap" version="2.0">
    <!--
    Encapsulating objects, which are inside "goto" and follows ".forward"/".backward", in ".if" object
    -->
    <xsl:output indent="yes" method="xml"/>
    <xsl:strip-space elements="*"/>
    <xsl:variable name="curNode" select='//objects/descendant::o[@which][1]'/>
    <xsl:variable name="curName" select='//objects/descendant::o[@which][1]/@which'/>
    <xsl:variable name="curFlag" select='//objects/descendant::o[@which][1]/@fl'/>
    <xsl:variable name="curType" select='//objects/descendant::o[@which][1]/@tt'/>
    <xsl:template match='o[ends-with(@base,"goto") and o[1]/o[1][@name=$curName]]//o'>
        <xsl:variable name="current" select="."/>
        <xsl:choose>
            <xsl:when test='$current/preceding::o[count(.|$curNode)=count(.) and count(.)=count($curNode)]'>
                <xsl:choose>
                    <xsl:when test='not(preceding-sibling::o[preceding::o[count(.|$curNode)=count(.) and count(.)=count($curNode)]]) and not(ancestor::o[preceding::o[count(.|$curNode)=count(.) and count(.)=count($curNode)]])'>
                        <xsl:element name="o">
                            <xsl:attribute name="base"><xsl:text>.if</xsl:text></xsl:attribute>
                            <xsl:element name="o">
                                <xsl:attribute name="base"><xsl:text>.eq</xsl:text></xsl:attribute>
                                <xsl:element name="o">
                                    <xsl:attribute name="base"><xsl:value-of select="$curFlag"/></xsl:attribute>
                                </xsl:element>
                                <xsl:element name="o">
                                    <xsl:attribute name="base"><xsl:text>org.eolang.int</xsl:text></xsl:attribute>
                                    <xsl:attribute name="data"><xsl:text>int</xsl:text></xsl:attribute>
                                    <xsl:choose>
                                        <xsl:when test='$curType="b"'>
                                            <xsl:text>1</xsl:text>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <xsl:text>0</xsl:text>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:element>
                            </xsl:element>
                            <xsl:element name="o">
                                <xsl:attribute name="base"><xsl:text>org.eolang.seq</xsl:text></xsl:attribute>
                                <xsl:copy-of select="$current"/>
                                <xsl:copy-of select='$current/following-sibling::o'/>
                            </xsl:element>
                            <xsl:element name="o">
                                <xsl:attribute name="base"><xsl:text>org.eolang.bool</xsl:text></xsl:attribute>
                                <xsl:attribute name="data"><xsl:text>bool</xsl:text></xsl:attribute>
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
    <xsl:template match='objects/descendant::o[@which][1]'>
        <xsl:copy>
            <xsl:apply-templates select='node()|@* except @which'/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match='node()|@*'>
        <xsl:copy>
            <xsl:apply-templates select='node()|@*'/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>