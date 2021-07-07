package com.usalko.crpt.fazeco;

import com.usalko.crpt.fazeco.i18.Messages;
import org.beryx.textio.StringInputReader;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.swing.SwingTextTerminal;
import org.fusesource.jansi.AnsiConsole;
import picocli.CommandLine;

import java.util.Locale;
import java.util.concurrent.Callable;

/**
 * Консольный интерфейс для проверки тестового задания
 * Используем замечательную библиотеку picocli @see https://github.com/remkop/picocli
 *
 */
@CommandLine.Command(
        versionProvider = FazecoCliVersionProvider.class
)
public class Cli implements Callable<Number> {

    /**
     *  Параметры для запуска cli
     */
    private final CliParams params;

    public Cli(CliParams params) {
        this.params = params;
    }

    /**
     * Метод для запуска консольного приложения
     * @param args набор аргументов передаваемых в программу
     */
    public static void main(String[] args ) {
        if (CliUtil.os() == CliUtil.OS.WINDOWS) {
            AnsiConsole.systemInstall();
        }
        CliParams params = CommandLine.populateCommand(new CliParams(), args);
        if (params.isPrintHelp()) {
            CommandLine.usage(params, System.out);
            return;
        }
        Number result = CommandLine.call(new Cli(params));
        if (result != null) {
            System.out.println(result);
        }
    }

    /**
     * Главный метод класса
     * В случае когда в параметрах отключен интерактив читает первый параметр
     * и возвращает результат в stdout
     * В случае интерактивного режима, открывает консоль и выводит приглашение
     * @return всегда возвращает null
     */
    @Override
    public Number call() throws Exception {
        // Если пользователь указал, переопределяем локаль
        // В противном случае используется локаль по умолчанию
        Locale.setDefault(params.locale());

        MathService mathService = new MathService();
        if (params.commandMode() && params.inputNumber() != null) {
            return mathService.zeroCountInFactorial(params.inputNumber());
        }

        // Interactive mode, wait for user input
        // If console not available display swing-based terminal

        TextIO textIO = TextIoFactory.getTextIO();

        TextTerminal<?> textTerminal = textIO.getTextTerminal();
        if (textTerminal instanceof SwingTextTerminal) {
            textTerminal.getProperties().setPaneWidth(800);
            updateTerminalSettings((SwingTextTerminal) textTerminal);
        }
        textTerminal.println(Messages.resource("header.txt"));
        textTerminal.println(Messages.translate(Messages.APP_VERSION));

        StringInputReader stringInputReader = textIO.newStringInputReader();

        while (true) {
            String input = stringInputReader.read(Messages.translate(Messages.CLI_INPUT_PROMPT));
            if (input.equalsIgnoreCase("q")) {
                textTerminal.dispose();
                break;
            }
            Number number = CliUtil.parseNumber(input);
            if (number != null) {
                try {
                    textTerminal.println(Messages.translate(Messages.CLI_RESULT, number, mathService.zeroCountInFactorial(number)));
                } catch (FazecoRuntimeException | FazecoException e) {
                    textTerminal.println(e.getMessage());
                }
                continue;
            }
            textTerminal.println(Messages.translate(Messages.CLI_INVALID_NUMBER, input));
        }

        return null;
    }

    /**
     *  Настройка терминала для операционной системы
     * @param textTerminal терминал для которого производится настройка
     */
    private void updateTerminalSettings(SwingTextTerminal textTerminal) {
        ArgumentChecks.throwIfNull(textTerminal);
        if (CliUtil.os() == CliUtil.OS.WINDOWS) {
            return;
        }
        textTerminal.setPromptFontFamily("monospaced");
    }
}
