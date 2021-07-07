package com.usalko.test.intproc;

import com.usalko.test.intproc.utils.Utils;

import java.io.*;

/**
 * Copyright (C) usalko.com - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ivan Usalko <ivict@usalko.com>, July 2019
 */
public abstract class AbstractIntProcTest {

    protected static void copyResourceAsFileToCurrentFolder(String resourceUrl) {
        Utils.throwIfNull(resourceUrl, "Argument resourceUrl can't be null");
        try (InputStream resourceStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceUrl);) {
            if (resourceStream == null) {
                throw new IllegalStateException("Resource: " + resourceUrl + ", not found");
            }
            String fileName = new File(resourceUrl).getName();
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileName, false)); // Overwrite file
            byte[] buff = new byte[4096];
            int readBytes;
            while ((readBytes = resourceStream.read(buff)) >= 0) {
                if (readBytes == 0) {
                    continue;
                }
                outputStream.write(buff, 0, readBytes); // Not processing write result because buffered-stream has 8192 capacity by default
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void removeFile(String file) {
        Utils.throwIfNull(file, "Argument file can't be null");
        new File(file).delete();
    }


}
