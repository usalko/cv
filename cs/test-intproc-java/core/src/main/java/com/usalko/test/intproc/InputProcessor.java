package com.usalko.test.intproc;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */

import com.usalko.test.intproc.cmd.UnknownCommand;

import java.io.*;

/**
 * Текстовый процессор для вводимых данных
 * Парсит входные слова в комманды
 */
public class InputProcessor {

    private final InputStream inputStream;
    private final OutputStream outputStream;

    public InputProcessor(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void start(CommandProcessor commandProcessor) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        String cmdInput;
        while ((cmdInput = reader.readLine()) != null) {
            try {
                if (!cmdInput.trim().isEmpty()) {
                    ParseResult parseResult = commandProcessor.parse(cmdInput);
                    writer.write(commandProcessor.exec(parseResult.command(), parseResult));
                }
            } catch (UnknownCommand | IntProcException e) {
                writer.write(e.getLocalizedMessage());
            }
            writer.newLine();
            writer.flush();
        }
    }
}
