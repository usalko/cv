package com.usalko.maven.plugin.usage.asm;

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

import java.util.HashMap;
import java.util.Map;

public class VisitorsContext {

    /**
     * Input parameter
     */
    private final String className;

    // Current class cursor
    private String currentClassName;
    private Map<String, Integer> statistics;
    private String searchString;

    public VisitorsContext(String className) {
        this.className = className;
        this.statistics = new HashMap<>();
        this.searchString = className.replace('.', '/');
    }

    public String getClassName() {
        return className;
    }

    void acceptClass(String candidate) {
        if (className == null) {
            return;
        }
        if (candidate == null) {
            return;
        }
        if (candidate.contains(searchString)) {
            statistics.putIfAbsent(currentClassName, 0);
            statistics.put(currentClassName, statistics.get(currentClassName) + 1);
        }
    }

    void setCurrentClassName(String name) {
        currentClassName = name;
    }

    public Map<String, Integer> getStatistics() {
        return statistics;
    }
}
