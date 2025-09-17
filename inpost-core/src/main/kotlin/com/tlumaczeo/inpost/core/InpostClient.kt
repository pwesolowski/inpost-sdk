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

import io.github.pwesolowski.inpost.core.config.InpostClientConfig
import io.github.pwesolowski.inpost.core.models.CreateShipmentRequest
import io.github.pwesolowski.inpost.core.models.Organization
import io.github.pwesolowski.inpost.core.models.Shipment

/**
 * Entry point for interacting with InPost APIs (synchronous).
 * Inspired by AWS SDK v2 style.
 */
interface InpostClient : AutoCloseable {
    val config: InpostClientConfig

    fun getOrganization(organizationId: String): Organization

    fun listShipments(
        organizationId: String,
        page: Int? = null,
        perPage: Int? = null,
    ): List<Shipment>

    fun createShipment(
        organizationId: String,
        request: CreateShipmentRequest,
    ): Shipment

    override fun close() {}

    companion object {
        fun builder(): InpostClientBuilder = InpostClientBuilder()
    }
}
