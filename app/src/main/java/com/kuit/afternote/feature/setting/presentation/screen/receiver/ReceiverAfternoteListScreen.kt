package com.kuit.afternote.feature.setting.presentation.screen.receiver

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
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
import com.kuit.afternote.core.ui.component.list.AfternoteListItem
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun ReceiverAfternoteListScreen(
    modifier: Modifier = Modifier,
    receiverName: String,
    items: List<AfternoteListDisplayItem>,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    BackHandler(onBack = onBackClick)
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(key = "intro") {
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    ReceiverAfternoteIntro(receiverName = receiverName)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            items(
                items = items,
                key = { item -> item.id }
            ) { item ->
                AfternoteListItem(
                    item = item,
                    onClick = { onItemClick(item.id) }
                )
            }
        }
    }
}

@Composable
private fun ReceiverAfternoteIntro(
    modifier: Modifier = Modifier,
    receiverName: String
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = B1)) {
                append(receiverName)
            }
            append(" ")
            append(stringResource(R.string.daily_answer_intro_line1_suffix))
            append("\n")
            append(stringResource(R.string.receiver_afternote_intro_line2))
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

@Preview(showBackground = true)
@Composable
private fun ReceiverAfternoteListScreenPreview() {
    AfternoteTheme {
        ReceiverAfternoteListScreen(
            receiverName = "김지은",
            items = listOf(
                AfternoteListDisplayItem(
                    id = "instagram",
                    serviceName = stringResource(R.string.receiver_afternote_item_instagram),
                    date = "2025.11.26",
                    iconResId = R.drawable.img_insta_pattern
                ),
                AfternoteListDisplayItem(
                    id = "gallery",
                    serviceName = stringResource(R.string.receiver_afternote_item_gallery),
                    date = "2025.11.26",
                    iconResId = R.drawable.ic_gallery
                ),
                AfternoteListDisplayItem(
                    id = "memorial_guideline",
                    serviceName = stringResource(R.string.receiver_afternote_item_memorial_guideline),
                    date = "2025.11.26",
                    iconResId = R.drawable.ic_memorial_guideline
                ),
                AfternoteListDisplayItem(
                    id = "naver_mail",
                    serviceName = stringResource(R.string.receiver_afternote_item_naver_mail),
                    date = "2025.11.26",
                    iconResId = R.drawable.img_naver_mail
                )
            ),
            onBackClick = {},
            onItemClick = {}
        )
    }
}
