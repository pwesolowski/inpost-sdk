/*
 * Copyright (c) ${YEAR} Piotr Tłumaczeo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tlumaczeo.inpost.core

import com.tlumaczeo.inpost.core.auth.AuthCredentials
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InpostClientBuilderTest {
    @Test
    fun buildsClientWithProvidedConfig() {
        val client =
            InpostClient
                .builder()
                .baseUrl("https://sandbox-api.inpost.pl")
                .credentials(AuthCredentials.ApiKey("test-key"))
                .userAgent("custom-agent")
                .build()
        client.use {
            assertEquals("https://sandbox-api.inpost.pl", it.config.baseUrl)
            assertEquals("custom-agent", it.config.userAgent)
        }
    }
}
