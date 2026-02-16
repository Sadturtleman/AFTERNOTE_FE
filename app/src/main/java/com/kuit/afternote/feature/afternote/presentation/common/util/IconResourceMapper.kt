package com.kuit.afternote.feature.afternote.presentation.common.util

import com.kuit.afternote.R
import com.kuit.afternote.feature.afternote.domain.model.ServiceType

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
        }

}
