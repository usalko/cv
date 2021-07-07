package com.usalko.utils

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

import java.util.stream.Collectors

/**
 * Helper class for some json stuff
 */
internal object JsonTools { // NOSONAR

    fun toJson(map: Map<*, *>?): String { // NOSONAR
        return if (map == null) {
            "null"
        } else "{" +
                map.entries.stream()
                        .map { e -> jsonEntry(e.key!!, e.value!!) }
                        .collect(Collectors.joining(",")) +
                "}"
    }

    private fun jsonEntry(key: Any, value: Any): String {
        return jsonString(key) + ":" + jsonObject(value)
    }

    fun jsonString(value: Any): String {
        return "\"" + value.toString().replace("\"", "\\\"") + "\""
    }

    private fun jsonObject(value: Any?): String {
        if (value == null) {
            return "null"
        }
        if (value is Number) {
            return value.toString()
        }
        return if (value is Map<*, *>) {
            toJson(value as Map<*, *>?)
        } else "\"" + value.toString().replace("\"", "\\\"") + "\""
    }

}

fun <K, V> Map<K, V>.toJson(): String {
    return JsonTools.toJson(this)
}

