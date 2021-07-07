package com.usalko.test.intproc;

import com.usalko.test.intproc.cmd.AbstractCommand;

import java.util.regex.Pattern;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */
public class SimpleCommand extends AbstractCommand {

    public SimpleCommand() {
        super(Pattern.compile("SIMPLE-COMMAND"), "Комманда для тестов", "SIMPLE-COMMAND");
    }

    @Override
    public String exec(ExecContext execContext) {
        return "SIMPLE-COMMAND";
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка выполнения: " + th.getLocalizedMessage();
    }
}
