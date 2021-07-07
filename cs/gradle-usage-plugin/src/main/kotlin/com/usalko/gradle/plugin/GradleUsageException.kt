package com.usalko.gradle.plugin

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class GradleUsageException : RuntimeException {

    internal enum class TypicalReason(private val messageTemplate: String) {
        CLASS_ENTRY_ERROR("Could not obtain class entry for {}"),
        GENERAL_IO_EXCEPTION("Input/output error");

        fun format(vararg messageTemplateArguments: Any): String {
            return String.format(messageTemplate, *messageTemplateArguments)
        }
    }

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    internal constructor(reason: TypicalReason, cause: Throwable,
                         vararg messageTemplateArguments: Any) : super(reason.format(*messageTemplateArguments), cause) {
    }
}
