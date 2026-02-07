package com.kuit.afternote.feature.afternote.presentation.screen

import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.AfternoteProcessingMethod
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
            accountId = payload.accountId,
            password = payload.password,
            message = payload.message,
            accountProcessingMethod = payload.accountProcessingMethod,
            informationProcessingMethod = payload.informationProcessingMethod,
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
            val devData = devDataForServiceType(serviceType)
            AfternoteItem(
                id = "dev_${serviceName}_${date}_$index",
                serviceName = serviceName,
                date = date,
                type = serviceType,
                accountId = devData.accountId,
                password = devData.password,
                message = devData.message,
                accountProcessingMethod = devData.accountProcessingMethod,
                informationProcessingMethod = devData.informationProcessingMethod,
                processingMethods = devData.processingMethods,
                galleryProcessingMethods = devData.galleryProcessingMethods
            )
        }

    /**
     * Edit screen category dropdown value from service name.
     * Use when loading existing item so selectedCategory matches the dropdown options.
     */
    fun categoryStringForEditScreen(serviceName: String): String =
        when (inferServiceType(serviceName)) {
            ServiceType.SOCIAL_NETWORK, ServiceType.OTHER -> "소셜네트워크"
            ServiceType.BUSINESS -> "비즈니스"
            ServiceType.GALLERY_AND_FILES -> "갤러리 및 파일"
            ServiceType.ASSET_MANAGEMENT -> "재산 처리"
            ServiceType.MEMORIAL -> "추모 가이드라인"
        }

    /**
     * 서비스명으로부터 ServiceType 추론 (단일 소스; 목록/편집 공용)
     */
    private fun inferServiceType(serviceName: String): ServiceType =
        when (serviceName) {
            "갤러리", "파일" -> ServiceType.GALLERY_AND_FILES
            "인스타그램", "페이스북", "X", "스레드", "틱톡", "유튜브",
            "카카오톡", "카카오스토리", "네이버 블로그", "네이버 카페", "네이버 밴드",
            "네이버", "디스코드" -> ServiceType.SOCIAL_NETWORK
            "추모 가이드라인" -> ServiceType.MEMORIAL
            "네이버 메일" -> ServiceType.BUSINESS
            else -> ServiceType.OTHER
        }

    /**
     * Dev 더미 데이터: 각 Detail 화면에 하드코딩된 값과 동일하게 맞춤.
     *
     * SocialNetworkDetailScreen: id="qwerty123", pw="qwerty123", method=추모 계정,
     *   processing=["게시물 내리기","추모 게시물 올리기","추모 계정으로 전환하기"],
     *   message="이 계정에는 우리 가족 여행 사진이 많아.…"
     *
     * GalleryDetailScreen: infoMethod=TRANSFER_TO_ADDITIONAL_AFTERNOTE_EDIT_RECEIVER,
     *   galleryProcessing=["'엽사' 폴더 박선호에게 전송","'흑역사' 폴더 삭제"],
     *   message="" (없음)
     */
    private fun defaultSocialOrBusinessDevFields(): DevItemFields =
        DevItemFields(
            accountId = "qwerty123",
            password = "qwerty123",
            message = "이 계정에는 우리 가족 여행 사진이 많아.\n" +
                "계정 삭제하지 말고 꼭 추모 계정으로 남겨줘!",
            accountProcessingMethod = "MEMORIAL_ACCOUNT",
            processingMethods = listOf(
                AfternoteProcessingMethod("1", "게시물 내리기"),
                AfternoteProcessingMethod("2", "추모 게시물 올리기"),
                AfternoteProcessingMethod("3", "추모 계정으로 전환하기")
            )
        )

    private fun devDataForServiceType(type: ServiceType): DevItemFields =
        when (type) {
            ServiceType.SOCIAL_NETWORK, ServiceType.OTHER, ServiceType.BUSINESS ->
                defaultSocialOrBusinessDevFields()
            ServiceType.GALLERY_AND_FILES -> DevItemFields(
                informationProcessingMethod = "TRANSFER_TO_ADDITIONAL_AFTERNOTE_EDIT_RECEIVER",
                galleryProcessingMethods = listOf(
                    AfternoteProcessingMethod("1", "'엽사' 폴더 박선호에게 전송"),
                    AfternoteProcessingMethod("2", "'흑역사' 폴더 삭제")
                )
            )
            ServiceType.MEMORIAL -> DevItemFields()
            ServiceType.ASSET_MANAGEMENT -> DevItemFields()
        }

    private data class DevItemFields(
        val accountId: String = "",
        val password: String = "",
        val message: String = "",
        val accountProcessingMethod: String = "",
        val informationProcessingMethod: String = "",
        val processingMethods: List<AfternoteProcessingMethod> = emptyList(),
        val galleryProcessingMethods: List<AfternoteProcessingMethod> = emptyList()
    )
}
