package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;

import java.util.regex.Pattern;

import static com.usalko.test.intproc.utils.Utils.NL;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */
public class CmdExit extends AbstractCommand {

    public CmdExit() {
        super(Pattern.compile("\\s*(EXIT|QUIT)\\s*", Pattern.CASE_INSENSITIVE),
                "Выход из приложения",
                "EXIT" + NL + "QUIT");
    }

    @Override
    public String exec(ExecContext execContext) {
        System.exit(0);
        return "";
    }

    @Override
    public String handleError(Throwable th) {
        return th.getLocalizedMessage();
    }
}
