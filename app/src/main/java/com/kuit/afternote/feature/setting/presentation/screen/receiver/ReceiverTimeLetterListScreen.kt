package com.kuit.afternote.feature.setting.presentation.screen.receiver

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterBlockItem
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun ReceiverTimeLetterListScreen(
    modifier: Modifier = Modifier,
    receiverName: String,
    items: List<TimeLetterItem>,
    onBackClick: () -> Unit,
    onItemClick: (TimeLetterItem) -> Unit = {}
) {
    Scaffold(
        containerColor = Gray1,
        topBar = {
            TopBar(
                title = stringResource(R.string.daily_answer_title),
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item(key = "intro") {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillParentMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    ReceiverTimeLetterIntro(receiverName = receiverName)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            items(
                items = items,
                key = { item -> item.id }
            ) { item ->
                TimeLetterBlockItem(
                    receiverName = item.receivername,
                    sendDate = item.sendDate,
                    title = item.title,
                    content = item.content,
                    imageResId = item.imageResId,
                    theme = item.theme,
                    onClick = { onItemClick(item) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ReceiverTimeLetterIntro(
    modifier: Modifier = Modifier,
    receiverName: String
) {
    val introSuffix = stringResource(R.string.daily_answer_intro_line1_suffix)
    val introLine2 = stringResource(R.string.receiver_timeletter_intro_line2)

    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = B1)) {
                append(receiverName)
            }
            append(" ")
            append(introSuffix)
            append("\n")
            append(introLine2)
        },
        style = TextStyle(
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Bold,
            color = Black
        )
    )
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun ReceiverTimeLetterListScreenPreview() {
    AfternoteTheme {
        ReceiverTimeLetterListScreen(
            receiverName = "김지은",
            items = listOf(
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
                    receivername = "박채연",
                    sendDate = "2028. 11. 24",
                    title = "채연아 21번째 생일을 축하해",
                    content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..아빠가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
                    imageResId = null,
                    theme = LetterTheme.BLUE
                ),
                TimeLetterItem(
                    id = "3",
                    receivername = "박채연",
                    sendDate = "2029. 11. 24",
                    title = "채연아 22번째 생일을 축하해",
                    content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄빠가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
                    imageResId = null,
                    theme = LetterTheme.YELLOW
                )
            ),
            onBackClick = {},
            onItemClick = {}
        )
    }
}
