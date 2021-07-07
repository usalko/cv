package com.usalko.crpt.fazeco;

import com.usalko.crpt.fazeco.i18.Messages;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

/**
 * Класс предоставляющий заголовок для Cli
 */
class FazecoCliVersionProvider implements CommandLine.IVersionProvider {

    /**
     * Возвращает массив строк для заголовка консольного приложения
     * @return массив строк заголовка
     */
    @Override
    public String[] getVersion() {
        String headerInfo = Messages.resource("header.txt");
        if (headerInfo == null) {
            return new String[]{Messages.translate(Messages.APP_VERSION)};
        }
        List<String> result = Arrays.stream(headerInfo.split("\n")).collect(Collectors.toList());
        result.add(Messages.translate(Messages.APP_VERSION));
        return result.toArray(new String[0]);
    }
}
