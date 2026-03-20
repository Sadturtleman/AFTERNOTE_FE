package com.kuit.afternote.feature.afternote.domain.model

import android.R.id.message
import com.kuit.afternote.core.domain.model.AfternoteServiceType

/**
 * 애프터노트 아이템 도메인 모델
 *
 * @param id 고유 식별자
 * @param serviceName 서비스명
 * @param date 날짜 (yyyy.MM.dd 형식)
 * @param type 서비스 타입 (필터링용)
 * @param message 남기실 말씀
 */
data class AfternoteItem(
    val id: String,
    val serviceName: String,
    val date: String,
    val type: AfternoteServiceType,
    val account: AfternoteAccount = AfternoteAccount(),
    val processing: AfternoteItemProcessing = AfternoteItemProcessing(),
)

data class AfternoteAccount(
    val id: String = "",
    val password: String = "",
)

data class AfternoteItemProcessing(
    val message: String = "",
    val accountMethod: String = "",
    val informationMethod: String = "",
    val methods: List<AfternoteProcessingMethod> = emptyList(),
    val galleryMethods: List<AfternoteProcessingMethod> = emptyList(),
)
