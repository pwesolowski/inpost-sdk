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

import com.tlumaczeo.inpost.core.config.InpostClientConfig
import com.tlumaczeo.inpost.core.models.TrackingResponse
import kotlinx.coroutines.Deferred

/**
 * Entry point for interacting with InPost APIs.
 * Inspired by AWS SDK v2 style and Kotlin coroutines.
 */
interface InpostClient : AutoCloseable {
    val config: InpostClientConfig

    /**
     * Example endpoint: track a shipment by tracking number.
     * Suspends or returns a Deferred for flexible async usage.
     */
    suspend fun trackShipment(trackingNumber: String): TrackingResponse

    fun trackShipmentAsync(trackingNumber: String): Deferred<TrackingResponse>

    override fun close() {}

    companion object {
        fun builder(): InpostClientBuilder = InpostClientBuilder()
    }
}
