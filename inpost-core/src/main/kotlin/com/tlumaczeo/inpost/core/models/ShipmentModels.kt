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

package io.github.pwesolowski.inpost.core.models

data class Organization(
    val id: String,
    val name: String?,
)

data class Shipment(
    val id: String?,
    val status: String?,
    val tracking_number: String?,
    val service: String?,
    val reference: String?,
    val is_return: Boolean?,
)

// Simplified request based on "Tworzenie przesyłki w trybie uproszczonym"
// https://dokumentacja-inpost.atlassian.net/wiki/spaces/PL/pages/11731061/Tworzenie+przesy+ki+w+trybie+uproszczonym

data class CreateShipmentRequest(
    val sender: Party,
    val receiver: Party?,
    val parcels: Parcels,
    val service: String?,
    val custom_attributes: Map<String, Any?>? = null,
    val cod: Cod? = null,
    val reference: String? = null,
    val is_return: Boolean? = null,
)

data class Party(
    val company_name: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: Address? = null,
)

data class Address(
    val street: String? = null,
    val building_number: String? = null,
    val city: String? = null,
    val post_code: String? = null,
    val country_code: String? = null,
)

data class Parcels(
    val template: String? = null,
    val dimensions: Dimensions? = null,
    val weight: Weight? = null,
)

data class Dimensions(
    val length: Double? = null,
    val width: Double? = null,
    val height: Double? = null,
    val unit: String? = null,
)

data class Weight(
    val amount: Double? = null,
    val unit: String? = null,
)

data class Cod(
    val amount: Double? = null,
    val currency: String? = null,
)
