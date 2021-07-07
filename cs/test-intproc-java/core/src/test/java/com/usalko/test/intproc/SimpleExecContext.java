package com.usalko.test.intproc;

import com.usalko.test.intproc.cmd.Command;

import java.util.regex.Matcher;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */
public class SimpleExecContext implements ExecContext {

    private final Command command;
    private final Matcher matcher;
    private final IntegerRepository repository;
    private final CommandRegistry registry;

    private SimpleExecContext(Command command, Matcher matcher,
                              CommandRegistry registry,
                              IntegerRepository repository) {
        this.command = command;
        this.matcher = matcher;
        this.registry = registry;
        this.repository= repository;
    }

    @Override
    public Matcher matcher() {
        return matcher;
    }

    @Override
    public CommandRegistry registry() {
        return registry;
    }

    @Override
    public IntegerRepository repository() {
        return repository;
    }

    public static SimpleExecContext context(IntegerRepository repository,
                                            CommandRegistry registry,
                                            Command command,
                                            String input) {
        return new SimpleExecContext(command,
                command.pattern().matcher(input),
                registry,
                repository);
    }

}
