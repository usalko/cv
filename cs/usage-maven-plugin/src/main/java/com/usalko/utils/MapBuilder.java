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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/*
 * Copyright August 2019 Ivan Usalko <ivict@usalko.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Билдер для создания карт.
 * @param <K> параметрический тип ключа.
 * @param <V> параметрический тип значения.
 * @param <T> тип карты.
 */
public class MapBuilder<K, V, T extends Map<K, V>> {

    private final T t;

    /**
     * Инициализация карты (не ленивая)
     * @param initializer лямбда инициализации, может быть использована для тюнинга.
     */
    private MapBuilder(Supplier<T> initializer) {
        this.t = initializer.get();
    }

    /**
     * Помещение елемента в карту.
     * @param key ключ элемента.
     * @param value значение элемента.
     * @return билдер в рамках которого проводится работа по наполнению карты.
     */
    public MapBuilder<K, V, T> put(K key, V value) {
        t.put(key, value);
        return this;
    }

    /**
     * Завершающий метод билдера.
     * @return ссылку на карту.
     */
    public T build() {
        return t;
    }

    /**
     * Возвращает простой json для карты
     * @return string with json format
     */
    public String toJson() {
        return JsonTools.toJson(t);
    }

    /**
     * Статический хелпер, для сокращения конструкции:
     * new MapBuilder&lt;&gt;((Supplier&lt;HashMap&lt;K, V&gt;&gt;) HashMap::new)
     * @param <K> keys type
     * @param <V> values type
     */
    public static<K, V> MapBuilder<K, V, HashMap<K, V>> hashMap() {
        return new MapBuilder<>(HashMap::new);
    }

}