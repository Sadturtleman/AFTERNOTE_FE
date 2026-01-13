package com.kuit.afternote.feature.receiver.presentation.uimodel

import java.time.LocalDate

data class Receiver(
    val name: String,
    val state: Boolean,
    val record: String,
    val subDate: LocalDate,
    val acceptDate: LocalDate
)
