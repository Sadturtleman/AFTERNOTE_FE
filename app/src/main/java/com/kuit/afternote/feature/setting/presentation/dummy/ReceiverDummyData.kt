package com.kuit.afternote.feature.setting.presentation.dummy

import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiver

internal data class ReceiverDummyDetail(
    val receiverId: String,
    val name: String,
    val relationship: String,
    val phoneNumber: String,
    val email: String,
    val dailyQuestionCount: Int,
    val timeLetterCount: Int,
    val afternoteCount: Int
)

internal object ReceiverDummyData {
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

    fun detailOf(receiverId: String): ReceiverDummyDetail {
        return receiverDetails[receiverId] ?: receiverDetails.getValue("receiver_1")
    }
}
