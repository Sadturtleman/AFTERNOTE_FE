package com.kuit.afternote.feature.timeletter.presentation.dummy

import com.kuit.afternote.R
import com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem

/** Preview / fake-provider sample letters (lives in timeletter feature, not core). */
object TimeLetterPreviewDummies {
    val sampleItemsForPreview: List<TimeLetterItem> =
        listOf(
            TimeLetterItem(
                id = "1",
                receivername = "박채연",
                sendDate = "2027. 11. 24",
                title = "채연아 20번째 생일을 축하해",
                content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
                imageResId = R.drawable.ic_test_block,
                theme = LetterTheme.PEACH,
                createDate = "2026.11.24",
            ),
            TimeLetterItem(
                id = "2",
                receivername = "김민수",
                sendDate = "2026. 05. 10",
                title = "졸업 축하해 친구야",
                content = "드디어 졸업이구나! 우리가 함께한 시간들이 정말 소중했어. 앞으로도 좋은 일만 가득하길...",
                imageResId = null,
                theme = LetterTheme.BLUE,
                createDate = "2026.11.24",
            ),
            TimeLetterItem(
                id = "3",
                receivername = "이지은",
                sendDate = "2028. 01. 01",
                title = "새해 복 많이 받아",
                content = "새해가 밝았어! 올해도 건강하고 행복하게 보내길 바라. 사랑해!",
                imageResId = R.drawable.ic_test_block,
                theme = LetterTheme.YELLOW,
                createDate = "2026.11.24",
            ),
            TimeLetterItem(
                id = "4",
                receivername = "홍길동",
                sendDate = "2029. 03. 15",
                title = "오랜만이야 친구",
                content = "정말 오랜만이다! 요즘 어떻게 지내? 다음에 시간 되면 같이 밥 먹자.",
                imageResId = null,
                theme = LetterTheme.PEACH,
                createDate = "2026.11.24",
            ),
        )
}
