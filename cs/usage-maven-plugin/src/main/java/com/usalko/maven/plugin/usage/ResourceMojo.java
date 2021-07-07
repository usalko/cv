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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;

import java.util.List;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

/**
 * Goal which search resource usages.
 */
@Mojo(name = "resource")
public class ResourceMojo
        extends AbstractUsageMojo {
    /**
     * Resource name for search from dependencies
     */
    @Parameter(property = "resourceName", required = true)
    private String resourceName;

    public void execute()
            throws MojoExecutionException {
        // Parse resourceName
        // Walk for dependencies tree and get all references for this class
        // First line output is artifacts where found class
        // Next lines: artifact and class with import this class
        if (resourceName == null) {
            throw new MojoExecutionException("Resource name can't be empty");
        }

        try {
            List<ArtifactEntry> lookupResult = walkingDependencyTrees();

            getLog().info("{\"usage-resource\": [" + lookupResult.stream().map(Object::toString)
                    .collect(Collectors.joining(",\n ")) + "]}s");

        } catch (DependencyGraphBuilderException exception) {
            throw new MojoExecutionException("Cannot build project dependency graph for search resource", exception);
        } catch (Exception exception) {
            throw new MojoExecutionException("Cannot search resource in project dependency graph",
                    exception);
        }

    }

    @Override
    protected void handleEntry(JarEntry jarEntry, byte[] jarEntryContent, Artifact artifact,
                               List<ArtifactEntry> entries) {
        if (!jarEntry.getName().contains(resourceName)) {
            return;
        }
        entries.add(new ArtifactEntry(artifact, jarEntry.getName()));
    }
}
