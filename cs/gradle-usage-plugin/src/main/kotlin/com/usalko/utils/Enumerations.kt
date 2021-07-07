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

import java.util.*
import java.util.stream.Stream
import java.util.stream.StreamSupport

object Enumerations {

    /**
     * Convert enumeration to stream
     * https://stackoverflow.com/questions/23261803/iterate-an-enumeration-in-java-8/23276455#23276455
     * @param <T> type
     * @param enumeration source enumeration
     * @return stream for the next pipeline
    </T> */
    fun <T> asStream(enumeration: Enumeration<T>): Stream<T> {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        object : Iterator<T> {
                            override fun next(): T {
                                if (!enumeration.hasMoreElements()) {
                                    throw NoSuchElementException()
                                }
                                return enumeration.nextElement()
                            }

                            override fun hasNext(): Boolean {
                                return enumeration.hasMoreElements()
                            }
                        },
                        Spliterator.ORDERED), false)
    }
}
