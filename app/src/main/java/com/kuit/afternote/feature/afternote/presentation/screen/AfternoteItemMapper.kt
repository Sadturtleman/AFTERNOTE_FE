package com.kuit.afternote.feature.afternote.presentation.screen

import com.kuit.afternote.core.domain.model.AfternoteServiceCatalog
import com.kuit.afternote.core.domain.model.AfternoteServiceType
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.AfternoteProcessingMethod

/**
 * Pair<String, String> 및 RegisterAfternotePayload를 AfternoteItem으로 변환하는 매퍼
 * 레거시 데이터 형식과의 호환성을 위한 헬퍼
 */
object AfternoteItemMapper {

    /**
     * List<Pair>를 List<AfternoteItem>으로 변환하며 **안정적인 id**를 부여.
     * 목록/상세/편집에서 동일한 id로 조회할 수 있도록 더미 목록용.
     */
    fun toAfternoteItemsWithStableIds(pairs: List<Pair<String, String>>): List<AfternoteItem> =
        pairs.mapIndexed { index, pair ->
            val (serviceName, date) = pair
            val serviceType = inferServiceType(serviceName)
            val dummyData = dummyDataForServiceType(serviceType)
            AfternoteItem(
                id = "dummy_${serviceName}_${date}_$index",
                serviceName = serviceName,
                date = date,
                type = serviceType,
                accountId = dummyData.accountId,
                password = dummyData.password,
                message = dummyData.message,
                accountProcessingMethod = dummyData.accountProcessingMethod,
                informationProcessingMethod = dummyData.informationProcessingMethod,
                processingMethods = dummyData.processingMethods,
                galleryProcessingMethods = dummyData.galleryProcessingMethods
            )
        }

    /**
     * Edit screen category dropdown value from [AfternoteServiceType].
     * Prefer this when type is known; do not infer category from title (titles are user-defined).
     */
    fun categoryStringForEditScreen(serviceType: AfternoteServiceType): String =
        when (serviceType) {
            AfternoteServiceType.SOCIAL_NETWORK -> "소셜네트워크"
            AfternoteServiceType.GALLERY_AND_FILES -> "갤러리 및 파일"
            AfternoteServiceType.MEMORIAL -> "추모 가이드라인"
        }

    /**
     * 서비스명으로부터 AfternoteServiceType 추론.
     * Only for dummy/legacy data (e.g. toAfternoteItemsWithStableIds pairs). Do not use for
     * user-defined titles; category/type must come from API or list item when available.
     */
    private fun inferServiceType(serviceName: String): AfternoteServiceType =
        AfternoteServiceCatalog.serviceTypeFor(serviceName)

    /**
     * 더미 데이터: 각 Detail 화면에 하드코딩된 값과 동일하게 맞춤.
     *
     * SocialNetworkDetailScreen: id="qwerty123", pw="qwerty123", method=추모 계정,
     *   processing=["게시물 내리기","추모 게시물 올리기","추모 계정으로 전환하기"],
     *   message="이 계정에는 우리 가족 여행 사진이 많아.…"
     *
     * GalleryDetailScreen: infoMethod=TRANSFER_TO_ADDITIONAL_AFTERNOTE_EDIT_RECEIVER,
     *   galleryProcessing=["'엽사' 폴더 박선호에게 전송","'흑역사' 폴더 삭제"],
     *   message="" (없음)
     */
    private fun defaultSocialOrBusinessDummyFields(): DummyItemFields =
        DummyItemFields(
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

    private fun dummyDataForServiceType(type: AfternoteServiceType): DummyItemFields =
        when (type) {
            AfternoteServiceType.SOCIAL_NETWORK -> defaultSocialOrBusinessDummyFields()
            AfternoteServiceType.GALLERY_AND_FILES -> DummyItemFields(
                informationProcessingMethod = "TRANSFER_TO_ADDITIONAL_AFTERNOTE_EDIT_RECEIVER",
                galleryProcessingMethods = listOf(
                    AfternoteProcessingMethod("1", "'엽사' 폴더 박선호에게 전송"),
                    AfternoteProcessingMethod("2", "'흑역사' 폴더 삭제")
                )
            )
            AfternoteServiceType.MEMORIAL -> DummyItemFields()
        }

    private data class DummyItemFields(
        val accountId: String = "",
        val password: String = "",
        val message: String = "",
        val accountProcessingMethod: String = "",
        val informationProcessingMethod: String = "",
        val processingMethods: List<AfternoteProcessingMethod> = emptyList(),
        val galleryProcessingMethods: List<AfternoteProcessingMethod> = emptyList()
    )
}
