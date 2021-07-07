package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;

import java.util.regex.Pattern;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdAdd extends AbstractCommand {

    public CmdAdd() {
        super(Pattern.compile("\\s*ADD\\s*([\\d\\s]+)\\s*", Pattern.CASE_INSENSITIVE),
                "Добавить значение в набор",
                "ADD 12");
    }

    @Override
    public String exec(ExecContext execContext) {
        String number = execContext.matcher().group(1);
        Integer value = Integer.parseInt(number.replaceAll("\\s+", ""));
        execContext.repository().addInteger(value);
        return "Добавлено значение: " + value;
    }

    @Override
    public String handleError(Throwable th) {
        return ": " + th.getLocalizedMessage();
    }
}
