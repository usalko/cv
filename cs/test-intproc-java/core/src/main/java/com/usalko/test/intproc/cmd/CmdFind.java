package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdFind extends AbstractCommand {

    public CmdFind() {
        super(Pattern.compile("\\s*FIND(\\s+\\d+)?\\s*", Pattern.CASE_INSENSITIVE),
                "Найти значение в наборе;если найдено, выводит позицию",
                "FIND 12");
    }

    @Override
    public String exec(ExecContext execContext) {
        String value = execContext.matcher().group(1);
        int integerValue = Integer.parseInt(value.trim());
        AtomicInteger foundInIndex = new AtomicInteger(-1);
        Integer positionedValue = StreamSupport.stream(execContext.repository().spliterator(), false)
                .filter(element -> {
                    foundInIndex.incrementAndGet();
                    return Objects.equals(element, integerValue);
                }).findAny().orElse(null);
        if (positionedValue != null) {
            return "Значение " + integerValue + " найдено в позиции " + foundInIndex.get();
        }
        return "Значение " + integerValue + " не найдено";
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка при выполнении комманды поиска: " + th.getLocalizedMessage();
    }
}
