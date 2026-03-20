package com.kuit.afternote.feature.afternote.domain.model

import com.kuit.afternote.domain.model.AfternoteServiceType

/**
 * 애프터노트 아이템 도메인 모델
 *
 * @param id 고유 식별자
 * @param serviceName 서비스명
 * @param date 날짜 (yyyy.MM.dd 형식)
 * @param type 서비스 타입 (필터링용)
 */
data class Item(
    val id: String,
    val serviceName: String,
    val date: String,
    val type: AfternoteServiceType,
    val account: Account = Account(),
    val processing: ItemProcessing = ItemProcessing(),
)

data class Account(
    val id: String = "",
    val password: String = "",
)

data class ItemProcessing(
    val message: String = "",
    val accountMethod: String = "",
    val informationMethod: String = "",
    val methods: List<ProcessingMethod> = emptyList(),
    val galleryMethods: List<ProcessingMethod> = emptyList(),
)
