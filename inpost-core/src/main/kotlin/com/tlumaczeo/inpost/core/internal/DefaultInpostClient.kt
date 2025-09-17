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

package com.tlumaczeo.inpost.core.internal

import com.fasterxml.jackson.databind.ObjectMapper
import com.tlumaczeo.inpost.core.InpostClient
import com.tlumaczeo.inpost.core.config.InpostClientConfig
import com.tlumaczeo.inpost.core.models.TrackingResponse
import io.ktor.client.HttpClient
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

internal class DefaultInpostClient(
    override val config: InpostClientConfig,
    private val http: HttpClient,
    private val mapper: ObjectMapper,
) : InpostClient {
    override suspend fun trackShipment(trackingNumber: String): TrackingResponse {
        error("Not implemented")
    }

    override fun trackShipmentAsync(trackingNumber: String): Deferred<TrackingResponse> =
        CompletableDeferred<TrackingResponse>().also {
            it.completeExceptionally(UnsupportedOperationException("Not implemented"))
        }

    override fun close() {
        http.close()
    }
}
