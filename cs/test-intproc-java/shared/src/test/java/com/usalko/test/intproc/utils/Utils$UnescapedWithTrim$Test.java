package com.usalko.test.intproc.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */
@RunWith(Parameterized.class)
public class Utils$UnescapedWithTrim$Test {

    private final String expected;
    private final String input;

    public Utils$UnescapedWithTrim$Test(String input, String expected) {
        this.expected = expected;
        this.input = input;
    }

    @Parameterized.Parameters
    public static Collection cases() throws InstantiationException, IllegalAccessException {
        return Arrays.asList(new Object[][]{
                {"123\\s123", "123 123"},
                {"123\\t123", "123\t123"},
                {"123\\\\123", "123\\123"}
        });
    }


        @Test
    public void test() {
        assertEquals(expected, Utils.unescapedWithTrim(input));
    }

}