package com.kuit.afternote.feature.timeletter.data.dto
//TODO: 예전에 해보려고 한 건데 나중에 지워야 됨. 서버랑 다름

data class TimeLetterReceiver(
    val id: Long,
    val receiver_name:  String,
    val send_at: String,
    val title: String,
    val content: String,
    val image_url: String?,
    val relation : String
)

