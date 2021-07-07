package com.usalko.gradle.plugin

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

import com.usalko.utils.toJson
import org.gradle.api.artifacts.ResolvedArtifact
import java.util.*

/**
 * Class describe entries in maven artifacts which satisfy to search conditions
 */
class ResolvedArtifactEntry(private val artifact: ResolvedArtifact, private val entry: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that = other as ResolvedArtifactEntry?
        return artifact == that!!.artifact && entry == that.entry
    }

    override fun hashCode(): Int {
        return Objects.hash(artifact, entry)
    }

    override fun toString(): String {
        return mapOf("artifact" to artifact, "entry" to entry).toJson()
    }
}
