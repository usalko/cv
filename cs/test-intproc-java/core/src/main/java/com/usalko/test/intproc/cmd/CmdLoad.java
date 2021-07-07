package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;
import com.usalko.test.intproc.utils.Utils;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

import static com.usalko.test.intproc.utils.Utils.DEFAULT_SEPARATOR_VALUE;
import static com.usalko.test.intproc.utils.Utils.NL;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdLoad extends AbstractCommand {

    public CmdLoad() {
        super(Pattern.compile("\\s*LOAD(\\s+\\S+)(\\s+\\S+)?\\s*", Pattern.CASE_INSENSITIVE),
                "Загрузить набор из текстового файла. " + NL +
                        "Имя файла может быть с путем или без него. " + NL +
                        "Для указания пробелов и разделителей пути можно использовать комбинации спец. символов. " + NL +
                        "Если разделитель не указан, используется символ табуляции. " + NL +
                        "Перед началом загрузки, если файл существует, текущие значения будут очищены " + NL +
                        "по аналогии с коммандой CLEAR",
                "LOAD 1.txt" + NL + "LOAD c:\\\\1\\l1.txt ;");
    }

    @Override
    public String exec(ExecContext execContext) {
        String fileName = Utils.unescapedWithTrim(execContext.matcher().group(1));
        if (fileName.isEmpty()) {
            throw new IllegalStateException("Не указан обязательный аргумент комманды: имя файла для загрузки данных");
        }
        String separator = Utils.unescapedWithTrim(execContext.matcher().group(2));
        String separatorValue = separator.isEmpty() ? DEFAULT_SEPARATOR_VALUE : separator;
        File file = new File(fileName.replaceAll("\\\\", "\\"));
        if (!file.exists()) {
            throw new IllegalStateException("К сожалению файл: " + file.getAbsolutePath() + " не существует, загрузка данных не может быть произведена");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int countOfValues = 0;
            char[] buffer = new char[2048];
            int readChars;
            execContext.repository().process(List::clear);
            StringBuilder words = new StringBuilder();
            while ((readChars = reader.read(buffer)) >= 0) {
                if (readChars == 0) {
                    continue;
                }
                words.append(buffer, 0, readChars);
                countOfValues += processWordsWithoutTail(execContext, words, separatorValue);
            }
            countOfValues += processSingleWord(execContext, words.toString(), separatorValue);
            return "Обработан файл : " + file.getAbsolutePath() + NL + "Загружено " + countOfValues + " значений";
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("К сожалению файл: " + file.getAbsolutePath() + " не существует, загрузка данных не может быть произведена");
        } catch (IOException e) {
            throw new IllegalStateException("Не обрабатываемая ошибка при загрузке данных из файла: " + file.getAbsolutePath() + ", возможно загрузка была частично произведена, для проверки используйте команду LIST");
        }
    }

    private int processWordsWithoutTail(ExecContext execContext, StringBuilder words, String separatorValue) {
        int count = 0;
        int pos;
        while ((pos = words.indexOf(separatorValue)) >= 0) { // Хвост (pos < 0) не обрабатываем
            String word = words.substring(0, pos);
            processSingleWord(execContext, word, separatorValue);
            words.delete(0, pos + separatorValue.length());
            count++;
        }
        return count;
    }

    private int processSingleWord(ExecContext execContext, String word, String separatorValue) {
        Utils.throwIfNull(execContext, "Argument execContext can't be null");
        Utils.throwIfNull(word, "Argument word can't be null");
        Utils.throwIfNull(separatorValue, "Argument separatorValue can't be null");
        if (word.contains(separatorValue)) {
            return 0;
        }
        int integerValue = Integer.valueOf(word.replaceAll("\\s+|\\n|\\r", ""));
        execContext.repository().addInteger(integerValue);
        return 1;
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка загрузки списка элементов из файла: " + th.getLocalizedMessage();
    }
}
