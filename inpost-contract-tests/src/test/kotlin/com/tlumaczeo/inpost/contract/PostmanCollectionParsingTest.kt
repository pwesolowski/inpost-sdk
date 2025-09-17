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

package com.tlumaczeo.inpost.contract

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class PostmanCollectionParsingTest {
    @Test
    fun parseFirstCollection() {
        val dir = Paths.get("postman")
        assertTrue(Files.exists(dir), "Postman collections directory should exist: $dir")
        val first =
            Files.list(dir).use { stream ->
                stream
                    .filter { it.fileName.toString().endsWith(".postman_collection.json") }
                    .findFirst()
                    .orElseThrow { IllegalStateException("No Postman collection found in $dir") }
            }
        val mapper = jacksonObjectMapper().findAndRegisterModules()
        val json: Map<String, Any?> = mapper.readValue(Files.readString(first))
        assertNotNull(json["info"], "Collection should contain 'info'")
    }
}
