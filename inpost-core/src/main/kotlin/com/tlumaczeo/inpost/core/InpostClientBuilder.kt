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

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.tlumaczeo.inpost.core.auth.AuthCredentials
import com.tlumaczeo.inpost.core.config.InpostClientConfig
import com.tlumaczeo.inpost.core.internal.DefaultInpostClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.jackson.JacksonConverter

class InpostClientBuilder internal constructor() {
    private var baseUrl: String = "https://api.inpost.pl"
    private var credentials: AuthCredentials? = null
    private var connectTimeoutMs: Long = 10_000
    private var readTimeoutMs: Long = 30_000
    private var userAgent: String = "inpost-jvm-sdk"
    private var httpClient: HttpClient? = null
    private var objectMapper: ObjectMapper = defaultObjectMapper()

    fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

    fun credentials(credentials: AuthCredentials) = apply { this.credentials = credentials }

    fun connectTimeoutMs(timeout: Long) = apply { this.connectTimeoutMs = timeout }

    fun readTimeoutMs(timeout: Long) = apply { this.readTimeoutMs = timeout }

    fun userAgent(agent: String) = apply { this.userAgent = agent }

    fun httpClient(client: HttpClient) = apply { this.httpClient = client }

    fun objectMapper(mapper: ObjectMapper) = apply { this.objectMapper = mapper }

    fun build(): InpostClient {
        val creds = requireNotNull(credentials) { "Auth credentials must be provided" }
        val cfg =
            InpostClientConfig(
                baseUrl = baseUrl,
                credentials = creds,
                connectTimeoutMs = connectTimeoutMs,
                readTimeoutMs = readTimeoutMs,
                userAgent = userAgent,
            )
        val client = httpClient ?: createDefaultHttpClient(cfg, objectMapper)
        return DefaultInpostClient(cfg, client, objectMapper)
    }

    private fun createDefaultHttpClient(
        config: InpostClientConfig,
        mapper: ObjectMapper,
    ): HttpClient =
        HttpClient(CIO) {
            engine {
                requestTimeout = config.readTimeoutMs
            }
            install(ContentNegotiation) {
                register(io.ktor.http.ContentType.Application.Json, JacksonConverter(mapper))
            }
            defaultRequest {
                headers.append("User-Agent", config.userAgent)
            }
        }

    private fun defaultObjectMapper(): ObjectMapper =
        jacksonObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}
