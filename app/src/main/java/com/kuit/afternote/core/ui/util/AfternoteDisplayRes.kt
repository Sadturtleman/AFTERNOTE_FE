package com.kuit.afternote.core.ui.util

import com.kuit.afternote.R
import com.kuit.afternote.core.domain.model.AfternoteServiceCatalog
import com.kuit.afternote.core.domain.model.AfternoteServiceType

/**
 * Single source of truth for afternote type → display resources (label string, icon).
 * Same mapping for writer and receiver. Writer passes [AfternoteServiceType.name]; receiver passes API sourceType.
 *
 * @param typeKey Writer: e.g. SOCIAL_NETWORK, GALLERY_AND_FILES, MEMORIAL. Receiver: e.g. INSTAGRAM, GALLERY, GUIDE, NAVER_MAIL.
 * @return Pair(stringResId, drawableResId). Use [stringResource](first) for label.
 */
fun getAfternoteDisplayRes(typeKey: String): Pair<Int, Int> {
    val (stringResId, displayKey) = TYPE_KEY_TO_STRING_AND_DISPLAY_KEY[typeKey]
        ?: (R.string.afternote_category_social_network to typeKey)
    val drawableResId = DISPLAY_KEY_TO_DRAWABLE[displayKey] ?: R.drawable.img_logo
    return stringResId to drawableResId
}

private data class DisplayEntry(
    val typeKey: String,
    val displayKey: String,
    val stringResId: Int,
    val drawableResId: Int
)

/**
 * Single list: each service/category appears once. Both lookup maps are derived from this.
 */
private val DISPLAY_ENTRIES: List<DisplayEntry> =
    listOf(
        DisplayEntry("SOCIAL_NETWORK", "SOCIAL_NETWORK", R.string.afternote_category_social_network, R.drawable.ic_social_pattern),
        DisplayEntry("GALLERY_AND_FILES", "GALLERY_AND_FILES", R.string.receiver_afternote_item_gallery, R.drawable.ic_gallery_pattern),
        DisplayEntry("MEMORIAL", "MEMORIAL", R.string.receiver_afternote_item_memorial_guideline, R.drawable.ic_memorial_guideline),
        DisplayEntry("INSTAGRAM", "인스타그램", R.string.receiver_afternote_item_instagram, R.drawable.img_insta_pattern),
        DisplayEntry("GALLERY", "갤러리", R.string.receiver_afternote_item_gallery, R.drawable.ic_gallery_pattern),
        DisplayEntry("GUIDE", "추모 가이드라인", R.string.receiver_afternote_item_memorial_guideline, R.drawable.ic_memorial_guideline),
        DisplayEntry("NAVER_MAIL", "네이버 메일", R.string.receiver_afternote_item_naver_mail, R.drawable.img_naver_mail_pattern),
        DisplayEntry("FACEBOOK", "페이스북", R.string.afternote_category_social_network, R.drawable.img_facebook_pattern),
        DisplayEntry("X", "X", R.string.afternote_category_social_network, R.drawable.img_x_pattern),
        DisplayEntry("THREAD", "스레드", R.string.afternote_category_social_network, R.drawable.img_thread_pattern),
        DisplayEntry("TIKTOK", "틱톡", R.string.afternote_category_social_network, R.drawable.img_tiktok_pattern),
        DisplayEntry("YOUTUBE", "유튜브", R.string.afternote_category_social_network, R.drawable.img_youtube_pattern),
        DisplayEntry("KAKAOTALK", "카카오톡", R.string.afternote_category_social_network, R.drawable.img_kakaotalk_pattern),
        DisplayEntry("KAKAOSTORY", "카카오스토리", R.string.afternote_category_social_network, R.drawable.img_kakaostory_pattern),
        DisplayEntry("NAVER_BLOG", "네이버 블로그", R.string.afternote_category_social_network, R.drawable.img_naverblog_pattern),
        DisplayEntry("NAVER_CAFE", "네이버 카페", R.string.afternote_category_social_network, R.drawable.img_navercafe_pattern),
        DisplayEntry("NAVER_BAND", "네이버 밴드", R.string.afternote_category_social_network, R.drawable.img_naverband_pattern),
        DisplayEntry("DISCORD", "디스코드", R.string.afternote_category_social_network, R.drawable.img_discord_pattern),
        DisplayEntry("GOOGLE_PHOTO", "구글 포토", R.string.receiver_afternote_item_gallery, R.drawable.img_googlephoto_pattern),
        DisplayEntry("MYBOX", "네이버 MYBOX", R.string.receiver_afternote_item_gallery, R.drawable.img_mybox_pattern),
        DisplayEntry("ICLOUD", "아이클라우드", R.string.receiver_afternote_item_gallery, R.drawable.img_icloud_pattern),
        DisplayEntry("ONEDRIVE", "Onedrive", R.string.receiver_afternote_item_gallery, R.drawable.img_onedrive_pattern),
        DisplayEntry("TALKDRIVE", "카카오톡 톡서랍", R.string.receiver_afternote_item_gallery, R.drawable.img_talkdrive_pattern),
        // Writer-only: displayKey used by getIconResForServiceName; no receiver typeKey
        DisplayEntry("_", "파일", R.string.receiver_afternote_item_gallery, R.drawable.ic_gallery_pattern)
    )

private val DISPLAY_KEY_TO_DRAWABLE: Map<String, Int> =
    DISPLAY_ENTRIES.associate { it.displayKey to it.drawableResId }

private val TYPE_KEY_TO_STRING_AND_DISPLAY_KEY: Map<String, Pair<Int, String>> =
    DISPLAY_ENTRIES
        .filter { it.typeKey != "_" }
        .associate { it.typeKey to Pair(it.stringResId, it.displayKey) }

/**
 * Icon drawable res for an [AfternoteServiceType]. Same mapping as [getAfternoteDisplayRes]; use when you have [AfternoteServiceType].
 */
fun getIconResForServiceType(serviceType: AfternoteServiceType): Int =
    getAfternoteDisplayRes(serviceType.name).second

/**
 * Icon drawable res for a specific afternote service by its display name (e.g. "인스타그램", "갤러리").
 * Uses the same [DISPLAY_KEY_TO_DRAWABLE] as receiver typeKey lookups.
 * Falls back to category icon when the service has no dedicated pattern or name is unknown.
 */
fun getIconResForServiceName(serviceName: String): Int =
    DISPLAY_KEY_TO_DRAWABLE[serviceName]
        ?: getIconResForServiceType(AfternoteServiceCatalog.serviceTypeFor(serviceName))

