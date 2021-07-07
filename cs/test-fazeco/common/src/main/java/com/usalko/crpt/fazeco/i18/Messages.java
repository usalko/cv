package com.usalko.crpt.fazeco.i18;

import com.usalko.crpt.fazeco.ArgumentChecks;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */

/**
 * Интерфейс инкапсулирующий ключи сообщений
 * И методы трансляции сообщений
 */
public interface Messages {

    String NOT_IMPLEMENTED_CASE = "runtime.not-implemented-case";
    String NUMBER_CANT_BE_NULL = "runtime.number-cant-be-null";
    String ARGUMENT_CANT_BE_NULL = "runtime.argument-cant-be-null";
    String CANT_PARSE_STRING_FOR_ENUM = "runtime.cant-parse-string-for-enum";
    String INVALID_TYPE_NUMBER = "exception.invalid-type-number";
    String CLI_INPUT_PROMPT = "cli.input-prompt";
    String CLI_RESULT = "cli.result";
    String CLI_INVALID_NUMBER = "cli.invalid-number";
    String APP_VERSION = "app.version";

    Charset ISO_8859_1_CHARSET = Charset.forName("ISO-8859-1");

    /**
     * Утилита для трансляции сообщений в локаль по умолчанию
     * @param message ключ сообщения по которому вытаскивается текст или шаблон
     * @param messageArguments аргументы для шаблоны сообщения
     * @return сообщения в локали по умолчанию @see Locale#getDefault
     */
    static String translate(String message, Object... messageArguments) {
        return translate(message, Locale.getDefault(), messageArguments);
    }

    /**
     * Утилита для трансляции текстовых сообщений
     * @param message ключ текстового сообщения см. i18/messages
     * @param locale локаль для пакета
     * @param messageArguments набор аргументов, действует если по ключу расположен шаблон
     * @return транслированное сообщение
     */
    static String translate(String message, Locale locale, Object... messageArguments) {
        ResourceBundle messages = ResourceBundle.getBundle("i18/messages", locale);
        return String.format(new String(messages.getString(message).getBytes(ISO_8859_1_CHARSET),
                Charset.defaultCharset()), messageArguments);
    }

    /**
     * Загружает ресурс как строку
     * @param resource имя ресурса
     * @return строка или null если ресурс не найден
     */
    static String resource(String resource) {
        ArgumentChecks.throwIfNull(resource);
        try(InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                return null;
            }
            InputStreamReader reader = new InputStreamReader(in);
            StringWriter writer = new StringWriter();
            int readCount;
            char[] buffer = new char[2048];
            while((readCount = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, readCount);
            }
            writer.close();
            return writer.toString();
        } catch (IOException e) {
            // Игнорируем исключение согласно контракту
            return null;
        }
    }
}
