package com.kuit.afternote.feature.timeletter.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "draft_letters")
data class DraftLetterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val receiverName: String,
    val sendDate: String,
    val title: String,
    val content: String,
    val createdAt: Long
)
