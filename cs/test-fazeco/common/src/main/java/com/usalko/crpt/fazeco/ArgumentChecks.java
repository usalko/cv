package com.usalko.crpt.fazeco;

import com.usalko.crpt.fazeco.i18.Messages;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

/**
 * Проверки аргументов, вынесены в утилитарный класс, ради контроля сообщений в исключениях
 */
public interface ArgumentChecks {

    /**
     * Если аргумент null выкидываем runtime исключение
     * @param value если не null проверка пройдена и исключение не выбрасывается
     */
    static void throwIfNull(Object value) {
        if (value != null) {
            return;
        }
        throw new FazecoRuntimeException(Messages.ARGUMENT_CANT_BE_NULL);
    }

}
