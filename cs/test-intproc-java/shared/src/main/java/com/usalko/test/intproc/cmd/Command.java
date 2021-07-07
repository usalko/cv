package com.usalko.test.intproc.cmd;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

import com.usalko.test.intproc.ExecContext;

import java.util.regex.Pattern;

/**
 * Инкапсуляция команды
 */
public interface Command {

    String exec(ExecContext execContext);

    String handleError(Throwable th);

    Pattern pattern();

}
