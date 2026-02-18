package com.kuit.afternote.core.ui.component.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterBlockItem
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem

@Composable
fun TimeLetterBlockList(
    modifier: Modifier = Modifier,
    timeLetterItemList: List<TimeLetterItem>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemSpacing: Dp = 16.dp,
    onItemClick: (TimeLetterItem) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
        contentPadding = contentPadding
    ) {
        items(
            items = timeLetterItemList,
            key = { it.id }
        ) { letter ->
            TimeLetterBlockItem(
                timeLetter = letter,
                onClick = { onItemClick(letter) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeLetterBlockListPreview() {
    val mockLetters = listOf(
        TimeLetterItem(
            id = "1",
            receivername = "박채연",
            sendDate = "2027. 11. 24",
            title = "채연아 20번째 생일을 축하해",
            content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
            imageResId = R.drawable.ic_test_block,
            theme = LetterTheme.PEACH,
            createDate = "2026.11.24"
        ),
        TimeLetterItem(
            id = "2",
            receivername = "김민수",
            sendDate = "2026. 05. 10",
            title = "졸업 축하해 친구야",
            content = "드디어 졸업이구나! 우리가 함께한 시간들이 정말 소중했어. 앞으로도 좋은 일만 가득하길...",
            imageResId = null,
            theme = LetterTheme.BLUE,
            createDate = "2026.11.24"
        )
    )

    TimeLetterBlockList(
        timeLetterItemList = mockLetters,
        contentPadding = PaddingValues(vertical = 16.dp)
    )
}
