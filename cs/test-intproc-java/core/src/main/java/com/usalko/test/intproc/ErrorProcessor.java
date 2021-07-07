package com.usalko.test.intproc;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

import com.usalko.test.intproc.cmd.Command;

/**
 * Процессор для обработки ошибок выполнения команды
 * Или неудачного парсинга в InputProcessor
 */
public class ErrorProcessor {

    public String handle(Command command, Throwable th) {
        if (th instanceof IntProcException) {
            return command.handleError(th);
        }
        return "Необработанная исключительная ситуация: " + th.getLocalizedMessage();
    }
}
