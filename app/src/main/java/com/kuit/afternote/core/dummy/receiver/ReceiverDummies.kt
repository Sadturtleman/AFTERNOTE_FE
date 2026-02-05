package com.kuit.afternote.core.dummy.receiver

import com.kuit.afternote.R
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiver
import com.kuit.afternote.feature.setting.presentation.screen.dailyanswer.DailyAnswerItemUiModel
import com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem

/**
 * Seed for building [com.kuit.afternote.core.uimodel.AfternoteListDisplayItem].
 * Use [serviceNameResId] when non-null (resolve with stringResource); otherwise [serviceNameLiteral].
 */
data class AfternoteListItemSeed(
    val id: String,
    val serviceNameResId: Int?,
    val serviceNameLiteral: String?,
    val date: String,
    val iconResId: Int
)

data class ReceiverDummyDetail(
    val receiverId: String,
    val name: String,
    val relationship: String,
    val phoneNumber: String,
    val email: String,
    val dailyQuestionCount: Int,
    val timeLetterCount: Int,
    val afternoteCount: Int
)

/**
 * Centralized dummy data for receiver-related screens (list, detail, time letters, daily answers).
 * Replace with API load when backend is ready.
 */
object ReceiverDummies {
    val receiverList: List<MainPageEditReceiver> = listOf(
        MainPageEditReceiver(id = "receiver_1", name = "김지은", label = "딸"),
        MainPageEditReceiver(id = "receiver_2", name = "김혜성", label = "아들"),
        MainPageEditReceiver(id = "receiver_3", name = "박서연", label = "조카"),
        MainPageEditReceiver(id = "receiver_4", name = "황은주", label = "언니"),
        MainPageEditReceiver(id = "receiver_5", name = "황은경", label = "동생")
    )

    private val receiverDetails: Map<String, ReceiverDummyDetail> = mapOf(
        "receiver_1" to ReceiverDummyDetail(
            receiverId = "receiver_1",
            name = "김지은",
            relationship = "딸",
            phoneNumber = "010-1234-1234",
            email = "jieun01@naver.com",
            dailyQuestionCount = 8,
            timeLetterCount = 12,
            afternoteCount = 4
        ),
        "receiver_2" to ReceiverDummyDetail(
            receiverId = "receiver_2",
            name = "김혜성",
            relationship = "아들",
            phoneNumber = "010-9876-5432",
            email = "hyeseong02@gmail.com",
            dailyQuestionCount = 1,
            timeLetterCount = 0,
            afternoteCount = 2
        ),
        "receiver_3" to ReceiverDummyDetail(
            receiverId = "receiver_3",
            name = "박서연",
            relationship = "조카",
            phoneNumber = "010-4567-8901",
            email = "seoyeon03@kakao.com",
            dailyQuestionCount = 0,
            timeLetterCount = 3,
            afternoteCount = 0
        ),
        "receiver_4" to ReceiverDummyDetail(
            receiverId = "receiver_4",
            name = "황은주",
            relationship = "언니",
            phoneNumber = "010-0000-0000",
            email = "eunju04@outlook.com",
            dailyQuestionCount = 21,
            timeLetterCount = 6,
            afternoteCount = 10
        ),
        "receiver_5" to ReceiverDummyDetail(
            receiverId = "receiver_5",
            name = "황은경",
            relationship = "동생",
            phoneNumber = "010-1111-2222",
            email = "eunkyung05@naver.com",
            dailyQuestionCount = 5,
            timeLetterCount = 2,
            afternoteCount = 1
        )
    )

    fun detailOf(receiverId: String): ReceiverDummyDetail =
        receiverDetails[receiverId] ?: receiverDetails.getValue("receiver_1")

    /** Default receiver name for dev "receiver afternote main" screen. */
    fun defaultReceiverTitleForDev(): String = "박서연"

    /**
     * Seeds for receiver afternote list (dev mode, NavGraph "receiver_afternote_list").
     * Replace with API load when backend is ready.
     */
    fun defaultAfternoteListSeedsForReceiverList(): List<AfternoteListItemSeed> =
        listOf(
            AfternoteListItemSeed(
                id = "1",
                serviceNameResId = null,
                serviceNameLiteral = "추모 가이드라인",
                date = "2025.12.01",
                iconResId = R.drawable.img_logo
            ),
            AfternoteListItemSeed(
                id = "2",
                serviceNameResId = null,
                serviceNameLiteral = "갤러리",
                date = "2025.12.02",
                iconResId = R.drawable.img_insta_pattern
            ),
            AfternoteListItemSeed(
                id = "3",
                serviceNameResId = null,
                serviceNameLiteral = "인스타그램",
                date = "2025.12.03",
                iconResId = R.drawable.img_insta_pattern
            )
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
                date = "2025.11.26",
                iconResId = R.drawable.img_insta_pattern
            ),
            AfternoteListItemSeed(
                id = "gallery",
                serviceNameResId = R.string.receiver_afternote_item_gallery,
                serviceNameLiteral = null,
                date = "2025.11.26",
                iconResId = R.drawable.ic_gallery
            ),
            AfternoteListItemSeed(
                id = "memorial_guideline",
                serviceNameResId = R.string.receiver_afternote_item_memorial_guideline,
                serviceNameLiteral = null,
                date = "2025.11.26",
                iconResId = R.drawable.ic_memorial_guideline
            ),
            AfternoteListItemSeed(
                id = "naver_mail",
                serviceNameResId = R.string.receiver_afternote_item_naver_mail,
                serviceNameLiteral = null,
                date = "2025.11.26",
                iconResId = R.drawable.img_naver_mail
            )
        )

    /**
     * Dummy time letter items for a receiver. Replace with API load by receiverId when backend is ready.
     */
    fun defaultTimeLetterItems(receiverId: String): List<TimeLetterItem> {
        val detail = detailOf(receiverId)
        return List(detail.timeLetterCount.coerceAtLeast(1)) { index ->
            TimeLetterItem(
                id = "timeletter_$index",
                receivername = detail.name,
                sendDate = "2027. 11. 24",
                title = "채연아 20번째 생일을 축하해",
                content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
                imageResId = if (index == 0) R.drawable.ic_test_block else null,
                theme = when (index % 3) {
                    0 -> LetterTheme.BLUE
                    1 -> LetterTheme.YELLOW
                    else -> LetterTheme.PEACH
                }
            )
        }
    }

    /**
     * Dummy daily answer items for a receiver. Replace with API load by receiverId when backend is ready.
     */
    fun dailyAnswerItems(
        receiverId: String,
        question: String,
        answer: String,
        dateText: String
    ): List<DailyAnswerItemUiModel> {
        val count = detailOf(receiverId).dailyQuestionCount
        return List(count) {
            DailyAnswerItemUiModel(question = question, answer = answer, dateText = dateText)
        }
    }

    /**
     * Sample time letter items for Previews. Shared so Previews and tests use the same data.
     */
    val sampleTimeLetterItemsForPreview: List<TimeLetterItem> =
        listOf(
            TimeLetterItem(
                id = "1",
                receivername = "박채연",
                sendDate = "2027. 11. 24",
                title = "채연아 20번째 생일을 축하해",
                content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
                imageResId = R.drawable.ic_test_block,
                theme = LetterTheme.PEACH
            ),
            TimeLetterItem(
                id = "2",
                receivername = "김민수",
                sendDate = "2026. 05. 10",
                title = "졸업 축하해 친구야",
                content = "드디어 졸업이구나! 우리가 함께한 시간들이 정말 소중했어. 앞으로도 좋은 일만 가득하길...",
                imageResId = null,
                theme = LetterTheme.BLUE
            ),
            TimeLetterItem(
                id = "3",
                receivername = "이지은",
                sendDate = "2028. 01. 01",
                title = "새해 복 많이 받아",
                content = "새해가 밝았어! 올해도 건강하고 행복하게 보내길 바라. 사랑해!",
                imageResId = R.drawable.ic_test_block,
                theme = LetterTheme.YELLOW
            ),
            TimeLetterItem(
                id = "4",
                receivername = "홍길동",
                sendDate = "2029. 03. 15",
                title = "오랜만이야 친구",
                content = "정말 오랜만이다! 요즘 어떻게 지내? 다음에 시간 되면 같이 밥 먹자.",
                imageResId = null,
                theme = LetterTheme.PEACH
            )
        )
}
