package com.kuit.afternote.feature.setting.presentation.screen.receiver

import com.kuit.afternote.R

/**
 * Maps API sourceType to display resource IDs for the receiver afternote list.
 * Caller should use [stringResource](first) when first is non-null, else use raw sourceType.
 *
 * @return Pair(stringResId or null, drawableResId)
 */
fun getAfternoteSourceDisplayResIds(sourceType: String): Pair<Int?, Int> =
    when (sourceType) {
        "INSTAGRAM" -> R.string.receiver_afternote_item_instagram to R.drawable.img_insta_pattern
        "GALLERY" -> R.string.receiver_afternote_item_gallery to R.drawable.ic_gallery
        "GUIDE" -> R.string.receiver_afternote_item_memorial_guideline to R.drawable.ic_memorial_guideline
        "NAVER_MAIL" -> R.string.receiver_afternote_item_naver_mail to R.drawable.img_naver_mail
        else -> null to R.drawable.img_logo
    }
