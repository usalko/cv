package com.usalko.crpt.fazeco;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

import picocli.CommandLine;

import java.util.Locale;

/**
 * Набор параметров из командной строки
 */
class CliParams {

    @CommandLine.Parameters(index = "0", arity = "0..1", descriptionKey = "cli.parameter.0")
    private String inputNumber;

    @CommandLine.Option(names = { "-l", "--locale" }, descriptionKey = "cli.locale")
    private String locale = "en_US";

    @CommandLine.Option(names = { "-c", "--command-mode" }, descriptionKey = "cli.command-mode")
    private boolean commandMode = false;

    @CommandLine.Option(names = { "-h", "--help" })
    private boolean printHelp;

    /**
     * Разбор аргумента locale
     * В случае неудачи возвращает системную локаль
     * @return локаль распознанную из поля #locale или локаль по умолчанию @see Locale#getDefault()
     */
    public Locale locale() {
        String[] parsedLocale = locale.split("_");
        if (parsedLocale.length > 1) {
            return new Locale(parsedLocale[0], parsedLocale[1]);
        }
        return Locale.getDefault();
    }

    /**
     * Возвращает признак выключения интерактивного режима для запуска приложения
     * @return true если приложение запущено только для вычисления из входного потока и возврата
     */
    public boolean commandMode() {
        return commandMode;
    }

    /**
     * Число которое передается во входной поток
     * @return число из входного потока
     */
    public Number inputNumber() {
        if (inputNumber == null) {
            return null;
        }
        return CliUtil.parseNumber(inputNumber);
    }

    /**
     * Только напечатать экран помощи и выйти
     * @return true если только печатем экран помощи
     */
    public boolean isPrintHelp() {
        return printHelp;
    }
}
