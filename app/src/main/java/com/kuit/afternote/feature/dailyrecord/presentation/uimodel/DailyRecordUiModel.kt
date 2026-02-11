package com.kuit.afternote.feature.dailyrecord.presentation.uimodel

data class MindRecordUiModel(
    val id: Long,
    val title: String,
    val formattedDate: String,
    val draftLabel: String,
    val content: String? = null,
    val type: String? = null,
    val category: String? = null
)

