package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;

import java.util.Collections;
import java.util.regex.Pattern;

import static com.usalko.test.intproc.utils.Utils.NL;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdSort extends AbstractCommand {

    public CmdSort() {
        super(Pattern.compile("\\s*SORT\\s*(\\s+(ASC|DESC)\\s*)?", Pattern.CASE_INSENSITIVE),
                "Отсортировать числа по возрастанию (asc) или по убыванию (desc). Если параметр не указан, сортировать по возрастанию.",
                "SORT" + NL + "SORT desc");
    }

    @Override
    public String exec(ExecContext execContext) {
        String direction = execContext.matcher().group(1);
        if (direction != null && "DESC".equals(direction.trim().toUpperCase())) {
            execContext.repository().process((data) -> {
                data.sort(Collections.reverseOrder());
            });
            return "Числа отсортированны по убыванию";
        }
        execContext.repository().process(Collections::sort);
        return "Числа отсортированны по возрастанию";
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка сортировки списка элементов: " + th.getLocalizedMessage();
    }
}
