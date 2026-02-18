package com.kuit.afternote.feature.dailyrecord.domain.usecase

import android.util.Log
import com.kuit.afternote.feature.dailyrecord.data.dto.EmotionResponse
import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepositoryImpl
import javax.inject.Inject

class GetEmotionsUseCase @Inject constructor(
    private val repositoryImpl: MindRecordRepositoryImpl
) {
    suspend operator fun invoke(): EmotionResponse {
        return try {
            Log.d("TRACE_USECASE", "==> 1. UseCase 호출됨")
            val result = repositoryImpl.getEmotions()
            Log.d("TRACE_USECASE", "<== 2. UseCase 반환됨 (데이터 개수: ${result.emotions.size})")
            result
        } catch (e: Exception) {
            Log.e("TRACE_USECASE", "!!! 3. UseCase에서 예외 발생: ${e.message}")
            throw e
        }
    }
}
