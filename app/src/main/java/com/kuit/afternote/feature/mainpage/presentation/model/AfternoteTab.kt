package com.kuit.afternote.feature.mainpage.presentation.model

/**
 * 애프터노트 탭 카테고리
 */
enum class AfternoteTab(
    val label: String
) {
    ALL("전체"),
    SOCIAL_NETWORK("소셜네트워크"),
    BUSINESS("비즈니스"),
    GALLERY_AND_FILES("갤러리 및 파일"),
    ASSET_MANAGEMENT("재산 처리"),
    MEMORIAL("추모 가이드라인")
}
