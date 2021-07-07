package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;

import java.util.regex.Pattern;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdGet extends AbstractCommand {

    public CmdGet() {
        super(Pattern.compile("\\s*GET(\\s+\\d+)?\\s*", Pattern.CASE_INSENSITIVE),
                "Прочитать значение в указанной позиции",
                "GET 0");
    }

    @Override
    public String exec(ExecContext execContext) {
        String index = execContext.matcher().group(1);
        int indexValue = Integer.parseInt(index.trim());
        int count = execContext.repository().count();
        if (indexValue < 0 || indexValue >= count) {
            throw new IllegalStateException("Индекс находится вне диапазона 0 " + count);
        }
        return "Элемент по идексу " + indexValue + ": " + execContext.repository().getInteger(indexValue);
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка при выполнении комманды получения значения по индексу: " + th.getLocalizedMessage();
    }
}
