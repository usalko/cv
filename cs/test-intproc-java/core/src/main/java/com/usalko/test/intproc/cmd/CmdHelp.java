package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.CommandRegistry;
import com.usalko.test.intproc.ExecContext;
import com.usalko.test.intproc.utils.Utils;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.usalko.test.intproc.utils.Utils.NL;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */
public class CmdHelp extends AbstractCommand {

    private static final Pattern FIRST_WORD = Pattern.compile("((\\\\)(s\\*))?([\\w\\-]+)((\\\\)(s\\*))?");

    public CmdHelp() {
        super(Pattern.compile("\\s*HELP(\\s+\\w+)?(\\s+\\w+)*\\s*", Pattern.CASE_INSENSITIVE),
                "Вывод подсказки",
                "HELP QUIT");
    }

    @Override
    public String exec(ExecContext execContext) {
        int startPos = execContext.matcher().start(1);
        if (startPos < 0) {
            return availableCommands(execContext.registry());
        }
        StringBuffer text = new StringBuffer();
        execContext.matcher().appendTail(text);
        String command = text.substring(startPos);
        Command cmdSearchedByExample = findCommandByExample(execContext, command);
        if (cmdSearchedByExample != null) {
            return descriptionAndExample(cmdSearchedByExample);
        }
        List<Command> commandsFoundedByFirstWord = findCommandsByFirstWord(execContext.registry(), command);
        if (!commandsFoundedByFirstWord.isEmpty()) {
            return commandsFoundedByFirstWord.stream().map(this::descriptionAndExample).collect(Collectors.joining(NL));
        }
        return "Не найдена комманда: " + command;
    }

    private List<Command> findCommandsByFirstWord(CommandRegistry registry, String input) {
        Utils.throwIfNull(registry, "Argument registry can't be null");
        Utils.throwIfNull(input, "Argument input can't be null");
        String normalizedInput = input.trim().toUpperCase();
        if (normalizedInput.isEmpty()) {
            return Collections.emptyList();
        }

        return registry.availableCommands().stream().filter(command -> {
            Matcher matcher = FIRST_WORD.matcher(command.pattern().pattern());
            String commandFirstWord = matcher.find() ? matcher.group(4).toUpperCase() : command.pattern().pattern();
            return commandFirstWord.startsWith(normalizedInput);
        }).collect(Collectors.toList());
    }

    private Command findCommandByExample(ExecContext execContext, String command) {
        try {
            return execContext.registry().find(execContext.repository(), command).command();
        } catch (CommandRegistry.CommandNotFound e) { // Простой перехват, обработка null на уровне выше
            return null;
        }
    }

    private String availableCommands(CommandRegistry registry) {
        Utils.throwIfNull(registry, "Argument registry can't be null");
        return "Список доступных комманд:" + NL + registry.availableCommands().stream().map(command -> {
            Matcher matcher = FIRST_WORD.matcher(command.pattern().pattern());
            return matcher.find() ? matcher.group(4).toUpperCase(): command.pattern().pattern();
        }).collect(Collectors.joining(", "));
    }

    private String descriptionAndExample(Command command) {
        if (command instanceof HelpSupport) {
            return ((HelpSupport) command).description() + NL + ((HelpSupport) command).example();
        }
        return "Нет подсказки для комманды: " + command;
    }

    @Override
    public String handleError(Throwable th) {
        return th.getLocalizedMessage();
    }
}
