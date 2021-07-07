package com.usalko.crpt.fazeco;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

import java.math.BigInteger;

/**
 * Реализация алгоритма описанного в
 * https://site.ada.edu.az/~medv/acm/Docs%20e-olimp/Volume%202/123.htm
 * В случае типов Big Integer
 */
public class ZeroCountBigInteger implements ZeroCounter<BigInteger> {

    /**
     * Факториал числа n равен произведению чисел от 1 до n. Ноль в конце произведения
     * появляется в результате перемножения 2 и 5. Но поскольку при разложении на простые
     * множители числа n! двоек больше чем пятерок, то количество нулей в конце n!
     * равно количеству пятерок в разложении n! на простые множители.
     *
     * В данной реализации мы используем механику больших чисел java
     *
     * @param number число для которого производится вычисление
     * @return BigNumber который возвращает количество нулей в значении факториала
     * @throws InvalidNumberException выбрасывается в случае отрицательного значения
     */
    @Override
    public Number count(BigInteger number) throws InvalidNumberException {
        GeneralChecks.throwIfNegative(number);
        BigInteger n = number;
        BigInteger result = BigInteger.ZERO;
        BigInteger five = BigInteger.valueOf(5L); // Не имеет смысла выносить в константу, поскольку <= 16 и следовательно работает кеш
        while(n.signum() == 1) { // Big integer > 0
            n = n.divide(five);
            result = result.add(n);
        }
        return result;
    }
}
