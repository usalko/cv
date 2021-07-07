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

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;

import com.usalko.maven.plugin.usage.asm.UsageClassVisitor;
import com.usalko.maven.plugin.usage.asm.VisitorsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.objectweb.asm.ClassReader;

/**
 * Goal which search class usages.
 */
@Mojo(name = "class")
public class ClassMojo
        extends AbstractUsageMojo {

    /**
     * Class name for search from dependencies
     */
    @Parameter(property = "className", required = true)
    private String className;

    @Override
    protected void handleEntry(JarEntry jarEntry, byte[] jarEntryContent, Artifact artifact,
                               List<ArtifactEntry> entries) {
        ClassReader classReader = new ClassReader(jarEntryContent);
        VisitorsContext context = new VisitorsContext(className);
        classReader.accept(new UsageClassVisitor(context),
                EXPAND_FRAMES);
        context.getStatistics().forEach((clsName, countOfUsage) ->
            entries.add(new ArtifactEntry(artifact, clsName + ":" + countOfUsage))
        );
    }

    public void execute()
            throws MojoExecutionException {
        // Parse className
        // Walk for dependencies tree and get all references for this class
        // First line output is artifacts where found class
        // Next lines: artifact and class with import this class
        if (className == null) {
            throw new MojoExecutionException("Class name can't be empty");
        }

        try {
            List<ArtifactEntry> lookupResult = walkingDependencyTrees();

            getLog().info("{\"usage-class\": [" + lookupResult.stream().map(Object::toString)
                    .collect(Collectors.joining(",\n ")) + "]}");

        } catch (DependencyGraphBuilderException exception) {
            throw new MojoExecutionException("Cannot build project dependency graph for search class", exception);
        } catch (Exception exception) {
            throw new MojoExecutionException("Cannot search class in project dependency graph",
                    exception);
        }

    }

}
