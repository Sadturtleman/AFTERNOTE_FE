package com.kuit.afternote.feature.afternote.data.dto

import kotlinx.serialization.ExperimentalSerializationApi
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
    @JsonNames("receiver_id") val receiverId: Long? = null,
    @JsonNames("receiverName", "receiver_name") val name: String? = null,
    @JsonNames("receiverRelation", "receiver_relation", "relationship") val relation: String? = null,
    @JsonNames(
        "receiverPhone",
        "receiver_phone",
        "phoneNumber",
        "phone_number",
    ) val phone: String? = null,
)
