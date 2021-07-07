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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.usalko.utils.Enumerations;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.shared.artifact.filter.StrictPatternExcludesArtifactFilter;
import org.apache.maven.shared.artifact.filter.StrictPatternIncludesArtifactFilter;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.filter.AncestorOrSelfDependencyNodeFilter;
import org.apache.maven.shared.dependency.graph.filter.AndDependencyNodeFilter;
import org.apache.maven.shared.dependency.graph.filter.ArtifactDependencyNodeFilter;
import org.apache.maven.shared.dependency.graph.filter.DependencyNodeFilter;
import org.apache.maven.shared.dependency.graph.traversal.BuildingDependencyNodeVisitor;
import org.apache.maven.shared.dependency.graph.traversal.CollectingDependencyNodeVisitor;
import org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor;
import org.apache.maven.shared.dependency.graph.traversal.FilteringDependencyNodeVisitor;

import static com.usalko.maven.plugin.usage.UsageMojoException.TypicalReason.CLASS_ENTRY_ERROR;
import static com.usalko.maven.plugin.usage.UsageMojoException.TypicalReason.GENERAL_IO_EXCEPTION;

abstract class AbstractUsageMojo extends AbstractMojo {

    /**
     * POM
     */
    @Parameter( defaultValue = "${project}", readonly = true, required = true )
    MavenProject project;

    /**
     * The Maven session
     */
    @Parameter( defaultValue = "${session}", readonly = true, required = true )
    MavenSession session;

    /**
     * Contains the full list of projects in the reactor.
     */
    @Parameter( defaultValue = "${reactorProjects}", readonly = true, required = true )
    List<MavenProject> reactorProjects;

    /**
     * The scope to filter by when resolving the dependency tree, or <code>null</code>
     * to include dependencies from all
     * scopes.
     */
    @Parameter( property = "scope" )
    String scope;

    /**
     * The computed dependency tree root node of the Maven project.
     */
    DependencyNode rootNode;

    /**
     * A comma-separated list of artifacts to filter the serialized dependency tree by, or <code>null</code> not to
     * filter the dependency tree. The filter syntax is:
     *
     * <pre>
     * [groupId]:[artifactId]:[type]:[version]
     * </pre>
     *
     * where each pattern segment is optional and supports full and partial <code>*</code> wildcards. An empty pattern
     * segment is treated as an implicit wildcard.
     * <p>
     * For example, <code>org.apache.*</code> will match all artifacts whose group id starts with
     * <code>org.apache.</code>, and <code>:::*-SNAPSHOT</code> will match all snapshot artifacts.
     * </p>
     *
     * @see StrictPatternIncludesArtifactFilter
     */
    @Parameter( property = "includes" )
    String includes;

    /**
     * A comma-separated list of artifacts to filter from the serialized dependency tree, or <code>null</code> not to
     * filter any artifacts from the dependency tree. The filter syntax is:
     *
     * <pre>
     * [groupId]:[artifactId]:[type]:[version]
     * </pre>
     *
     * where each pattern segment is optional and supports full and partial <code>*</code> wildcards. An empty pattern
     * segment is treated as an implicit wildcard.
     * <p>
     * For example, <code>org.apache.*</code> will match all artifacts whose group id starts with
     * <code>org.apache.</code>, and <code>:::*-SNAPSHOT</code> will match all snapshot artifacts.
     * </p>
     *
     * @see StrictPatternExcludesArtifactFilter
     */
    @Parameter( property = "excludes" )
    String excludes;
    /**
     * The dependency tree builder to use.
     */
    @Component(hint = "default")
    private DependencyGraphBuilder dependencyGraphBuilder;

    public DependencyNode getRootNode() {
        return rootNode;
    }

    public MavenProject getProject() {
        return project;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public MavenSession getSession() {
        return session;
    }

    public void setSession(MavenSession session) {
        this.session = session;
    }

    /**
     * Lookup class in dependency tree and collect artifact-entries
     *
     * @param theRootNode the dependency tree root node to serialize
     * @param artifactEntries the entries which found
     */
    protected void lookupArtifactsEntriesDependencyTree(DependencyNode theRootNode,
                                                        List<ArtifactEntry> artifactEntries) {

        DependencyNodeVisitor visitor = getLookupArtifactEntriesDependencyNodeVisitor(artifactEntries);

        visitor = new BuildingDependencyNodeVisitor(visitor);

        DependencyNodeFilter filter = createDependencyNodeFilter();

        if (filter != null) {
            CollectingDependencyNodeVisitor collectingVisitor = new CollectingDependencyNodeVisitor();
            DependencyNodeVisitor firstPassVisitor = new FilteringDependencyNodeVisitor(
                    collectingVisitor, filter);
            theRootNode.accept(firstPassVisitor);

            DependencyNodeFilter secondPassFilter =
                    new AncestorOrSelfDependencyNodeFilter(collectingVisitor.getNodes());
            visitor = new FilteringDependencyNodeVisitor(visitor, secondPassFilter);
        }

        theRootNode.accept(visitor);
    }

    /**
     * Gets the dependency node filter to use when serializing the dependency graph.
     *
     * @return the dependency node filter, or <code>null</code> if none required
     */
    private DependencyNodeFilter createDependencyNodeFilter()
    {
        List<DependencyNodeFilter> filters = new ArrayList<>();

        // filter includes
        if ( includes != null )
        {
            List<String> patterns = Arrays.asList( includes.split( "," ) );

            getLog().debug( "+ Filtering dependency tree by artifact include patterns: " + patterns );

            ArtifactFilter artifactFilter = new StrictPatternIncludesArtifactFilter( patterns );
            filters.add( new ArtifactDependencyNodeFilter( artifactFilter ) );
        }

        // filter excludes
        if ( excludes != null )
        {
            List<String> patterns = Arrays.asList( excludes.split( "," ) );

            getLog().debug( "+ Filtering dependency tree by artifact exclude patterns: " + patterns );

            ArtifactFilter artifactFilter = new StrictPatternExcludesArtifactFilter( patterns );
            filters.add( new ArtifactDependencyNodeFilter( artifactFilter ) );
        }

        return filters.isEmpty() ? null : new AndDependencyNodeFilter( filters );
    }

    /**
     * @return {@link DependencyNodeVisitor}
     */
    private DependencyNodeVisitor getLookupArtifactEntriesDependencyNodeVisitor(List<ArtifactEntry> entries) {
        return new DependencyNodeVisitor() {
            public boolean visit(DependencyNode dependencyNode) {
                try {
                    lookupEntriesInArtifact(dependencyNode.getArtifact(), entries);
                } catch (IOException e) {
                    throw new UsageMojoException(GENERAL_IO_EXCEPTION, e);
                }
                return true;
            }

            public boolean endVisit(DependencyNode dependencyNode) {
                return true;
            }
        };
    }

    private void lookupEntriesInArtifact(Artifact artifact, List<ArtifactEntry> entries)
            throws IOException {
        if (isJarFile(artifact.getFile())) {
            lookupEntriesInJar(artifact, entries);
        }
    }

    private void lookupEntriesInJar(Artifact artifact, List<ArtifactEntry> entries)
            throws IOException {
        try (JarFile jarFile = new JarFile(artifact.getFile())) {
            Enumerations.asStream(jarFile.entries()).forEach(entry -> {
                if (entry.isDirectory()) {
                    return;
                }
                if (!entry.getName().endsWith(".class")) {
                    return;
                }
                try (InputStream inputStream = jarFile.getInputStream(entry)) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    byte[] buffer = new byte[0xFFFF];
                    for (int len; (len = inputStream.read(buffer)) != -1;) {
                        os.write(buffer, 0, len);
                    }
                    os.flush();
                    handleEntry(entry, os.toByteArray(), artifact, entries);
                } catch (IOException e) {
                    throw new UsageMojoException(CLASS_ENTRY_ERROR, e);
                }
            });
        }
    }

    /**
     * Gets the artifact filter to use when resolving the dependency tree.
     *
     * @return the artifact filter
     */
    protected ArtifactFilter createResolvingArtifactFilter() {
        ArtifactFilter filter;

        // filter scope
        if (scope != null) {
            getLog().debug("+ Resolving dependency tree for scope '" + scope + "'");

            filter = new ScopeArtifactFilter(scope);
        } else {
            filter = null;
        }

        return filter;
    }

    protected abstract void handleEntry(JarEntry jarEntry, byte[] jarEntryContent, Artifact artifact,
                                        List<ArtifactEntry> entries);

    private boolean isJarFile(File file) {
        return file != null && file.getName().endsWith(".jar");
    }

    protected List<ArtifactEntry> walkingDependencyTrees() throws DependencyGraphBuilderException {
        ArtifactFilter artifactFilter = createResolvingArtifactFilter();

        ProjectBuildingRequest buildingRequest =
                new DefaultProjectBuildingRequest( session.getProjectBuildingRequest() );
        buildingRequest.setProject(project);

        // non-verbose mode use dependency graph component, which gives consistent results
        // with Maven version running
        rootNode = dependencyGraphBuilder
                .buildDependencyGraph(buildingRequest, artifactFilter);

        List<ArtifactEntry> lookupResult = new ArrayList<>();

        lookupArtifactsEntriesDependencyTree(rootNode, lookupResult);
        return lookupResult;
    }
}
