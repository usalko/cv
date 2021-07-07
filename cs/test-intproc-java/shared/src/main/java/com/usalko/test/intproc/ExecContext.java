package com.usalko.test.intproc;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

import java.util.regex.Matcher;

/**
 * Контекст выполнения комманды
 */
public interface ExecContext {

    Matcher matcher();

    CommandRegistry registry();

    IntegerRepository repository();

}
