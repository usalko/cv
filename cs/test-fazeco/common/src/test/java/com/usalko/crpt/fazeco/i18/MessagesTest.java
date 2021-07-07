package com.usalko.crpt.fazeco.i18;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, February 2019
 */
@RunWith(Parameterized.class)
public class MessagesTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { Messages.INVALID_TYPE_NUMBER, new Locale("en", "US"), new Object[]{"type"}
                        ,  "Invalid type, number type is type"}
        });
    }

    private final String arg0;
    private final Locale arg1;
    private final Object[] arg2;

    private final String expected;

    public MessagesTest(String arg0, Locale arg1, Object[] arg2, String expected) {
        this.arg0 = arg0;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.expected = expected;
    }

    @Test
    public void translate() {
        Assert.assertEquals(expected, Messages.translate(arg0, arg1, arg2));
    }
}