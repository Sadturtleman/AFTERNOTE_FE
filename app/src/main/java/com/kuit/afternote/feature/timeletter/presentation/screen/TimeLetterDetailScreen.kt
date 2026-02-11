package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.TopBar

/**
 * 타임레터 상세 화면.
 * 리스트에서 선택한 타임레터의 제목, 수신인, 발송 예정일, 본문을 표시한다.
 *
 * @param receiverName 수신인 이름
 * @param sendDate 발송 예정일 (표시용 문자열)
 * @param title 편지 제목
 * @param content 편지 본문
 * @param onBackClick 뒤로가기 콜백
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeLetterDetailScreen(
    receiverName: String,
    sendDate: String,
    title: String,
    content: String,
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val receiverTitle = stringResource(R.string.time_letter_detail_receiver_to, receiverName.ifEmpty { "-" })
    val sendDateLabel = stringResource(R.string.time_letter_detail_send_date, sendDate.ifEmpty { "-" })

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = receiverTitle,
                    onBackClick = onBackClick,
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_vector),
                                contentDescription = stringResource(R.string.content_description_back),
                                modifier = Modifier.size(width = 6.dp, height = 12.dp)
                            )
                        }
                    },
                    titleContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { /* 수신자 선택 로직 */ }
                        ) {
                            Text(
                                text = receiverTitle,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Image(
                                painter = painterResource(id = R.drawable.ic_down),
                                contentDescription = stringResource(R.string.content_description_expand_down)
                            )
                        }
                    }
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = sendDateLabel,
                fontSize = 14.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(31.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(37.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFBDBDBD)
            )

            Spacer(modifier = Modifier.height(29.dp))

            Text(
                text = content,
                fontSize = 14.sp,
                color = Color(0xFF212121)
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeLetterDetail() {
    MaterialTheme {
        TimeLetterDetailScreen(
            receiverName = "박채연",
            sendDate = "2026. 11. 24",
            title = "채연아 20번째 생일을 축하해",
            content = "너가 태어난 게 엊그제 같은데 벌써 스무살이라니, 엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶구나. 요즘 학교는 어떻게 다니고 있니? 공부는 잘하고 있지? 친구는 많이 사귀었니? 채연이가 가고싶어했던 그 학교, 학과에 갔을지 엄마는 너무 궁금해하면서 이 편지를 쓰고 있어. 엄마랑 다시 만나면 그동안 어떻게 지냈는지 하나하나 다 알려줘야 해. 사랑한다 생일 축하해 내 딸."
        )
    }
}
