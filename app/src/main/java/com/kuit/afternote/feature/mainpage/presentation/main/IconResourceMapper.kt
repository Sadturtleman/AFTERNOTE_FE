package com.kuit.afternote.feature.mainpage.presentation.main

import com.kuit.afternote.R
import com.kuit.afternote.feature.mainpage.domain.model.ServiceType

/**
 * ServiceType에 따라 적절한 아이콘 리소스를 반환하는 매퍼
 * UI 레벨에서 리소스 매핑을 담당
 */
object IconResourceMapper {
    /**
     * ServiceType에 따라 아이콘 리소스 반환
     */
    fun getIconResForServiceType(serviceType: ServiceType): Int =
        when (serviceType) {
            ServiceType.SOCIAL_NETWORK -> R.drawable.img_insta_pattern
            ServiceType.GALLERY_AND_FILES -> R.drawable.ic_gallery
            ServiceType.MEMORIAL -> R.drawable.ic_memorial_guideline
            ServiceType.BUSINESS -> R.drawable.img_naver_mail
            ServiceType.ASSET_MANAGEMENT -> R.drawable.img_logo
            ServiceType.OTHER -> R.drawable.img_logo
        }

    /**
     * 서비스명에 따라 ServiceType을 추론하고 아이콘 리소스 반환
     * 레거시 호환성을 위한 헬퍼 함수
     */
    fun getIconResForServiceName(serviceName: String): Int {
        val serviceType = when (serviceName) {
            "인스타그램", "페이스북", "트위터", "카카오톡", "네이버" -> ServiceType.SOCIAL_NETWORK
            "갤러리", "파일" -> ServiceType.GALLERY_AND_FILES
            "추모 가이드라인" -> ServiceType.MEMORIAL
            "네이버 메일" -> ServiceType.BUSINESS
            else -> ServiceType.OTHER
        }
        return getIconResForServiceType(serviceType)
    }
}
