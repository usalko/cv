package com.usalko.crpt.fazeco;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

/**
 * Реализация алгоритма описанного в
 * https://site.ada.edu.az/~medv/acm/Docs%20e-olimp/Volume%202/123.htm
 * В случае типов byte, short, integer
 */
public class ZeroCountInteger implements ZeroCounter<Integer> {

    /**
     * Факториал числа n равен произведению чисел от 1 до n. Ноль в конце произведения
     * появляется в результате перемножения 2 и 5. Но поскольку при разложении на простые
     * множители числа n! двоек больше чем пятерок, то количество нулей в конце n!
     * равно количеству пятерок в разложении n! на простые множители.
     *
     * @param number число для которого производится вычисление
     * @return Integer который возвращает количество нулей в значении факториала
     * @throws InvalidNumberException выбрасывается в случае отрицательного значения
     */
    @Override
    public Integer count(Integer number) throws InvalidNumberException {
        GeneralChecks.throwIfNegative(number);
        int n = number;
        int result = 0;
        while(n > 0) {
            n /= 5;
            result += n;
        }
        return result;
    }
}
