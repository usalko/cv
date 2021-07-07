package com.usalko.crpt.fazeco;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

/**
 * Общий класс для всех интеграционных методов, плейсхолдер для методов
 * которые помогают в проведении тестов
 */
public abstract class IntegrationTest {

    /**
     * Запуск главного метода класса, использует рефлексию для доступа
     * @param mainClass класс для запуска
     * @param arguments параметры командной строки (происходит сплит по пробелам для передачи в список аргументов в метод main)
     * @return результат который печатается в System#out
     * @throws Exception выкидывается в случае если статический метод main не найден у класса или имеет неправильную сигнатуру
     */
    protected String exec(Class<?> mainClass, String arguments) throws Exception {
        PrintStream initial = System.out;
        try {
            final ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(capturedOut));
            Method mainMethod = mainClass.getDeclaredMethod("main", Array.newInstance(String.class, 0).getClass());
            mainMethod.invoke(null, new Object[]{arguments.split("\\s+")});
            return capturedOut.toString();
        } finally {
            System.setOut(initial);
        }
    }
}
