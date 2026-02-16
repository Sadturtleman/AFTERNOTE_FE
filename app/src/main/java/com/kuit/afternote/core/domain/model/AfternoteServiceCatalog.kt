package com.kuit.afternote.core.domain.model

/**
 * Single source of truth for afternote service names by category.
 * Used for edit-screen dropdowns and for inferring [AfternoteServiceType] from service name
 * (e.g. dummy/legacy data). Shared by writer and receiver flows.
 */
object AfternoteServiceCatalog {
    /** Display names for 소셜 네트워크 (SOCIAL) category. */
    val socialServices: List<String> =
        listOf(
            "인스타그램",
            "페이스북",
            "X",
            "스레드",
            "틱톡",
            "유튜브",
            "카카오톡",
            "카카오스토리",
            "네이버 블로그",
            "네이버 카페",
            "네이버 밴드",
            "디스코드"
        )

    /** Display names for 갤러리 및 파일 (GALLERY) category. */
    val galleryServices: List<String> =
        listOf(
            "갤러리",
            "파일",
            "구글 포토",
            "네이버 MYBOX",
            "아이클라우드",
            "Onedrive",
            "카카오톡 톡서랍"
        )

    private const val MEMORIAL_SERVICE_NAME = "추모 가이드라인"

    /**
     * Infers [AfternoteServiceType] from a service display name.
     * Only for dummy/legacy data; when type is known from API or list item, use that instead.
     */
    fun serviceTypeFor(serviceName: String): AfternoteServiceType =
        when {
            galleryServices.contains(serviceName) -> AfternoteServiceType.GALLERY_AND_FILES
            serviceName == MEMORIAL_SERVICE_NAME -> AfternoteServiceType.MEMORIAL
            else -> AfternoteServiceType.SOCIAL_NETWORK
        }

    /** Default service when social category is selected (first in list). */
    val defaultSocialService: String
        get() = socialServices.first()

    /** Default service when gallery category is selected (first in list). */
    val defaultGalleryService: String
        get() = galleryServices.first()
}
