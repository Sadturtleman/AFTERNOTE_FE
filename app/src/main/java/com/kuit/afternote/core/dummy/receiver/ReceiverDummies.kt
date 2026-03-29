package com.kuit.afternote.core.dummy.receiver

import com.kuit.afternote.R

/**
 * Core-only seeds for receiver-related lists (no feature UI types).
 * Use [serviceNameResId] when non-null (resolve with stringResource); otherwise [serviceNameLiteral].
 */
data class AfternoteListItemSeed(
    val id: String,
    val serviceNameResId: Int?,
    val serviceNameLiteral: String?,
    val date: String,
    val iconResId: Int,
)

data class ReceiverDummyDetail(
    val receiverId: String,
    val name: String,
    val relationship: String,
    val phoneNumber: String,
    val email: String,
    val dailyQuestionCount: Int,
    val timeLetterCount: Int,
    val afternoteCount: Int,
)

/**
 * Shared dummy metadata for receiver flows (counts, list seeds). Rows typed to a specific
 * feature screen (e.g. edit-receiver list) live in that feature’s dummy package.
 */
object ReceiverDummies {
    private val receiverDetails: Map<String, ReceiverDummyDetail> =
        mapOf(
            "receiver_1" to
                ReceiverDummyDetail(
                    receiverId = "receiver_1",
                    name = "김지은",
                    relationship = "딸",
                    phoneNumber = "010-1234-1234",
                    email = "jieun01@naver.com",
                    dailyQuestionCount = 8,
                    timeLetterCount = 12,
                    afternoteCount = 4,
                ),
            "receiver_2" to
                ReceiverDummyDetail(
                    receiverId = "receiver_2",
                    name = "김혜성",
                    relationship = "아들",
                    phoneNumber = "010-9876-5432",
                    email = "hyeseong02@gmail.com",
                    dailyQuestionCount = 1,
                    timeLetterCount = 0,
                    afternoteCount = 2,
                ),
            "receiver_3" to
                ReceiverDummyDetail(
                    receiverId = "receiver_3",
                    name = "박서연",
                    relationship = "조카",
                    phoneNumber = "010-4567-8901",
                    email = "seoyeon03@kakao.com",
                    dailyQuestionCount = 0,
                    timeLetterCount = 3,
                    afternoteCount = 0,
                ),
            "receiver_4" to
                ReceiverDummyDetail(
                    receiverId = "receiver_4",
                    name = "황은주",
                    relationship = "언니",
                    phoneNumber = "010-0000-0000",
                    email = "eunju04@outlook.com",
                    dailyQuestionCount = 21,
                    timeLetterCount = 6,
                    afternoteCount = 10,
                ),
            "receiver_5" to
                ReceiverDummyDetail(
                    receiverId = "receiver_5",
                    name = "황은경",
                    relationship = "동생",
                    phoneNumber = "010-1111-2222",
                    email = "eunkyung05@naver.com",
                    dailyQuestionCount = 5,
                    timeLetterCount = 2,
                    afternoteCount = 1,
                ),
        )

    fun detailOf(receiverId: String): ReceiverDummyDetail =
        receiverDetails[receiverId] ?: receiverDetails.getValue("receiver_1")

    /** Default receiver name for receiver afternote main / preview. */
    fun defaultReceiverTitle(): String = "박서연"

    /**
     * Seeds for receiver afternote list (NavGraph "receiver_afternote_list").
     * Replace with API load when backend is ready.
     */
    fun defaultAfternoteListSeedsForReceiverList(): List<AfternoteListItemSeed> =
        listOf(
            AfternoteListItemSeed(
                id = "1",
                serviceNameResId = null,
                serviceNameLiteral = "추모 가이드라인",
                date = "2025.12.01",
                iconResId = R.drawable.img_logo,
            ),
            AfternoteListItemSeed(
                id = "2",
                serviceNameResId = null,
                serviceNameLiteral = "갤러리",
                date = "2025.12.02",
                iconResId = R.drawable.img_insta_pattern,
            ),
            AfternoteListItemSeed(
                id = "3",
                serviceNameResId = null,
                serviceNameLiteral = "인스타그램",
                date = "2025.12.03",
                iconResId = R.drawable.img_insta_pattern,
            ),
        )

    /**
     * Seeds for receiver afternote detail list (Setting > Receiver > 애프터노트 목록).
     * Replace with API load by receiverId when backend is ready.
     */
    fun defaultAfternoteListSeedsForReceiverDetail(): List<AfternoteListItemSeed> =
        listOf(
            AfternoteListItemSeed(
                id = "instagram",
                serviceNameResId = R.string.receiver_afternote_item_instagram,
                serviceNameLiteral = null,
                date = "2026.2.6",
                iconResId = R.drawable.img_insta_pattern,
            ),
            AfternoteListItemSeed(
                id = "gallery",
                serviceNameResId = R.string.receiver_afternote_item_gallery,
                serviceNameLiteral = null,
                date = "2026.2.7",
                iconResId = R.drawable.ic_gallery_pattern,
            ),
            AfternoteListItemSeed(
                id = "memorial_guideline",
                serviceNameResId = R.string.receiver_afternote_item_memorial_guideline,
                serviceNameLiteral = null,
                date = "2026.2.8",
                iconResId = R.drawable.ic_memorial_guideline,
            ),
            AfternoteListItemSeed(
                id = "naver_mail",
                serviceNameResId = R.string.receiver_afternote_item_naver_mail,
                serviceNameLiteral = null,
                date = "2026.2.9",
                iconResId = R.drawable.img_naver_mail_pattern,
            ),
        )
}
