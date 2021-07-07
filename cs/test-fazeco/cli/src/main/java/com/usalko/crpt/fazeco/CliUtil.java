package com.usalko.crpt.fazeco;

import com.usalko.crpt.fazeco.i18.Messages;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

/**
 * Набор утилит специфических для cli интерфейса
 */
public final class CliUtil {

    /**
     * Регулярное выражение для определения чисел
     */
    private static final Pattern IS_DECIMAL_NUMBER = Pattern.compile("([+-])?(\\d+)([\\.\\,]\\d*)?$");

    /**
     * Константа указывающая на семейство операционнох систем в которой запущена программа
     */
    private static final OS os = OS.parse(System.getProperty("os.name").toLowerCase());

    public enum OS {
        UNIX("nix", "nux", "aix"), MAC("mac"), WINDOWS("win"), SUNOS("sunos");

        private final String[] detector;
        OS(String... detector) {
            this.detector = detector;
        }

        /**
         * Определение операционки по ключевой подстроке
         * @param osName то что приходит в System#getProperty("os.name")
         * @return значение перечисления обозначающее семейство операционных систем
         */
        public static OS parse(String osName) {
            ArgumentChecks.throwIfNull(osName);
            return Arrays.stream(values())
                    .flatMap(v -> Arrays.stream(v.detector).map(s -> new AbstractMap.SimpleImmutableEntry<>(s, v)))
                    .filter(e -> osName.contains(e.getKey()))
                    .map(AbstractMap.SimpleImmutableEntry::getValue)
                    .findAny().orElseThrow(() -> new FazecoRuntimeException(Messages.CANT_PARSE_STRING_FOR_ENUM, osName, OS.class));
        }
    }

    /**
     * Возвращает текущее семейство операционных систем определенное из системного свойства: os.name
     * @return значение перечисления OS
     */
    public static OS os() {
        return os;
    }

    /**
     * Разбираем текст для вытаскивания числа
     * @param inputNumber текст для анализа
     * @return число или null если не удалось проанализировать
     */
    public static Number parseNumber(String inputNumber) {
        ArgumentChecks.throwIfNull(inputNumber);
        String decimalInput = inputNumber.replaceAll("\\s+", "");
        Matcher matcher = IS_DECIMAL_NUMBER.matcher(decimalInput);
        if (matcher.find()) {
            try {
                return parseDecimalNumber(notNull(matcher.group(1)) + matcher.group(2) + notNull(matcher.group(3)));
            } catch (RuntimeException e) {
                // Игнорируем исключение по контракту метода
                return null;
            }
        }
        return null;
    }

    /**
     * Возвращает пустую строку на значение null
     * @param value входная строка
     * @return пустая строка если null или исходная строка в противном случае
     */
    private static String notNull(String value) {
        return value == null ? "": value;
    }

    /**
     * Преобразовываем строку в число
     * @param decimalNumber на входе строка для преобразования возможно содержащая одну "."
     * @return Число в случае успеха
     * @throws NumberFormatException выкидывается при попытке распарсить невалидную строку
     */
    private static Number parseDecimalNumber(String decimalNumber) throws NumberFormatException {
        ArgumentChecks.throwIfNull(decimalNumber);
        String decimalInput = decimalNumber.replaceAll("\\s+", "");
        boolean hasSeparator = decimalInput.indexOf('.') >= 0;
        if (hasSeparator) {
            return reduceBigDecimal(new BigDecimal(decimalInput));
        } else {
            return reduceBigInteger(new BigInteger(decimalInput));
        }
    }

    /**
     * Пытаемся более точно распознать числовой тип для целых чисел
     * @param bigInteger большое целое число
     * @return число с типом для которого необходима меньшая разрядность представления или без изменений
     */
    private static Number reduceBigInteger(BigInteger bigInteger) {
        ArgumentChecks.throwIfNull(bigInteger);
        if (bigInteger.bitLength() > 64) {
            return bigInteger;
        }
        if (bigInteger.bitLength() > 32) {
            return bigInteger.longValue();
        }
        if (bigInteger.bitLength() > 16) {
            return bigInteger.intValue();
        }
        if (bigInteger.bitLength() > 8) {
            return bigInteger.shortValue();
        }
        return bigInteger.byteValue();
    }

    /**
     * Пытаемся более точно распознать числовой тип для вещественных чисел
     * @param bigDecimal большое вещественное число
     * @return число с типом для которого необходима меньшая разрядность представления или без изменений
     */
    private static Number reduceBigDecimal(BigDecimal bigDecimal) {
        ArgumentChecks.throwIfNull(bigDecimal);
        String textRepresentation = bigDecimal.toString();
        if (textRepresentation.length() - textRepresentation.indexOf('.') > 15) {
            return bigDecimal;
        }
        return bigDecimal.doubleValue();
    }
}
