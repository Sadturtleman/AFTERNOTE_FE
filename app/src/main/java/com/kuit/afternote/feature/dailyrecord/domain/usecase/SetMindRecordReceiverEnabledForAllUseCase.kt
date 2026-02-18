package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepository
import javax.inject.Inject

/**
 * 수신인에게 "나의 모든 마음의 기록" 전달 여부를 일괄 설정합니다.
 * PATCH mind-records/{recordId}/receivers/{receiverId} 를
 * 현재 사용자의 모든 마음의 기록에 대해 호출합니다.
 */
class SetMindRecordReceiverEnabledForAllUseCase
    @Inject
    constructor(
        private val repository: MindRecordRepository
    ) {
        suspend operator fun invoke(receiverId: Long, enabled: Boolean) {
            val recordIds = collectAllMindRecordIds()
            recordIds.forEach { recordId ->
                repository.setMindRecordReceiverEnabled(
                    recordId = recordId,
                    receiverId = receiverId,
                    enabled = enabled
                )
            }
        }

        private suspend fun collectAllMindRecordIds(): List<Long> {
            val types = listOf("DAILY_QUESTION", "DIARY", "DEEP_THOUGHT")
            val allIds = types.flatMap { type ->
                repository.getMindRecords(type, "LIST", null, null)
                    .data?.records?.map { it.recordId } ?: emptyList()
            }
            return allIds.distinct()
        }
    }
