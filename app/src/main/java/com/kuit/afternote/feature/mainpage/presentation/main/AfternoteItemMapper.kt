package com.kuit.afternote.feature.mainpage.presentation.main

import com.kuit.afternote.feature.mainpage.domain.model.AfternoteItem
import com.kuit.afternote.feature.mainpage.domain.model.ServiceType
import java.util.UUID

/**
 * Pair<String, String>을 AfternoteItem으로 변환하는 매퍼
 * 레거시 데이터 형식과의 호환성을 위한 헬퍼
 */
object AfternoteItemMapper {
    /**
     * Pair를 AfternoteItem으로 변환
     */
    fun toAfternoteItem(pair: Pair<String, String>): AfternoteItem {
        val (serviceName, date) = pair
        val serviceType = inferServiceType(serviceName)
        return AfternoteItem(
            id = UUID.randomUUID().toString(),
            serviceName = serviceName,
            date = date,
            type = serviceType
        )
    }

    /**
     * List<Pair>를 List<AfternoteItem>으로 변환
     */
    fun toAfternoteItems(pairs: List<Pair<String, String>>): List<AfternoteItem> {
        return pairs.map { toAfternoteItem(it) }
    }

    /**
     * 서비스명으로부터 ServiceType 추론
     */
    private fun inferServiceType(serviceName: String): ServiceType {
        return when (serviceName) {
            "인스타그램", "페이스북", "트위터", "카카오톡", "네이버" -> ServiceType.SOCIAL_NETWORK
            "갤러리", "파일" -> ServiceType.GALLERY_AND_FILES
            "추모 가이드라인" -> ServiceType.MEMORIAL
            "네이버 메일" -> ServiceType.BUSINESS
            else -> ServiceType.OTHER
        }
    }
}
