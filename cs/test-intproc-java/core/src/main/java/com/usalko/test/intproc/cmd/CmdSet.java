package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;

import java.util.regex.Pattern;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdSet extends AbstractCommand {

    public CmdSet() {
        super(Pattern.compile("\\s*SET\\s+(\\d+)\\s+(\\d+)\\s*", Pattern.CASE_INSENSITIVE),
                "Установить значение в указанной позиции",
                "SET 0 1");
    }

    @Override
    public String exec(ExecContext execContext) {
        String index = execContext.matcher().group(1);
        String value = execContext.matcher().group(2);
        int integerIndex = Integer.parseInt(index);
        int integerValue = Integer.parseInt(value);
        if (integerIndex < 0 || integerIndex >= execContext.repository().count()) {
            throw new IllegalStateException("Индекс: " + integerIndex + " находится вне диапазона");
        }
        execContext.repository().setInteger(integerIndex, integerValue);
        return "Установлено значение " + integerValue +
                " для элемента с индексом " + integerIndex;
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка при выполнении команды установки значения: " + th.getLocalizedMessage();
    }
}
