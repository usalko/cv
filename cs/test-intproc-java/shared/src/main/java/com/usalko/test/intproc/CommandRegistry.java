package com.usalko.test.intproc;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

import com.usalko.test.intproc.cmd.Command;
import com.usalko.test.intproc.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс инкапсулирующий функционал работы с реестром комманд
 */
public class CommandRegistry {

    private final Map<Pattern, Command> commands;
    private final Map<Integer, Pattern> index;

    public static class CommandNotFound extends Exception { }

    /**
     * Результат поиска комманды
     */
    public class FindResult implements ParseResult {
        private final Matcher matcher;
        private final Command command;
        private final IntegerRepository repository;

        private FindResult(IntegerRepository repository, Matcher matcher, Command command) {
            this.repository = repository;
            this.matcher = matcher;
            this.command = command;
        }

        @Override
        public Command command() {
            return command;
        }

        @Override
        public Matcher matcher() {
            return matcher;
        }

        @Override
        public CommandRegistry registry() {
            return CommandRegistry.this;
        }

        @Override
        public IntegerRepository repository() {
            return repository;
        }
    }

    public CommandRegistry() {
        // Нет определенных требований к хранилищу
        // используем linked для удобства отладки
        this.commands = new LinkedHashMap<>();
        this.index = new LinkedHashMap<>();
    }


    /**
     * Регистрация новой команды
     * Перебор команд при поиске
     * осуществляется в обратном
     * порядке
     * @param pattern паттерн который отвечает области применения комманды для ввода строк
     * @param command
     */
    public void put(Pattern pattern, Command command) {
        commands.put(pattern, command);
        index.put(index.size(), pattern);
    }

    /**
     * Поиск комманды по входной строке
     * @param input Строка которую ввел пользователь
     * @return Комманда в случае успешного поиска
     * @throws CommandNotFound выкидывается в случае неудачи
     */
    public ParseResult find(IntegerRepository repository, String input) throws CommandNotFound {
        Utils.throwIfNull(input, "Argument input can't be null");
        for (int i = index.size() - 1; i >= 0; i--) {
            Pattern pattern = index.get(i);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                return new FindResult(repository, matcher, commands.get(pattern));
            }
        }
        throw new CommandNotFound();
    }

    /**
     * Возвращает список зарегистрированных комманд
     * или пустой список если нет зарегистрированных
     * комманд, для регистрации комманд используйте
     * {@link #put(Pattern, Command)}
     * @return список комманд или пустой список если нет зарегистрированных команд
     */
    public List<Command> availableCommands() {
        return new ArrayList<>(commands.values());
    }

}
