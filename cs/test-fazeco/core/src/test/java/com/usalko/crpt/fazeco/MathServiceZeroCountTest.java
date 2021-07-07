package com.usalko.crpt.fazeco;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
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
public class MathServiceZeroCountTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0,
                        0},
                {28,
                        6},
                {100000,
                        24999},
                {10000000000L,
                        2499999997L},
                {new BigInteger("100000000000000000000"),
                        new BigInteger("24999999999999999996")},
        });
    }

    private final Number arg0;

    private final Number expected;

    public MathServiceZeroCountTest(Number arg0, Number expected) {
        this.arg0 = arg0;
        this.expected = expected;
    }

    @Test
    public void zeroCountInFactorial() throws Exception {
        assertEquals(expected, new MathService().zeroCountInFactorial(arg0));
    }
}