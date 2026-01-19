package com.kuit.afternote.feature.timeletter.data.dto

data class TimeLetterReceiver(
    val id: Long,
    val receiver_name:  String,
    val send_at: String,
    val title: String,
    val content: String,
    val image_url: String?,
    val relation : String
)

