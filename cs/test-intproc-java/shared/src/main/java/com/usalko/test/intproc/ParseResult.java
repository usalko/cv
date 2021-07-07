package com.usalko.test.intproc;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

import com.usalko.test.intproc.cmd.Command;

/**
 * Инкапсуляция выполнения поиска команды
 */
public interface ParseResult extends ExecContext {

    Command command();

}
