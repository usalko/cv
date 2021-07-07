package com.usalko.gradle.plugin.asm

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

import java.util.HashMap

class VisitorsContext(
        /**
         * Input parameter
         */
        private val className: String?) {

    // Current class cursor
    private var currentClassName: String? = null
    private val statistics: MutableMap<String, Int>
    private val searchString: String

    init {
        this.statistics = HashMap()
        this.searchString = className!!.replace('.', '/')
    }

    internal fun acceptClass(candidate: String?) {
        if (className == null) {
            return
        }
        if (candidate == null) {
            return
        }
        if (candidate.contains(searchString)) {
            statistics.putIfAbsent(currentClassName!!, 0)
            statistics[currentClassName!!] = statistics[currentClassName!!]!!.plus(1)
        }
    }

    internal fun setCurrentClassName(name: String) {
        currentClassName = name
    }

    fun getStatistics(): Map<String, Int> {
        return statistics
    }
}
