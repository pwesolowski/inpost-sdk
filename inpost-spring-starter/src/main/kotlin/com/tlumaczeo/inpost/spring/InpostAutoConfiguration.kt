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

package io.github.pwesolowski.inpost.spring

import io.github.pwesolowski.inpost.core.InpostClient
import io.github.pwesolowski.inpost.core.auth.AuthCredentials
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@EnableConfigurationProperties(InpostProperties::class)
class InpostAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    fun inpostClient(props: InpostProperties): InpostClient =
        InpostClient
            .builder()
            .baseUrl(props.baseUrl)
            .credentials(
                when (props.auth.type) {
                    "apiKey" -> AuthCredentials.ApiKey(props.auth.apiKey ?: error("apiKey required"))
                    else -> error("Unsupported auth type: ${props.auth.type}")
                },
            ).build()
}

@ConfigurationProperties(prefix = "inpost")
data class InpostProperties(
    val baseUrl: String = "https://api.inpost.pl",
    val auth: AuthProperties = AuthProperties(),
)

data class AuthProperties(
    val type: String = "apiKey",
    val apiKey: String? = null,
)
