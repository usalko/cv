package com.usalko.crpt.fazeco;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

import com.usalko.crpt.fazeco.i18.Messages;

/**
 * Исключение инициируемое проверкой на то что число для которого проводятся вычисления
 * является целым и положительным.
 */
public class InvalidNumberException extends FazecoException {

    /**
     * Специализированный конструктор для исключения которое выкидывается в случа числа которое не может обработать программа
     * @param number число информация о котором выводится в сообщении
     */
    public InvalidNumberException(Number number) {
        super(Messages.INVALID_TYPE_NUMBER, NumberType.parse(number));
    }

}
