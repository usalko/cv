package com.usalko.crpt.fazeco;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

/**
 * Общие проверки
 */
public interface GeneralChecks {

    /**
     * Выкидываем исключение если число отрицательное
     * @param number проверяемое число
     * @throws InvalidNumberException выкидываем в случае отрицательного значения аргумента
     */
    static void throwIfNegative(Number number) throws InvalidNumberException {
        if (NumberType.parse(number).isNegative()) {
            throw new InvalidNumberException(number);
        }
    }

}
