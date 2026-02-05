package com.kuit.afternote.feature.dailyrecord.domain.usecase

import DailyRecordRepositoryImpl
import com.kuit.afternote.feature.dailyrecord.data.RecordPost

class DailyRecordPostUseCase(
    private val repository: DailyRecordRepositoryImpl
) {
    suspend operator fun invoke(title: String, content: String): Result<RecordPost> {
        val post = RecordPost(
            id = TODO(),
            title = TODO(),
            createdAt = TODO(),
            updatedAt = TODO(),
            content = TODO()
        ) // 생성된 게시물
        return Result.success(post)
    }
}
