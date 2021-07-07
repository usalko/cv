package com.usalko.test.intproc.storage;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

import com.usalko.test.intproc.IntegerRepository;

import java.util.*;
import java.util.function.Consumer;

/**
 * Реализация хранилища в памяти
 */
public class InMemoryIntegerRepository implements IntegerRepository {

    private CustomArrayList data;

    /**
     * Используем кастомизацию для включения удаления диапазона
     */
    private static class CustomArrayList extends ArrayList<Integer> {

        public CustomArrayList() {
        }

        public CustomArrayList(Collection<Integer> source) {
            super(source);
        }

        @Override
        public void removeRange(int fromIndex, int toIndex) {
            super.removeRange(fromIndex, toIndex);
        }
    }

    /**
     * Ленивая ининциализация
     * Повышает надежность применения
     * в случае инициализации класса
     * через рефлексию
     */
    private CustomArrayList data() {
        if (data == null) {
            data = new CustomArrayList();
        }
        return data;
    }

    @Override
    public void addInteger(int value) {
        data().add(value);
    }

    @Override
    public void removeIntegerByIndex(int index) {
        data().remove(index);
    }

    @Override
    public void updateInteger(int index, int value) {
        data().remove(index);
    }

    @Override
    public List<Integer> queryIndex(int value) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < data().size(); i++) {
            if (Objects.equals(data().get(i), value)) {
                result.add(i);
            }
        }
        return result;
    }

    @Override
    public void removeByIndex(int index) {
        data().remove(index);
    }

    @Override
    public void removeByIndexRange(int startIndex, int endIndex) {
        data().removeRange(startIndex, endIndex);
    }

    @Override
    public void unique() {
        data = new CustomArrayList(new LinkedHashSet<>(data()));
    }

    @Override
    public int count() {
        return data().size();
    }

    @Override
    public Iterator<Integer> iterator() {
        return data().iterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> consumer) {
        data().forEach(consumer);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return data().spliterator();
    }

    @Override
    public void process(Consumer<List<Integer>> dataProcessor) {
        if (dataProcessor == null) {
            return;
        }
        dataProcessor.accept(data());
    }

    @Override
    public Integer getInteger(int index) {
        return data().get(index);
    }

    @Override
    public void setInteger(int index, int value) {
        data().set(index, value);
    }
}
