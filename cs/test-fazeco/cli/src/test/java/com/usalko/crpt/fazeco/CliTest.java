package com.usalko.crpt.fazeco;

import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Итеграционный тест для Cli.
 */
public class CliTest extends IntegrationTest {

    @Test
    public void help() throws Exception {
        String result = exec(Cli.class, "-h");
        System.out.println(result);
        result = Pattern.compile("\\s+", Pattern.DOTALL).matcher(result).replaceAll("");
        assertEquals("Usage:<mainclass>[-ch][-l=<locale>][<inputNumber>][<inputNumber>]-c,--command-mode-h,--help-l,--locale=<locale>", result);
    }

    @Test
    public void calc() throws Exception {
        String result = exec(Cli.class, "123 -c");
        System.out.println(result);
        result = Pattern.compile("\\s+", Pattern.DOTALL).matcher(result).replaceAll("");
        assertEquals("28", result);
    }
}
