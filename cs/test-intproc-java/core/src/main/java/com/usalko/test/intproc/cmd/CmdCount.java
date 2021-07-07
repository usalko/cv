package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;

import java.util.regex.Pattern;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdCount extends AbstractCommand {

    public CmdCount() {
        super(Pattern.compile("\\s*COUNT\\s*", Pattern.CASE_INSENSITIVE),
                "Отобразить количество чисел в наборе",
                "COUNT");
    }

    @Override
    public String exec(ExecContext execContext) {
        return "Чисел в наборе: " + execContext.repository().count();
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка определения количества чисел в наборе: " + th.getLocalizedMessage();
    }
}
