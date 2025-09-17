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

package io.github.pwesolowski.inpost.core

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.pwesolowski.inpost.core.auth.AuthCredentials
import io.github.pwesolowski.inpost.core.config.InpostClientConfig
import io.github.pwesolowski.inpost.core.internal.DefaultInpostClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Duration

class InpostClientBuilder internal constructor() {
    private var baseUrl: String = "https://api.inpost.pl"
    private var credentials: AuthCredentials? = null
    private var connectTimeoutMs: Long = 10_000
    private var readTimeoutMs: Long = 30_000
    private var userAgent: String = "inpost-jvm-sdk"
    private var httpClient: OkHttpClient? = null
    private var objectMapper: ObjectMapper = defaultObjectMapper()

    fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

    fun credentials(credentials: AuthCredentials) = apply { this.credentials = credentials }

    fun connectTimeoutMs(timeout: Long) = apply { this.connectTimeoutMs = timeout }

    fun readTimeoutMs(timeout: Long) = apply { this.readTimeoutMs = timeout }

    fun userAgent(agent: String) = apply { this.userAgent = agent }

    fun httpClient(client: OkHttpClient) = apply { this.httpClient = client }

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
        val client = httpClient ?: createDefaultHttpClient(cfg, creds)
        return DefaultInpostClient(cfg, client, objectMapper)
    }

    private fun createDefaultHttpClient(
        config: InpostClientConfig,
        creds: AuthCredentials,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .connectTimeout(Duration.ofMillis(config.connectTimeoutMs))
            .readTimeout(Duration.ofMillis(config.readTimeoutMs))
            .addInterceptor(UserAgentInterceptor(config.userAgent))
            .addInterceptor(AuthInterceptor(creds))
            .build()

    private fun defaultObjectMapper(): ObjectMapper =
        jacksonObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}

private class UserAgentInterceptor(
    private val userAgent: String,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val original: Request = chain.request()
        val request =
            original
                .newBuilder()
                .header("User-Agent", userAgent)
                .build()
        return chain.proceed(request)
    }
}

private class AuthInterceptor(
    private val credentials: AuthCredentials,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val original = chain.request()
        val builder = original.newBuilder()
        when (credentials) {
            is io.github.pwesolowski.inpost.core.auth.AuthCredentials.ApiKey ->
                builder.header(
                    "Authorization",
                    "Bearer ${credentials.key}",
                )
            is io.github.pwesolowski.inpost.core.auth.AuthCredentials.OAuth2ClientCredentials -> {
                throw UnsupportedOperationException("OAuth2 client credentials flow not implemented yet")
            }
        }
        return chain.proceed(builder.build())
    }
}
