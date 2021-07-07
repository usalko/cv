package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdClear extends AbstractCommand {

    public CmdClear() {
        super(Pattern.compile("\\s*CLEAR\\s*", Pattern.CASE_INSENSITIVE),
                "Удалить все из набора",
                "CLEAR");
    }

    @Override
    public String exec(ExecContext execContext) {
        execContext.repository().process(List::clear);
        return "Удалены все элементы из набора";
    }

    @Override
    public String handleError(Throwable th) {
        return ": " + th.getLocalizedMessage();
    }
}
