package com.usalko.utils;

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

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper class for some json stuff
 */
class JsonTools { // NOSONAR

    private JsonTools() { }

    static String toJson(Map<?, ?> map) { // NOSONAR
        if (map == null) {
            return "null";
        }
        return "{" +
                map.entrySet().stream()
                        .map(e -> jsonEntry(e.getKey(), e.getValue()))
                        .collect(Collectors.joining(",")) +
                "}";
    }

    private static String jsonEntry(Object key, Object value) {
        return jsonString(key) + ":" + jsonObject(value);
    }

    private static String jsonString(Object value) {
        return "\"" + value.toString() + "\"";
    }

    private static String jsonObject(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof Map) {
            return toJson((Map) value);
        }
        return "\"" + value.toString().replace("\"", "\\\"") + "\"";
    }

}
