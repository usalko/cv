package com.usalko.crpt.fazeco;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

import com.usalko.crpt.fazeco.i18.Messages;

/**
 * Общий класс для всех обрабатываемых исключений инициируемых логикой прикладной программы
 * Вводится для обеспечения единой точки локализации сообщений
 */
public class FazecoException extends Exception {

    /**
     * Основной конструктор, транслирует сообщение в текущую локаль
     * @param message ключ сообщения (см. пакет i18/messages)
     * @param messageArguments аргументы используются, в том случае если сообщение является шаблоном
     */
    public FazecoException(String message, Object... messageArguments) {
        super(Messages.translate(message, messageArguments));
    }
}
