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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Enumerations {

    private Enumerations() { }

    /**
     * Convert enumeration to stream
     * https://stackoverflow.com/questions/23261803/iterate-an-enumeration-in-java-8/23276455#23276455
     * @param <T> type
     * @param enumeration source enumeration
     * @return stream for the next pipeline
     */
    public static <T> Stream<T> asStream(Enumeration<T> enumeration) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            public T next() {
                                if (!enumeration.hasMoreElements()) {
                                    throw new NoSuchElementException();
                                }
                                return enumeration.nextElement();
                            }
                            public boolean hasNext() {
                                return enumeration.hasMoreElements();
                            }
                        },
                        Spliterator.ORDERED), false);
    }
}
