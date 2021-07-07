package com.usalko.test.intproc.cmd;

import com.usalko.test.intproc.ExecContext;
import com.usalko.test.intproc.utils.Utils;

import java.io.*;
import java.util.regex.Pattern;

import static com.usalko.test.intproc.utils.Utils.DEFAULT_SEPARATOR_VALUE;
import static com.usalko.test.intproc.utils.Utils.NL;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

public class CmdSave extends AbstractCommand {

    public CmdSave() {
        super(Pattern.compile("\\s*SAVE(\\s+\\S+)(\\s+\\S+)?\\s*", Pattern.CASE_INSENSITIVE),
                "Сохранить набор в текстовый файл. " + NL +
                        "Имя файла может быть с путем или без него. " + NL +
                        "Для указания пробелов и разделителей пути можно использовать комбинации спец. символов. " + NL +
                        "Если разделитель не указан, используется символ табуляции. ",
                "SAVE 1.txt" + NL + "SAVE c:\\\\1\\s2.txt ;");
    }

    @Override
    public String exec(ExecContext execContext) {
        String fileName = Utils.unescapedWithTrim(execContext.matcher().group(1));
        if (fileName.isEmpty()) {
            throw new IllegalStateException("Не указан обязательный аргумент комманды: имя файла для сохранения данных");
        }
        String separator = Utils.unescapedWithTrim(execContext.matcher().group(2));
        String separatorValue = separator.isEmpty() ? DEFAULT_SEPARATOR_VALUE : separator;
        File file = new File(fileName.replaceAll("\\\\", "\\"));
        if (file.exists()) {
            throw new IllegalStateException("К сожалению файл: " + file.getAbsolutePath() +
                    " существует, удалите пожалуйста или переместите файл перед выгрузкой данный. " +
                    "Поскольку при выгрузке в существующий файл информация, которая в нем находилась " +
                    "может быть потеряна.");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            int countOfValues = 0;
            for (Integer value : execContext.repository()) {
                writer.write(value + separatorValue);
                countOfValues++;
            }
            writer.flush();
            return "Сохранены данные в файл : " + file.getAbsolutePath() + NL + "Выгружено " + countOfValues + " значений";
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("К сожалению файл: " + file.getAbsolutePath() + " не существует, загрузка данных не может быть произведена");
        } catch (IOException e) {
            throw new IllegalStateException("Не обрабатываемая ошибка при сохранении данных в файл: " + file.getAbsolutePath());
        }
    }

    @Override
    public String handleError(Throwable th) {
        return "Ошибка сохранения списка элементов в файл: " + th.getLocalizedMessage();
    }
}
