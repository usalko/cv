package com.usalko.test.intproc;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

import com.usalko.test.intproc.cmd.UnknownCommand;
import com.usalko.test.intproc.cmd.Command;
import com.usalko.test.intproc.utils.Utils;

/**
 * Процессор комманд которые вводит пользователь
 */
public class CommandProcessor {

    private final IntegerRepository repository;
    private final ErrorProcessor errorProcessor;
    private final CommandRegistry commandRegistry;

    public CommandProcessor(IntegerRepository repository,
                            ErrorProcessor errorProcessor) {
        this.repository = repository;
        this.errorProcessor = errorProcessor;
        this.commandRegistry = new CommandRegistry();
    }

    public CommandProcessor registerCommand(Command command) {
        Utils.throwIfNull(command, "Argument command can't be null");
        commandRegistry.put(command.pattern(), command);
        return this;
    }

    public String exec(Command command, ExecContext execContext) throws IntProcException {
        Utils.throwIfNull(execContext, "Argument execContext can't be null");
        try {
            return command.exec(execContext);
        } catch (Throwable th) {
            return errorProcessor.handle(command, th);
        }
    }

    public ParseResult parse(String input) throws UnknownCommand {
        try {
            return commandRegistry.find(repository, input);
        } catch (CommandRegistry.CommandNotFound e) {
            throw new UnknownCommand(input);
        }
    }

    public CommandRegistry registry() {
        return commandRegistry;
    }
}
