package com.usalko.crpt.fazeco;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */
@RunWith(Parameterized.class)
public class CliUtilTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"0",
                        Integer.valueOf(0).byteValue()},
                {"1",
                        Integer.valueOf(1).byteValue()},
                {"1234",
                        Integer.valueOf(1234).shortValue()},
                {"1234.5",
                        1234.5},
                {"12341222",
                        12341222},
                {"12341222111111111",
                        12341222111111111L},
                {"1234122211111111111111111111",
                        new BigInteger("1234122211111111111111111111")},
                {"-1",
                        Integer.valueOf(-1).byteValue()},
                {"+1",
                        Integer.valueOf(1).byteValue()},
        });
    }

    private final String arg0;

    private final Number expected;

    public CliUtilTest(String arg0, Number expected) {
        this.arg0 = arg0;
        this.expected = expected;
    }

    @Test
    public void parseNumber() throws Exception {
        assertEquals(expected, CliUtil.parseNumber(arg0));
    }

}