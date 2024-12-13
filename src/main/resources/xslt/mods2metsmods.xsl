<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ This file is part of ***  M y C o R e  ***
  ~ See http://www.mycore.de/ for details.
  ~
  ~ MyCoRe is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ MyCoRe is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with MyCoRe.  If not, see <http://www.gnu.org/licenses/>.
  -->

<xsl:stylesheet xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:mods="http://www.loc.gov/mods/v3"
                xmlns:mets="http://www.loc.gov/METS/"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:mcrmods="http://www.mycore.de/xslt/mods"
                version="3.0"
                exclude-result-prefixes="mods xlink">

    <xsl:include href="functions/mods.xsl" />

    <xsl:variable name="objectID" select="/mycoreobject/@ID"/>
    <xsl:variable name="object" select="/"/>

    <xsl:param name="WebApplicationBaseURL"/>
    <xsl:param name="license" select="'CC-BY-NC-SA'"/>
    <xsl:param name="MIR.DFGViewer.DV.Owner" select="''"/>
    <xsl:param name="MIR.DFGViewer.DV.OwnerLogo" select="''"/>
    <xsl:param name="MIR.DFGViewer.DV.OwnerSiteURL" select="''"/>
    <xsl:param name="MIR.DFGViewer.DV.OPAC.CATALOG.URL" select="''"/>
    <xsl:param name="CurrentLang" select="'de'"/>

    <xsl:mode on-no-match="shallow-copy"/>

    <xsl:key name="logDiv" match="mets:div" use="@ID"/>

    <xsl:template match="/mycoreobject">
        <xsl:if test="count(structure/derobjects/derobject)=0">
            <xsl:message terminate="yes">Object has no derivates!</xsl:message>
        </xsl:if>


        <xsl:for-each select="structure/derobjects/derobject">
            <xsl:variable name="derID" select="@xlink:href"/>
            <xsl:variable name="derivateIFSXML" select="document(concat('ifs:/', $derID))"/>
            <xsl:variable name="metsChild"
                          select="$derivateIFSXML/mcr_directory/children/child[@type='file' and name/text()='mets.xml']"/>
            <xsl:if test="count($metsChild)=1">
                <xsl:variable name="metsFileURI" select="concat('mcrfile:', $derID, '/mets.xml')"/>
                <xsl:variable name="metsContent" select="document($metsFileURI)"/>
                <xsl:apply-templates select="$metsContent/mets:mets">
                    <xsl:with-param name="derID" select="$derID"/>
                </xsl:apply-templates>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="mets:fptr">
        <xsl:variable name="fileID" select="@FILEID" />
        <xsl:variable name="fileURL" select="../../../.././/mets:file[@ID=$fileID]/mets:FLocat/@xlink:href" />
        <xsl:comment><xsl:value-of select="$fileURL" /></xsl:comment>
        <xsl:choose>
            <xsl:when test="ends-with($fileURL, '.jpg') or ends-with($fileURL, '.png') or ends-with($fileURL, '.tiff')">
                <mets:fptr FILEID="THUMBS_{@FILEID}"/>
                <mets:fptr FILEID="DEFAULT_{@FILEID}"/>
            </xsl:when>
            <xsl:when test="ends-with($fileURL, '.xml') and starts-with($fileURL, 'alto/')">
                <mets:fptr FILEID="FULLTEXT_{@FILEID}"/>
            </xsl:when>
            <xsl:otherwise>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <xsl:template match="mets:mets">
        <xsl:param name="derID"/>
        <xsl:variable name="result">
            <xsl:copy>
                <mets:amdSec ID="amd_{$objectID}">
                    <mets:rightsMD ID="rightsMD_263566811">
                        <mets:mdWrap MIMETYPE="text/xml" MDTYPE="OTHER" OTHERMDTYPE="DVRIGHTS">
                            <mets:xmlData>
                                <dv:rights xmlns:dv="http://dfg-viewer.de/">
                                    <!-- owner name -->
                                    <dv:owner>
                                        <xsl:value-of select="$MIR.DFGViewer.DV.Owner"/>
                                    </dv:owner>
                                    <dv:ownerLogo>
                                        <xsl:value-of select="$MIR.DFGViewer.DV.OwnerLogo"/>
                                    </dv:ownerLogo>
                                    <dv:ownerSiteURL>
                                        <xsl:value-of select="$MIR.DFGViewer.DV.OwnerSiteURL"/>
                                    </dv:ownerSiteURL>
                                    <dv:ownerContact/>
                                    <dv:license>
                                        <xsl:choose>
                                            <xsl:when
                                                    test="$object/mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:accessCondition[@type='use and reproduction']">
                                                <xsl:variable name="licenseClass" select="'mir_licenses'"/>
                                                <xsl:variable name="licenseId"
                                                              select="substring-after($object/mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:accessCondition[@type='use and reproduction']/@xlink:href, '#')"/>
                                                <xsl:variable name="licenseCategory"
                                                              select="document('classification:metadata:-1:children:mir_licenses')/mycoreclass//category[@ID=$licenseId]" />
                                                <xsl:choose>
                                                  <xsl:when test="$licenseCategory/url[@xlink:href]">
                                                    <xsl:value-of select="$licenseCategory/url/@xlink:href" />
                                                  </xsl:when>
                                                  <xsl:otherwise>
                                                    <xsl:value-of select="$licenseCategory/label[@xml:lang=$CurrentLang or position()=1]/@text" />
                                                  </xsl:otherwise>
                                                </xsl:choose>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <xsl:value-of select="$license"/>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </dv:license>
                                </dv:rights>
                            </mets:xmlData>
                        </mets:mdWrap>
                    </mets:rightsMD>
                    <mets:digiprovMD>
                        <xsl:attribute name="ID">
                            <xsl:value-of select="concat('digiprovMD',$objectID)"/>
                        </xsl:attribute>
                        <mets:mdWrap MIMETYPE="text/xml" MDTYPE="OTHER" OTHERMDTYPE="DVLINKS">
                            <mets:xmlData>
                                <dv:links xmlns:dv="http://dfg-viewer.de/">
                                    <xsl:variable name="ppn"
                                                  select="substring-after($object/mycoreobject/metadata/def.modsContainer/modsContainer/mods:mods/mods:identifier[@type='uri'], ':ppn:')"/>
                                    <xsl:if test="$ppn">
                                        <dv:reference>
                                            <xsl:variable name="catalogURL"
                                                          select="concat(substring-before($MIR.DFGViewer.DV.OPAC.CATALOG.URL, '{PPN}'), $ppn , substring-after($MIR.DFGViewer.DV.OPAC.CATALOG.URL, '{PPN}'))"/>
                                            <xsl:variable name="uriResolved"
                                                          select="document(concat($catalogURL,'?format=xml'))//rdf:Description[@rdf:about=normalize-space($catalogURL)]/*[local-name() = 'page']/@rdf:resource"/>
                                            <xsl:value-of select="$uriResolved"/>
                                        </dv:reference>
                                    </xsl:if>
                                    <dv:presentation>
                                        <xsl:value-of select="concat($WebApplicationBaseURL,'receive/',$objectID)"/>
                                    </dv:presentation>
                                </dv:links>
                            </mets:xmlData>
                        </mets:mdWrap>
                    </mets:digiprovMD>
                </mets:amdSec>
                <mets:dmdSec ID="dmd_{$objectID}">
                    <mets:mdWrap MDTYPE="MODS">
                        <mets:xmlData>
                            <xsl:apply-templates select="document(concat('xslTransform:mods:mcrobject:', $objectID))"/>
                        </mets:xmlData>
                    </mets:mdWrap>
                </mets:dmdSec>
                <xsl:apply-templates>
                    <xsl:with-param name="derID" select="$derID"/>
                </xsl:apply-templates>
            </xsl:copy>
        </xsl:variable>
        <xsl:apply-templates select="$result" mode="filter" />
    </xsl:template>

    <xsl:template match="@*|node()" mode="filter">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" mode="filter"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="mets:smLink" mode="filter">
        <xsl:variable name="logDiv" select="key('logDiv', @xlink:to)"/>
        <xsl:if test="count($logDiv)&gt;0">
            <xsl:copy-of select="." />
        </xsl:if>
    </xsl:template>


    <xsl:template match="mods:*[(@authority or @authorityURI) and @valueURI and string-length(text())=0]">
        <xsl:copy>
            <xsl:apply-templates select="@*" />

            <xsl:if test="mcrmods:is-supported(.)">
                <xsl:value-of select="mcrmods:to-mycoreclass(.,'single')/.//label[@xml:lang='de']/@text" />
            </xsl:if>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="mods:classification[not(contains('ddc|zdbs',@authority))]">
        <xsl:comment>removed invalid classifications</xsl:comment>
    </xsl:template>

    <xsl:template match="mods:mods">
        <xsl:copy>
            <xsl:apply-templates select="*|@*"/>
            <mods:recordInfo>
                <mods:recordIdentifier source="LeoPARD">
                    <xsl:value-of select="$objectID"/>
                </mods:recordIdentifier>
            </mods:recordInfo>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="mods:recordInfo">
        <xsl:comment>removed original recordInfo</xsl:comment>
    </xsl:template>

    <xsl:template match="mods:relatedItem">
        <xsl:variable name="relatedItemCount" select="number()"/>
        <xsl:copy>
            <xsl:apply-templates select="*[not(mods:recordInfo)]|@*"/>
            <mods:recordInfo>
                <xsl:variable name="parentID">
                    <xsl:choose>
                        <xsl:when test="@xlink:href">
                            <xsl:value-of select="@xlink:href"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="concat($objectID, '_related_', $relatedItemCount)"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <mods:recordIdentifier source="LeoPARD">
                    <xsl:value-of select="$parentID"/>
                </mods:recordIdentifier>
            </mods:recordInfo>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="mods:identifier[not(contains('purl|urn|isbn|issn|doi|handle|zdb|vd16|vd17|vd18', @type))]">
        <xsl:comment>removed invalid identifiers</xsl:comment>
    </xsl:template>

    <xsl:template match="mets:fileSec">
        <xsl:param name="derID"/>
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:variable name="imageFiles" select="mets:fileGrp[@USE='MASTER']/mets:file[ends-with(mets:FLocat/@xlink:href, '.jpg') or ends-with(mets:FLocat/@xlink:href, '.png') or ends-with(mets:FLocat/@xlink:href, '.tiff')]" />
            <xsl:variable name="altoFiles" select="mets:fileGrp[@USE='ALTO']/mets:file[ends-with(mets:FLocat/@xlink:href, '.xml') and starts-with(mets:FLocat/@xlink:href, 'alto/')]" />

            <mets:fileGrp USE="THUMBS">
                <xsl:for-each select="$imageFiles">
                    <xsl:variable name="id" select="@ID"/>
                    <xsl:variable name="url" select="mets:FLocat/@xlink:href"/>
                    <mets:file ID="{concat('THUMBS_', $id)}" MIMETYPE="image/jpeg">
                        <mets:FLocat xmlns:xlink="http://www.w3.org/1999/xlink" LOCTYPE="URL"
                                     xlink:href="{$WebApplicationBaseURL}api/iiif/image/v2/{$derID}%2f{encode-for-uri($url)}/full/!256,256/0/color.jpg"/>
                    </mets:file>
                </xsl:for-each>
            </mets:fileGrp>

            <mets:fileGrp USE="DEFAULT">
                <xsl:for-each select="$imageFiles">
                    <xsl:variable name="id" select="@ID"/>
                    <xsl:variable name="url" select="mets:FLocat/@xlink:href"/>
                    <mets:file ID="{concat('DEFAULT_', $id)}" MIMETYPE="image/jpeg">
                        <mets:FLocat xmlns:xlink="http://www.w3.org/1999/xlink" LOCTYPE="URL"
                                     xlink:href="{$WebApplicationBaseURL}api/iiif/image/v2/{$derID}%2f{encode-for-uri($url)}/full/full/0/color.jpg"/>
                    </mets:file>
                </xsl:for-each>
            </mets:fileGrp>

            <xsl:if test="count($altoFiles)&gt;0">
                <mets:fileGrp USE="FULLTEXT">
                    <xsl:for-each select="$altoFiles">

                        <xsl:variable name="id" select="@ID"/>
                        <xsl:variable name="url" select="mets:FLocat/@xlink:href"/>
                        <mets:file ID="{concat('FULLTEXT_', $id)}" MIMETYPE="text/xml">
                            <mets:FLocat xmlns:xlink="http://www.w3.org/1999/xlink" LOCTYPE="URL"
                                         xlink:href="{$WebApplicationBaseURL}servlets/MCRFileNodeServlet/{$derID}/{$url}"/>
                        </mets:file>
                    </xsl:for-each>
                </mets:fileGrp>
            </xsl:if>
        </xsl:copy>
    </xsl:template>

    <!-- insert all links to parents log -->
    <xsl:template match="mets:smLink">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
        <xsl:call-template name="traverseSmLink">
            <xsl:with-param name="from" select="@xlink:from"/>
            <xsl:with-param name="to" select="@xlink:to"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="traverseSmLink">
        <xsl:param name="from"/>
        <xsl:param name="to"/>
        <xsl:variable name="logDiv" select="key('logDiv', $from)/parent::mets:div/@ID"/>
        <xsl:if test="$logDiv">
            <mets:smLink xlink:from="{$logDiv}" xlink:to="{$to}"/>
            <xsl:call-template name="traverseSmLink">
                <xsl:with-param name="from" select="$logDiv"/>
                <xsl:with-param name="to" select="$to"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template match="mets:structMap[@TYPE='PHYSICAL']/mets:div[@TYPE='physSequence']/mets:div">
        <xsl:if test="count(mets:fptr)&gt;0">
            <xsl:variable name="result">
                <xsl:copy>
                    <xsl:copy-of select="@*"/>
                    <xsl:if test="not(@ORDER)">
                        <xsl:attribute name="ORDER">
                            <xsl:value-of select="count(preceding-sibling::mets:div)+1"/>
                        </xsl:attribute>
                    </xsl:if>

                    <xsl:apply-templates/>
                </xsl:copy>
            </xsl:variable>

            <xsl:if test="count($result/mets:div/mets:fptr)&gt;0">
                <xsl:copy-of select="$result"/>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <xsl:template match="mets:structMap[@TYPE='LOGICAL']/mets:div">
        <xsl:copy>
            <xsl:attribute name="DMDID">
                <xsl:value-of select="concat('dmd_', $objectID)"/>
            </xsl:attribute>
            <xsl:attribute name="ADMID">
                <xsl:value-of select="concat('amd_', $objectID)"/>
            </xsl:attribute>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="mets:mptr">
        <!-- remove em -->
    </xsl:template>

</xsl:stylesheet>
