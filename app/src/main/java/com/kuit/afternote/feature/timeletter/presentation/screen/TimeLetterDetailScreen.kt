package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray9

/**
 * 타임레터 상세 화면.
 * 리스트에서 선택한 타임레터의 수신인, 제목, 작성/발송 일자, 본문을 카드 형태로 표시한다.
 *
 * @param receiverName 수신인 이름
 * @param sendDate 발송 일자 (표시용 문자열)
 * @param title 편지 제목
 * @param content 편지 본문
 * @param createdAt 작성 일자 (표시용, 없으면 "-")
 * @param onBackClick 뒤로가기 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeLetterDetailScreen(
    receiverName: String,
    sendDate: String,
    title: String,
    content: String,
    createdAt: String = "",
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val recipientLabel = stringResource(R.string.time_letter_detail_recipient_label)
    val createdLabel = stringResource(R.string.time_letter_detail_created_at, createdAt.ifEmpty { "-" })
    val deliveryLabel = stringResource(R.string.time_letter_detail_delivery_date, sendDate.ifEmpty { "-" })
    val screenTitle = stringResource(R.string.nav_timeletter)

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = screenTitle,
                    onBackClick = onBackClick
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                ) {
                    Row {
                        Text(
                            text = recipientLabel,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = " ${receiverName.ifEmpty { "-" }}",
                            fontSize = 14.sp,
                            color = Gray9,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700),
                        color = Gray9
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = createdLabel,
                        fontSize = 14.sp,
                        color = Gray5
                    )
                    Text(
                        text = deliveryLabel,
                        fontSize = 14.sp,
                        color = Gray5
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Gray4
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = content,
                        fontSize = 14.sp,
                        color = Gray9,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimeLetterDetail() {
    MaterialTheme {
        TimeLetterDetailScreen(
            receiverName = "박채연",
            sendDate = "2026. 11. 24. 00:00",
            title = "채연아 20번째 생일을 축하해",
            content = "너가 태어난 게 엊그제 같은데 벌써 스무살이라니,, 엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶구나.\n\n요즘 학교는 어떻게 다니고 있니? 공부는 잘하고 있지? 친구는 많이 사귀었니? 채연이가 가고싶어했던 그 학교, 학과에 갔을지 엄마는 너무 궁금해하면서 이 편지를 쓰고 있어. 엄마랑 다시 만나면 그동안 어떻게 지냈는지 하나하나 다 알려줘야 해. 사랑한다 생일 축하해 내 딸.",
            createdAt = "2023. 11. 21. 18:34"
        )
    }
}
