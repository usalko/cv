package com.usalko.test.intproc;

import com.usalko.test.intproc.cmd.*;
import com.usalko.test.intproc.cmd.Command;
import com.usalko.test.intproc.storage.InMemoryIntegerRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.usalko.test.intproc.utils.Utils.NL;
import static org.junit.Assert.assertEquals;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */
@RunWith(Parameterized.class)
public class InputProcessorTest extends AbstractIntProcTest {

    private final String inputString;
    private final Command[] commands;
    private final String expectedResult;

    public InputProcessorTest(String inputString, Command[] commands, String expectedResult) {
        this.inputString = inputString;
        this.commands = commands;
        this.expectedResult = expectedResult;
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        // Копирование в текущую папку файлов из ресурсов
        copyResourceAsFileToCurrentFolder("data/tab-separated-values.txt");
        copyResourceAsFileToCurrentFolder("data/tab-separated-values-tab-ended.txt");
        // Удаление существующих файлов для тестирования команды save
        removeFile("save-test1.txt");
        removeFile("save-test2.txt");
    }

    @Parameterized.Parameters
    public static Collection cases() throws InstantiationException, IllegalAccessException {
        return Arrays.asList(new Object[][] {
                {"COUNT", commands(CmdCount.class), text("Чисел в наборе: 0")},
                {"HELP", commands(SimpleCommand.class, CmdHelp.class),
                        text("Список доступных комманд:", "SIMPLE-COMMAND, HELP")
                },
                {"HELP A", commands(CmdAdd.class, CmdHelp.class),
                        text("Добавить значение в набор", "ADD 12")
                },
                {"HELP del 0", commands(CmdDel.class, CmdHelp.class),
                        text("Удалить из набора элемент с порядковым номером index (отсчет начинается с нуля)", "DEL 0")
                },
                {"HELP", commands(CmdCount.class, CmdHelp.class),
                        text("Список доступных комманд:", "COUNT, HELP")
                },
                {"HELP COUNT", commands(CmdCount.class, CmdHelp.class),
                        text("Отобразить количество чисел в наборе", "COUNT")
                },
                {"ADD 10", commands(CmdAdd.class), text("Добавлено значение: 10")},
                //{"SORT ", commands(CmdSort.class), text("Числа отсортированны по возрастанию")},
                //{"SORT desc", commands(CmdSort.class), text("Числа отсортированны по убыванию")},
                {"CLEAR ", commands(CmdClear.class), text("Удалены все элементы из набора")},
                {"DEL 0 ", commands(CmdDel.class), text("Необработанная исключительная ситуация: Индекс находится вне диапазона 0 0")},
                {"DEL 1 2 ", commands(CmdDelRange.class), text("Необработанная исключительная ситуация: Левая граница индекса: 1 находится вне диапазона")},
                {"FIND 12 ", commands(CmdFind.class), text("Значение 12 не найдено")},
                {"GET 1 ", commands(CmdGet.class), text("Необработанная исключительная ситуация: Индекс находится вне диапазона 0 0")},
                {"LIST ;\\s ", commands(CmdList.class), text("Список элементов: ")},
                {"LOAD 12345678\\s9101112131415161718.txt ;\\s ", commands(CmdLoad.class), text("Необработанная исключительная ситуация: К сожалению файл: " + new File("12345678 9101112131415161718.txt").getAbsolutePath() + " не существует, загрузка данных не может быть произведена")},
                {"SAVE save-test1.txt ;\\s ", commands(CmdSave.class), text("Сохранены данные в файл : " + new File("save-test1.txt").getAbsolutePath(), "Выгружено 0 значений")},
                {"SET 1 2", commands(CmdSet.class), text("Необработанная исключительная ситуация: Индекс: 1 находится вне диапазона")},
                {" UNIQUE", commands(CmdUnique.class), text("Произведено сжатие списка, количество элементов в списке (было -> стало): 0 -> 0")},
                // -- COMPLEX CASES
                //{text("ADD 8", "ADD 18", "SORT desc", "LIST"),
                //        commands(CmdSort.class, CmdAdd.class, CmdList.class),
                //        text("Добавлено значение: 8",
                //                "Добавлено значение: 18",
                //                "Числа отсортированны по убыванию",
                //                "Список элементов: 18\t8", "")},
                {text("ADD 2", "ADD 1", "CLEAR", "LIST"),
                        commands(CmdClear.class, CmdAdd.class, CmdList.class),
                        text("Добавлено значение: 2",
                                "Добавлено значение: 1",
                                "Удалены все элементы из набора",
                                "Список элементов: ", "")},
                {text("ADD 2", "ADD 1", "DEL 0", "LIST"),
                        commands(CmdDel.class, CmdAdd.class, CmdList.class),
                        text("Добавлено значение: 2",
                                "Добавлено значение: 1",
                                "Удален элемент по индексу: 0",
                                "Список элементов: 1", "")},
                {text("ADD 2", "ADD 1", "ADD 3", "ADD 4", "DEL 1 2", "LIST"),
                        commands(CmdDelRange.class, CmdAdd.class, CmdList.class),
                        text("Добавлено значение: 2",
                                "Добавлено значение: 1",
                                "Добавлено значение: 3",
                                "Добавлено значение: 4",
                                "Удалены элементы в диапазоне: 1 2",
                                "Список элементов: 2\t4", "")},
                {text("ADD 2", "ADD 1", "DEL 0 0", "LIST"),
                        commands(CmdDelRange.class, CmdAdd.class, CmdList.class),
                        text("Добавлено значение: 2",
                                "Добавлено значение: 1",
                                "Удален элемент в диапазоне: 0 0",
                                "Список элементов: 1", "")},
                {text("ADD 2", "ADD 1", "CLEAR", "LIST"),
                        commands(CmdClear.class, CmdAdd.class, CmdList.class),
                        text("Добавлено значение: 2",
                                "Добавлено значение: 1",
                                "Удалены все элементы из набора",
                                "Список элементов: ", "")},
                {text("ADD 2", "ADD 1", "COUNT"),
                        commands(CmdCount.class, CmdAdd.class, CmdList.class),
                        text("Добавлено значение: 2",
                                "Добавлено значение: 1",
                                "Чисел в наборе: 2", "")},
                {text("ADD 11", "ADD 12", "FIND 12"),
                        commands(CmdFind.class, CmdAdd.class, CmdList.class),
                        text("Добавлено значение: 11",
                                "Добавлено значение: 12",
                                "Значение 12 найдено в позиции 1", "")},
                {text("ADD 11", "ADD 12", "FIND 13"),
                        commands(CmdFind.class, CmdAdd.class, CmdList.class),
                        text("Добавлено значение: 11",
                                "Добавлено значение: 12",
                                "Значение 13 не найдено", "")},
                {text("ADD 1", " GET 0 "),
                        commands(CmdGet.class, CmdAdd.class, CmdList.class),
                        text("Добавлено значение: 1",
                                "Элемент по идексу 0: 1", "")},
                {text("LOAD tab-separated-values.txt", "LIST"),
                        commands(CmdLoad.class, CmdList.class),
                        text("Обработан файл : " + new File("tab-separated-values.txt").getAbsolutePath(),
                                "Загружено 5 значений",
                                "Список элементов: 12\t13\t14\t15\t16", "")},
                {text("ADD 11", "ADD 12", "ADD 13", "ADD 14", "ADD 15", "SAVE save-test2.txt"),
                        commands(CmdSave.class, CmdAdd.class),
                        text("Добавлено значение: 11",
                                "Добавлено значение: 12",
                                "Добавлено значение: 13",
                                "Добавлено значение: 14",
                                "Добавлено значение: 15",
                                "Сохранены данные в файл : " + new File("save-test2.txt").getAbsolutePath(),
                                "Выгружено 5 значений", "")},
                {text("ADD 55", "ADD 56", "SET 1 59 "),
                        commands(CmdSet.class, CmdAdd.class),
                        text("Добавлено значение: 55",
                                "Добавлено значение: 56",
                                "Установлено значение 59 для элемента с индексом 1", "")},
                {text("ADD 77", "ADD 66", "ADD 77", "UNIQUE "),
                        commands(CmdUnique.class, CmdAdd.class),
                        text("Добавлено значение: 77",
                                "Добавлено значение: 66",
                                "Добавлено значение: 77",
                                "Произведено сжатие списка, количество элементов в списке (было -> стало): 3 -> 2", "")},
                {text("ADD 1", "ADD 1", "ADD 3", "ADD 5", "ADD 1", "ADD 6", "ADD 7", "UNIQUE ", "LIST"),
                        commands(CmdUnique.class, CmdAdd.class, CmdList.class),
                        text("Добавлено значение: 1",
                                "Добавлено значение: 1",
                                "Добавлено значение: 3",
                                "Добавлено значение: 5",
                                "Добавлено значение: 1",
                                "Добавлено значение: 6",
                                "Добавлено значение: 7",
                                "Произведено сжатие списка, количество элементов в списке (было -> стало): 7 -> 5",
                                "Список элементов: 1\t3\t5\t6\t7", "")},
        });
    }

    private static String text(String... lines) {
        StringBuilder result = new StringBuilder();
        for (String line: lines) {
            result.append(line).append(NL);
        }
        return result.toString();
    }

    @SafeVarargs
    private static Command[] commands(Class<? extends Command>... commandTypes) throws IllegalAccessException, InstantiationException {
        List<Command> result = new ArrayList<>();
        for (Class<? extends Command> commandType: commandTypes) {
            result.add(commandType.newInstance());
        }
        return result.toArray(new Command[0]);
    }

    @Test
    public void test() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream((inputString + NL).getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputProcessor inputProcessor = new InputProcessor(inputStream, outputStream);
        CommandProcessor commandProcessor = new CommandProcessor(
                new InMemoryIntegerRepository(),
                new ErrorProcessor());
        for (Command command: commands) {
            commandProcessor.registerCommand(command);
        }
        inputProcessor.start(commandProcessor);

        assertEquals(expectedResult, outputStream.toString());
    }

}