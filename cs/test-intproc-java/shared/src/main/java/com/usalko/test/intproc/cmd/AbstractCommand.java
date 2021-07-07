package com.usalko.test.intproc.cmd;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

import com.usalko.test.intproc.ExecContext;
import com.usalko.test.intproc.utils.Utils;

import java.util.regex.Pattern;

/**
 * Общие методы для всех классов комманд
 */
public abstract class AbstractCommand implements Command, HelpSupport {

    private final Pattern pattern;
    private final String description;
    private final String example;

    public AbstractCommand(Pattern pattern) {
        this(pattern, null, null);
    }

    public AbstractCommand(Pattern pattern, String description) {
        this(pattern, description, null);
    }

    public AbstractCommand(Pattern pattern, String description, String example) {
        Utils.throwIfNull(pattern, "Pattern can't be null");
        this.pattern = pattern;
        this.description = Utils.notNull(description);
        this.example = Utils.notNull(example);
    }

    @Override
    abstract public String exec(ExecContext execContext);

    @Override
    abstract public String handleError(Throwable th);

    @Override
    public Pattern pattern() {
        return pattern;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String example() {
        return example;
    }
}
