package com.kuit.afternote.feature.timeletter.data.dto
import kotlinx.serialization.Serializable

@Serializable
enum class TimeLetterStatus {
    DRAFT,
    SCHEDULED,
    SENT
}
