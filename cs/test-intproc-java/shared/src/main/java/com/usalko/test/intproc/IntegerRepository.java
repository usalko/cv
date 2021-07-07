package com.usalko.test.intproc;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

import java.util.List;
import java.util.function.Consumer;

/**
 * Инкапсуляция реального хранилища
 * Паттерн Repository
 */
public interface IntegerRepository extends Iterable<Integer> {

    void addInteger(int value);

    void removeIntegerByIndex(int index);

    void updateInteger(int index, int value);

    List<Integer> queryIndex(int value);

    void removeByIndex(int index);

    void removeByIndexRange(int startIndex, int endIndex);

    void unique();

    int count();

    void process(Consumer<List<Integer>> dataProcessor);

    Integer getInteger(int index);

    void setInteger(int index, int value);
}
