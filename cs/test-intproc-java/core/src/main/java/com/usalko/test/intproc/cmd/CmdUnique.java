package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;

import java.util.regex.Pattern;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdUnique extends AbstractCommand {

    public CmdUnique() {
        super(Pattern.compile("\\s*UNIQUE\\s*", Pattern.CASE_INSENSITIVE),
                "Удалить дубликаты в наборе",
                "UNIQUE");
    }

    @Override
    public String exec(ExecContext execContext) {
        int initialSize = execContext.repository().count();
        execContext.repository().unique();
        return "Произведено сжатие списка, количество элементов в списке (было -> стало): " + initialSize + " -> " + execContext.repository().count();
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка \"сжатия\" списка элементов (оставляем только уникальные значения): " + th.getLocalizedMessage();
    }
}
