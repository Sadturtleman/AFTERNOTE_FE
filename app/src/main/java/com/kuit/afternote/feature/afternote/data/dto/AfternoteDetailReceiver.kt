package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Receiver in GET /afternotes/{id} response (GALLERY).
 * API currently returns only receiverId; name/relation are resolved via GET /users/receivers.
 * Accepts optional name/relation/phone if API adds them later.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class AfternoteDetailReceiver(
    @SerialName("receiverId")
    @JsonNames("receiver_id") val receiverId: Long? = null,
    @SerialName("name")
    @JsonNames("receiverName", "receiver_name") val name: String? = null,
    @SerialName("relation")
    @JsonNames("receiverRelation", "receiver_relation", "relationship") val relation: String? = null,
    @SerialName("phone")
    @JsonNames(
        "receiverPhone",
        "receiver_phone",
        "phoneNumber",
        "phone_number",
    ) val phone: String? = null,
)
