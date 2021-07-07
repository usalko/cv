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

import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.ArtifactUtils
import org.apache.maven.artifact.versioning.VersionRange
import org.apache.maven.plugin.testing.ArtifactStubFactory
import java.io.File
import java.io.IOException
import java.util.ArrayList

class UsageArtifactStubFactory(theWorkingDir: File, theCreateFiles: Boolean, private var flattenedPath: Boolean) : ArtifactStubFactory(theWorkingDir, theCreateFiles) {

    private fun getArtifactItem(artifact: Artifact): ArtifactItem {
        return ArtifactItem(artifact)
    }

    fun getArtifactItems(artifacts: Collection<Artifact>): List<ArtifactItem> {
        val list = ArrayList<ArtifactItem>()
        for (artifact in artifacts) {
            list.add(getArtifactItem(artifact))
        }
        return list
    }

    @Throws(IOException::class)
    override fun createArtifact(groupId: String, artifactId: String, versionRange: VersionRange, scope: String,
                                type: String, classifier: String, optional: Boolean): Artifact {
        val workingDir = workingDir

        if (!flattenedPath) {
            val path = StringBuilder(128)

            path.append(groupId.replace('.', '/')).append('/')

            path.append(artifactId).append('/')

            path.append(ArtifactUtils.toSnapshotVersion(versionRange.recommendedVersion.toString()))

            // don't use flatten directories, won't happen at runtime
            setWorkingDir(File(workingDir, path.toString()))
        }

        val artifact = super.createArtifact(groupId, artifactId, versionRange, scope, type, classifier, optional)

        setWorkingDir(workingDir)

        return artifact
    }

}