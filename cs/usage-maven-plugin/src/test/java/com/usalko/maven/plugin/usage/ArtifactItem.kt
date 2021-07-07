package com.usalko.maven.plugin.usage

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.usalko.maven.plugin.usage.AbstractMojoTest.TestUtils.cleanToBeTokenizedString
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.ArtifactUtils
//import org.apache.maven.shared.transfer.dependencies.DependableCoordinate
//import org.apache.maven.shared.dependencies.DependableCoordinate
import org.codehaus.plexus.util.StringUtils
import java.io.File

class ArtifactItem
/**
 * @param artifact [Artifact]
 */(artifact: Artifact) { // : DependableCoordinate {
    /**
     * Group Id of Artifact
     *
     * @parameter
     * @required
     */
    private var groupId: String? = null

    /**
     * Name of Artifact
     *
     * @parameter
     * @required
     */
    private var artifactId: String? = null

    /**
     * Version of Artifact
     *
     * @parameter
     */
    private var version: String? = null

    /**
     * Type of Artifact (War,Jar,etc)
     *
     * @parameter
     * @required
     */
    private var type: String? = "jar"

    /**
     * Classifier for Artifact (tests,sources,etc)
     *
     * @parameter
     */
    private var classifier: String? = null

    /**
     * Location to use for this Artifact. Overrides default location.
     *
     * @parameter
     */
    private var outputDirectory: File? = null

    /**
     * Provides ability to change destination file name
     *
     * @parameter
     */
    private var destFileName: String? = null

    /**
     * Force Overwrite..this is the one to set in pom
     */
    private var overWrite: String? = null

    /**
     * Encoding of artifact. Overrides default encoding.
     *
     * @parameter
     */
    private var encoding: String? = null

    /**
     *
     */
    private var needsProcessing: Boolean = false

    /**
     * Artifact Item
     */
    private var artifact: Artifact? = null

    /**
     * A comma separated list of file patterns to include when unpacking the artifact.
     */
    private var includes: String? = null

    /**
     * A comma separated list of file patterns to exclude when unpacking the artifact.
     */
    private var excludes: String? = null

    init {
        this.setArtifact(artifact)
        this.setArtifactId(artifact.artifactId)
        this.setClassifier(artifact.classifier)
        this.setGroupId(artifact.groupId)
        this.setType(artifact.type)
        this.setVersion(artifact.version)
    }

    private fun filterEmptyString(inString: String): String? {
        return if ("" == inString) {
            null
        } else inString
    }

    /**
     * @return Returns the artifactId.
     */
    fun getArtifactId(): String? {
        return artifactId
    }

    /**
     * @param theArtifact The artifactId to set.
     */
    fun setArtifactId(theArtifact: String) {
        this.artifactId = filterEmptyString(theArtifact)
    }

    /**
     * @return Returns the groupId.
     */
    fun getGroupId(): String? {
        return groupId
    }

    /**
     * @param groupId The groupId to set.
     */
    fun setGroupId(groupId: String) {
        this.groupId = filterEmptyString(groupId)
    }

    /**
     * @return Returns the type.
     */
    fun getType(): String? {
        return type
    }

    /**
     * @param type The type to set.
     */
    fun setType(type: String) {
        this.type = filterEmptyString(type)
    }

    /**
     * @return Returns the version.
     */
    fun getVersion(): String? {
        return version
    }

    /**
     * @param version The version to set.
     */
    fun setVersion(version: String) {
        this.version = filterEmptyString(version)
    }

    /**
     * @return Returns the base version.
     */
    fun getBaseVersion(): String {
        return ArtifactUtils.toSnapshotVersion(version!!)
    }

    /**
     * @return Classifier.
     */
    fun getClassifier(): String? {
        return classifier
    }

    /**
     * @param classifier Classifier.
     */
    fun setClassifier(classifier: String) {
        this.classifier = filterEmptyString(classifier)
    }

    override fun toString(): String {
        return if (this.classifier == null) {
            groupId + ":" + artifactId + ":" + StringUtils.defaultString(version, "?") + ":" + type
        } else {
            (groupId + ":" + artifactId + ":" + classifier + ":" + StringUtils.defaultString(version, "?") + ":"
                    + type)
        }
    }

    /**
     * @return Returns the location.
     */
    fun getOutputDirectory(): File? {
        return outputDirectory
    }

    /**
     * @param outputDirectory The outputDirectory to set.
     */
    fun setOutputDirectory(outputDirectory: File) {
        this.outputDirectory = outputDirectory
    }

    /**
     * @return Returns the location.
     */
    fun getDestFileName(): String? {
        return destFileName
    }

    /**
     * @param destFileName The destFileName to set.
     */
    fun setDestFileName(destFileName: String) {
        this.destFileName = filterEmptyString(destFileName)
    }

    /**
     * @return Returns the needsProcessing.
     */
    fun isNeedsProcessing(): Boolean {
        return this.needsProcessing
    }

    /**
     * @param needsProcessing The needsProcessing to set.
     */
    fun setNeedsProcessing(needsProcessing: Boolean) {
        this.needsProcessing = needsProcessing
    }

    /**
     * @return Returns the overWriteSnapshots.
     */
    fun getOverWrite(): String? {
        return this.overWrite
    }

    /**
     * @param overWrite The overWrite to set.
     */
    fun setOverWrite(overWrite: String) {
        this.overWrite = overWrite
    }

    /**
     * @return Returns the encoding.
     * @since 3.0
     */
    fun getEncoding(): String? {
        return this.encoding
    }

    /**
     * @param encoding The encoding to set.
     * @since 3.0
     */
    fun setEncoding(encoding: String) {
        this.encoding = encoding
    }

    /**
     * @return Returns the artifact.
     */
    fun getArtifact(): Artifact? {
        return this.artifact
    }

    /**
     * @param artifact The artifact to set.
     */
    fun setArtifact(artifact: Artifact) {
        this.artifact = artifact
    }

    /**
     * @return Returns a comma separated list of excluded items
     */
    fun getExcludes(): String {
        return cleanToBeTokenizedString(this.excludes ?: "")
    }

    /**
     * @param excludes A comma separated list of items to exclude i.e. `*.xml, *.properties`
     */
    fun setExcludes(excludes: String) {
        this.excludes = excludes
    }

    /**
     * @return Returns a comma separated list of included items
     */
    fun getIncludes(): String {
        return cleanToBeTokenizedString(this.includes ?: "")
    }

    /**
     * @param includes A comma separated list of items to include i.e. `*.xml, *.properties`
     */
    fun setIncludes(includes: String) {
        this.includes = includes
    }

}