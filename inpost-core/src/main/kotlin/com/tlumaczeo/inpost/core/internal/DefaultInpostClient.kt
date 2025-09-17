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

package io.github.pwesolowski.inpost.core.internal

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.pwesolowski.inpost.core.InpostClient
import io.github.pwesolowski.inpost.core.config.InpostClientConfig
import io.github.pwesolowski.inpost.core.models.CreateShipmentRequest
import io.github.pwesolowski.inpost.core.models.Organization
import io.github.pwesolowski.inpost.core.models.Shipment
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

internal class DefaultInpostClient(
    override val config: InpostClientConfig,
    private val http: OkHttpClient,
    private val mapper: ObjectMapper,
) : InpostClient {
    override fun getOrganization(organizationId: String): Organization {
        val url = baseUrlBuilder().addPathSegments("v1/organizations/$organizationId").build()
        val request =
            Request
                .Builder()
                .url(url)
                .get()
                .build()
        http.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) error("HTTP ${resp.code}: ${resp.body?.string()}")
            val body = resp.body?.string().orEmpty()
            return mapper.readValue(body)
        }
    }

    override fun listShipments(
        organizationId: String,
        page: Int?,
        perPage: Int?,
    ): List<Shipment> {
        val httpBuilder = baseUrlBuilder().addPathSegments("v1/organizations/$organizationId/shipments")
        if (page != null) httpBuilder.addQueryParameter("page", page.toString())
        if (perPage != null) httpBuilder.addQueryParameter("per_page", perPage.toString())
        val request =
            Request
                .Builder()
                .url(httpBuilder.build())
                .get()
                .build()
        http.newCall(request).execute().use { resp ->
            if (!resp.isSuccessful) error("HTTP ${resp.code}: ${resp.body?.string()}")
            val body = resp.body?.string().orEmpty()
            return mapper.readValue(body)
        }
    }

    override fun createShipment(
        organizationId: String,
        request: CreateShipmentRequest,
    ): Shipment {
        val url = baseUrlBuilder().addPathSegments("v1/organizations/$organizationId/shipments").build()
        val json = mapper.writeValueAsString(request)
        val mediaType = "application/json".toMediaType()
        val req =
            Request
                .Builder()
                .url(url)
                .post(json.toRequestBody(mediaType))
                .build()
        http.newCall(req).execute().use { resp ->
            if (resp.code !in 200..299) error("HTTP ${resp.code}: ${resp.body?.string()}")
            val body = resp.body?.string().orEmpty()
            return mapper.readValue(body)
        }
    }

    private fun baseUrlBuilder(): HttpUrl.Builder = config.baseUrl.toHttpUrl().newBuilder()

    override fun close() {
        // OkHttp cleans up automatically; no-op
    }
}
