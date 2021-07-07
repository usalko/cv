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

class ResourceMojoTest : AbstractMojoTest() {

    /** {@inheritDoc}  */
    @Throws(Exception::class)
    override fun setUp() {
        // required
        super.setUp()
    }

    /** {@inheritDoc}  */
    @Throws(Exception::class)
    override fun tearDown() {
        // required
        super.tearDown()
    }

    /**
     * Thanks for https://stackoverflow.com/questions/44009232/nosuchelementexception-thrown-while-testing-maven-plugin
     * @throws Exception if any
     */
    @Throws(Exception::class)
    fun testClass() {
        val pomForTests = configureExecutionSession("src/test/resources/unit/resource-test/pom.xml",
                // What we are test
                "com.usalko", "1.0-SNAPSHOT", "usage-maven-plugin", "resource",
                ResourceMojo::class.java)
        val resourceMojo = lookupMojo("resource", pomForTests) as ResourceMojo

        customPlexusResolver(resourceMojo, pluginConfiguration, project, session)

        assertNotNull(resourceMojo)

        resourceMojo.execute()
    }

}