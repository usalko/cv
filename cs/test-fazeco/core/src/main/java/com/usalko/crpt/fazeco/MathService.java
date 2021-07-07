package com.usalko.crpt.fazeco;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>,  2019
 */

import java.math.BigInteger;

/**
 * Сервис для математических операций
 */
public class MathService {

    /**
     * Статическая фабрика алгоритмов для вычисления количества нулей факториала
     * Фабрика используется для выбора алгоритмов которые используются по умолчанию
     * для различных типов чисел
     */
    private static class ZeroCountAlgorithmFactory {

        /**
         * Алгоритм по умолчанию для аргументов типа Integer
         * @param number аргумент для выбора
         * @return алгоритм определения нулей для факторила
         */
        static ZeroCounter<Integer> of(Integer number) {
            return new ZeroCountInteger();
        }

        /**
         * Алгоритм по умолчанию для аргументов типа Long
         * @param number аргумент для выбора
         * @return алгоритм определения нулей для факторила
         */
        static ZeroCounter<Long> of(Long number) {
            return new ZeroCountLong();
        }

        /**
         * Алгоритм по умолчанию для аргументов типа BigInteger
         * @param number аргумент для выбора
         * @return алгоритм определения нулей для факторила
         */
        static ZeroCounter<BigInteger> of(BigInteger number) {
            return new ZeroCountBigInteger();
        }
    }


    /**
     * Метод возвращает количество нулей в конце записи факториала числа "factorial".
     * @param factorial число для которого определен факториал
     * @return количество нулей в конце записи факториала
     * @throws InvalidNumberException выкидывается в случае числа для которого вычисление не представляется возможным
     */
    public Number zeroCountInFactorial(Number factorial) throws InvalidNumberException {
        switch (NumberType.parse(factorial)) {
            case POSITIVE_BYTE:
            case POSITIVE_SHORT:
            case POSITIVE_INTEGER:
                return ZeroCountAlgorithmFactory.of(factorial.intValue()).count(factorial.intValue());
            case POSITIVE_LONG:
                return ZeroCountAlgorithmFactory.of(factorial.longValue()).count(factorial.longValue());
            case POSITIVE_BIG_INTEGER:
                return ZeroCountAlgorithmFactory.of((BigInteger) factorial).count((BigInteger) factorial);
            default:
                throw new InvalidNumberException(factorial);
        }
    }
}
