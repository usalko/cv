package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;

import java.util.regex.Pattern;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdDel extends AbstractCommand {

    public CmdDel() {
        super(Pattern.compile("\\s*DEL\\s+(\\d+)\\s*", Pattern.CASE_INSENSITIVE),
                "Удалить из набора элемент с порядковым номером index (отсчет начинается с нуля)",
                "DEL 0");
    }

    @Override
    public String exec(ExecContext execContext) {
        String index = execContext.matcher().group(1);
        int indexValue = Integer.parseInt(index);
        int count = execContext.repository().count();
        if (indexValue < 0 || indexValue >= count) {
            throw new IllegalStateException("Индекс находится вне диапазона 0 " + count);
        }
        execContext.repository().removeByIndex(indexValue);
        return "Удален элемент по индексу: " + index;
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка при выполнении команды удаления: " + th.getLocalizedMessage();
    }
}
