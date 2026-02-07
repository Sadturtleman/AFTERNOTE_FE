package com.kuit.afternote.feature.afternote.presentation.screen

import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.ServiceType
import java.util.UUID

/**
 * Pair<String, String> 및 RegisterAfternotePayload를 AfternoteItem으로 변환하는 매퍼
 * 레거시 데이터 형식과의 호환성을 위한 헬퍼
 */
object AfternoteItemMapper {
    /**
     * 등록 페이로드로부터 AfternoteItem 생성 (처리 방법 포함)
     */
    fun fromPayload(payload: RegisterAfternotePayload): AfternoteItem {
        val serviceType = inferServiceType(payload.serviceName)
        return AfternoteItem(
            id = UUID.randomUUID().toString(),
            serviceName = payload.serviceName,
            date = payload.date,
            type = serviceType,
            processingMethods = payload.processingMethods,
            galleryProcessingMethods = payload.galleryProcessingMethods
        )
    }

    /**
     * List<Pair>를 List<AfternoteItem>으로 변환하며 **안정적인 id**를 부여.
     * 목록/상세/편집에서 동일한 id로 조회할 수 있도록 dev·더미 목록용.
     */
    fun toAfternoteItemsWithStableIds(pairs: List<Pair<String, String>>): List<AfternoteItem> =
        pairs.mapIndexed { index, pair ->
            val (serviceName, date) = pair
            val serviceType = inferServiceType(serviceName)
            AfternoteItem(
                id = "dev_${serviceName}_${date}_$index",
                serviceName = serviceName,
                date = date,
                type = serviceType
            )
        }

    /**
     * 서비스명으로부터 ServiceType 추론
     */
    private fun inferServiceType(serviceName: String): ServiceType =
        when (serviceName) {
            "인스타그램", "페이스북", "트위터", "카카오톡", "네이버" -> ServiceType.SOCIAL_NETWORK
            "갤러리", "파일" -> ServiceType.GALLERY_AND_FILES
            "추모 가이드라인" -> ServiceType.MEMORIAL
            "네이버 메일" -> ServiceType.BUSINESS
            else -> ServiceType.OTHER
        }
}
