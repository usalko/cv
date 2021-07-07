package com.usalko.test.intproc;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

import com.usalko.test.intproc.cmd.Command;
import com.usalko.test.intproc.storage.InMemoryIntegerRepository;

import java.io.IOException;
import java.util.ServiceLoader;

/**
 * Класс для запуска из консоли
 */
public class IntProc {

    public static void main(String[] args) throws IOException {
        ServiceLoader<Command> commandLoader = ServiceLoader.load(Command.class);

        CommandProcessor commandProcessor = new CommandProcessor(
                new InMemoryIntegerRepository(),
                new ErrorProcessor());

        for (Command command : commandLoader) {
            commandProcessor.registerCommand(command);
        }

        System.out.println("Что бы узнать список возможных комманд введите комманду HELP");
        new InputProcessor(System.in, System.out).start(commandProcessor);
    }
}
