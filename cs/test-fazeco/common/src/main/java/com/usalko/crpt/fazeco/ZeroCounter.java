package com.usalko.crpt.fazeco;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

/**
 * Общий интерфейс для всех реализаций алгоритма подсчета нулей факториала
 */
interface ZeroCounter<T extends Number> {
    /**
     * Метод инкапсулирующий вычисление количества нулей в факториале
     * @param number число для которого производится вычисление
     * @return количество нулей
     * @throws InvalidNumberException исключение инициируется в случае передачи невалидного аргумента
     * 1) числа которое не является целым
     * 2) числа которое не является положительным
     */
    Number count(T number) throws InvalidNumberException;
}
