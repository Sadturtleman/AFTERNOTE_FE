package com.kuit.afternote.feature.dailyrecord.domain.usecase

import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepository
import javax.inject.Inject

/**
 * Sets whether a specific mind record is delivered to a specific receiver.
 * PATCH /mind-records/{recordId}/receivers/{receiverId}
 *
 * @param recordId 마음의 기록 ID
 * @param receiverId 수신인 ID
 * @param enabled true → 해당 수신인에게 전달, false → 전달 중단
 * @return [Result] success on 200, failure on 403 or other error
 */
class SetMindRecordReceiverEnabledUseCase @Inject constructor(
    private val repository: MindRecordRepository
) {
    suspend operator fun invoke(
        recordId: Long,
        receiverId: Long,
        enabled: Boolean
    ): Result<Unit> {
        return try {
            repository.setMindRecordReceiverEnabled(
                recordId = recordId,
                receiverId = receiverId,
                enabled = enabled
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
