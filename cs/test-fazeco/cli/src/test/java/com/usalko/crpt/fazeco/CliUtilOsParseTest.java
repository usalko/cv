package com.usalko.crpt.fazeco;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */
@RunWith(Parameterized.class)
public class CliUtilOsParseTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"windows 7",
                        CliUtil.OS.WINDOWS},
        });
    }

    private final String arg0;

    private final CliUtil.OS expected;

    public CliUtilOsParseTest(String arg0, CliUtil.OS expected) {
        this.arg0 = arg0;
        this.expected = expected;
    }

    @Test
    public void os() throws Exception {
        assertEquals(expected, CliUtil.OS.parse(arg0));
    }
}