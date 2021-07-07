package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;

import java.util.regex.Pattern;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdDelRange extends AbstractCommand {

    public CmdDelRange() {
        super(Pattern.compile("\\s*DEL\\s+(\\d+)\\s+(\\d+)\\s*", Pattern.CASE_INSENSITIVE),
                "Удалить из набора элементы в указанном диапазоне",
                "DEL 1 2");
    }

    @Override
    public String exec(ExecContext execContext) {
        String fromIndex = execContext.matcher().group(1);
        String toIndex = execContext.matcher().group(2);
        Integer fromIndexValue = Integer.valueOf(fromIndex);
        Integer toIndexValue = Integer.valueOf(toIndex);
        if (fromIndexValue < 0 || fromIndexValue >= execContext.repository().count()) {
            throw new IllegalStateException("Левая граница индекса: " + fromIndex + " находится вне диапазона");
        }
        if (toIndexValue < 0 || toIndexValue >= execContext.repository().count()) {
            throw new IllegalStateException("Правая граница индекса: " + toIndex + " находится вне диапазона");
        }
        if (fromIndexValue > toIndexValue) {
            throw new IllegalStateException("Некорректно указан диапазон: " + fromIndex + toIndex);
        }
        execContext.repository().removeByIndexRange(fromIndexValue, toIndexValue + 1);
        return "Удален" + (toIndexValue - fromIndexValue > 0 ? "ы": "") +
                " элемент" + (toIndexValue - fromIndexValue > 0 ? "ы": "")
                + " в диапазоне: " + fromIndex + " " + toIndex;
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка при выполнении команды удаления: " + th.getLocalizedMessage();
    }
}
