package com.usalko.crpt.fazeco;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>,  2019
 */

import com.usalko.crpt.fazeco.i18.Messages;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * Перечисление, для типов чисел
 * Используется для нивелирования side-effect от конструкции instance of
 */
public enum NumberType {
    POSITIVE_INTEGER,
    POSITIVE_LONG,
    POSITIVE_BIG_INTEGER,
    POSITIVE_DOUBLE,
    POSITIVE_FLOAT,
    POSITIVE_BIG_DECIMAL,
    POSITIVE_SHORT,
    POSITIVE_BYTE,
    NEGATIVE_INTEGER(true),
    NEGATIVE_LONG(true),
    NEGATIVE_BIG_INTEGER(true),
    NEGATIVE_DOUBLE(true),
    NEGATIVE_FLOAT(true),
    NEGATIVE_BIG_DECIMAL(true),
    NEGATIVE_SHORT(true),
    NEGATIVE_BYTE(true)
    ;

    /**
     * Признак отрицательных значений
     */
    private final boolean isNegative;

    NumberType(boolean isNegative) {
        this.isNegative = isNegative;
    }

    NumberType() {
        this(false);
    }

    public boolean isNegative() {
        return isNegative;
    }

    private static class ClassEntry<T extends Number> {
        private final Class<T> type;
        private final boolean isNegative;

        private ClassEntry(Class<T> type, boolean isNegative) {
            this.type = type;
            this.isNegative = isNegative;
        }

        private ClassEntry(Class<T> type, NumberType numberType) {
            this(type, numberType.isNegative());
        }

        /**
         * Статический метод фабрика ключей
         * @param number число для которого генерируется ключ
         * @param <T> тип числа
         * @return ключ по которому будет производится поиск в индексе
         */
        @SuppressWarnings("unchecked")
        public static<T extends Number> ClassEntry<T> of(T number) {
            if (number == null) {
                throw new FazecoRuntimeException(Messages.NUMBER_CANT_BE_NULL);
            }
            if (number instanceof BigInteger) {
                return new ClassEntry<>((Class<T>) number.getClass(), ((BigInteger) number).signum() < 0);
            }
            return new ClassEntry<>((Class<T>) number.getClass(), number.longValue() < 0);
        }

        public Class<T> getType() {
            return type;
        }

        public boolean isNegative() {
            return isNegative;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ClassEntry<?> that = (ClassEntry<?>) o;
            return isNegative == that.isNegative &&
                    Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, isNegative);
        }
    }

    /**
     * Кешируемый индекс для определения типа числа
     */
    private static final Map<ClassEntry, NumberType> index;
    static {
        index = Arrays.stream(values()).collect(toMap(type -> {
            switch (type) {
                case POSITIVE_INTEGER:
                    return new ClassEntry<>(Integer.class, type);
                case POSITIVE_LONG:
                    return new ClassEntry<>(Long.class, type);
                case POSITIVE_BIG_INTEGER:
                    return new ClassEntry<>(BigInteger.class, type);
                case POSITIVE_DOUBLE:
                    return new ClassEntry<>(Double.class, type);
                case POSITIVE_FLOAT:
                    return new ClassEntry<>(Float.class, type);
                case POSITIVE_BIG_DECIMAL:
                    return new ClassEntry<>(BigDecimal.class, type);
                case POSITIVE_SHORT:
                    return new ClassEntry<>(Short.class, type);
                case POSITIVE_BYTE:
                    return new ClassEntry<>(Byte.class, type);
                case NEGATIVE_INTEGER:
                    return new ClassEntry<>(Integer.class, type);
                case NEGATIVE_LONG:
                    return new ClassEntry<>(Long.class, type);
                case NEGATIVE_BIG_INTEGER:
                    return new ClassEntry<>(BigInteger.class, type);
                case NEGATIVE_FLOAT:
                    return new ClassEntry<>(Float.class, type);
                case NEGATIVE_DOUBLE:
                    return new ClassEntry<>(Double.class, type);
                case NEGATIVE_BIG_DECIMAL:
                    return new ClassEntry<>(BigDecimal.class, type);
                case NEGATIVE_SHORT:
                    return new ClassEntry<>(Short.class, type);
                case NEGATIVE_BYTE:
                    return new ClassEntry<>(Byte.class, type);
                default:
                    throw new FazecoRuntimeException(Messages.NOT_IMPLEMENTED_CASE, type);
            }
        }, identity()));
    }


    /**
     * Определитель типа числа
     * @param number число тип которого необходимо определить
     * @return тип числа который может быть обработан программой
     */
    public static NumberType parse(Number number) {
        if (number == null) {
            throw new FazecoRuntimeException(Messages.NUMBER_CANT_BE_NULL);
        }
        return index.get(ClassEntry.of(number));
    }
}
