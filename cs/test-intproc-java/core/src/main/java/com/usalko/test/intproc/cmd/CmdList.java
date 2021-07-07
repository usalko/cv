package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;
import com.usalko.test.intproc.utils.Utils;

import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.usalko.test.intproc.utils.Utils.NL;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdList extends AbstractCommand {

    public CmdList() {
        super(Pattern.compile("\\s*LIST(\\s+\\S+)?\\s*", Pattern.CASE_INSENSITIVE),
                "Выводит текущее содержимое набора на экран. Если разделитель не указан, используется символ табуляции.",
                "LIST" + NL +"LIST ; " + NL + "LIST ;\\s");
    }

    @Override
    public String exec(ExecContext execContext) {
        String separator = Utils.unescapedWithTrim(execContext.matcher().group(1));
        String separatorValue = separator.isEmpty() ? Utils.DEFAULT_SEPARATOR_VALUE : separator;

        return "Список элементов: " + StreamSupport.stream(execContext.repository().spliterator(),
                false).map(Objects::toString).collect(Collectors.joining(separatorValue));
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка вывода списка элементов из набора: " + th.getLocalizedMessage();
    }
}
