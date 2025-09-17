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

package pl.inpost.core.config

import pl.inpost.core.auth.AuthCredentials

/**
 * Configuration for the InPost client.
 */
data class InpostClientConfig(
    val baseUrl: String,
    val credentials: AuthCredentials,
    val connectTimeoutMs: Long = 10_000,
    val readTimeoutMs: Long = 30_000,
    val userAgent: String = "inpost-jvm-sdk",
)
