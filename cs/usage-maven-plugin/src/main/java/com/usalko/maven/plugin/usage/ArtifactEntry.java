package com.usalko.maven.plugin.usage;

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

import com.usalko.utils.MapBuilder;
import java.util.Objects;
import org.apache.maven.artifact.Artifact;

/**
 * Class describe entries in maven artifacts which satisfy to search conditions
 */
public class ArtifactEntry {

    private final Artifact artifact;

    private final String entry;

    public ArtifactEntry(Artifact artifact, String entry) {
        this.artifact = artifact;
        this.entry = entry;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public String getEntry() {
        return entry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArtifactEntry that = (ArtifactEntry) o;
        return Objects.equals(artifact, that.artifact) &&
                Objects.equals(entry, that.entry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifact, entry);
    }

    @Override
    public String toString() {
        return MapBuilder.hashMap()
                .put("entry", entry)
                .put("artifact", artifact)
                .toJson();
    }
}
