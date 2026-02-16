package com.kuit.afternote.core.ui.util

import com.kuit.afternote.R
import com.kuit.afternote.core.domain.model.AfternoteServiceType

/**
 * Single source of truth for afternote type → display resources (label string, icon).
 * Same mapping for writer and receiver. Writer passes [AfternoteServiceType.name]; receiver passes API sourceType.
 *
 * @param typeKey Writer: e.g. SOCIAL_NETWORK, GALLERY_AND_FILES, MEMORIAL. Receiver: e.g. INSTAGRAM, GALLERY, GUIDE, NAVER_MAIL.
 * @return Pair(stringResId, drawableResId). Use [stringResource](first) for label.
 */
fun getAfternoteDisplayRes(typeKey: String): Pair<Int, Int> =
    when (typeKey) {
        "SOCIAL_NETWORK" -> R.string.afternote_category_social_network to R.drawable.ic_social_pattern
        "GALLERY_AND_FILES" -> R.string.receiver_afternote_item_gallery to R.drawable.ic_gallery_pattern
        "MEMORIAL" -> R.string.receiver_afternote_item_memorial_guideline to R.drawable.ic_memorial_guideline
        "INSTAGRAM" -> R.string.receiver_afternote_item_instagram to R.drawable.img_insta_pattern
        "GALLERY" -> R.string.receiver_afternote_item_gallery to R.drawable.ic_gallery_pattern
        "GUIDE" -> R.string.receiver_afternote_item_memorial_guideline to R.drawable.ic_memorial_guideline
        "NAVER_MAIL" -> R.string.receiver_afternote_item_naver_mail to R.drawable.img_naver_mail_pattern
        else -> R.string.afternote_category_social_network to R.drawable.img_logo
    }

/**
 * Icon drawable res for an [AfternoteServiceType]. Same mapping as [getAfternoteDisplayRes]; use when you have [AfternoteServiceType].
 */
fun getIconResForServiceType(serviceType: AfternoteServiceType): Int =
    getAfternoteDisplayRes(serviceType.name).second
