package com.usalko.test.intproc.utils;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

/**
 * Статические библиотечные методы
 */
public final class Utils {

    public static final String NL = System.getProperty("line.separator");
    public static final String DEFAULT_SEPARATOR_VALUE = "\t";

    private Utils() {}


    public static void throwIfNull(Object argument, String message) {
        if (argument != null) {
            return;
        }
        throw new IllegalArgumentException(message);
    }

    public static String notNull(String text) {
        if (text != null) {
            return text;
        }
        return "";
    }

    /**
     * Заменяет спец символы, проводит триминг
     * @param input входная строка может содержать спец символы \s \t \\
     * @return строка которая содержит замененные символы, пустая строка если на входе null
     */
    public static String unescapedWithTrim(String input) {
        if (input == null) {
            return "";
        }
        return input.trim()
                .replaceAll("\\\\t", "\t")
                .replaceAll("\\\\s", " ")
                .replace("\\\\", "\\");
    }
}
