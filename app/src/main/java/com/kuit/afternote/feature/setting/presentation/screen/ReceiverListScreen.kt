package com.kuit.afternote.feature.setting.presentation.screen

import ReceiverRow
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.BottomNavItem
import com.kuit.afternote.core.ui.component.BottomNavigationBar
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.Recipient
//import com.kuit.afternote.feature.receiver.presentation.component.ReceiverRow
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun ReceiverListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    receivers: List<Recipient> = emptyList() // TODO: ViewModel에서 가져오기
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "수신자 목록",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.HOME
            ) { }
        }
    ) { paddingValues ->
        if (receivers.isEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(R.drawable.img_empty_state),
                    contentDescription = "빈 수신자 목록",
                    modifier = Modifier
                        .width(106.dp)
                        .height(109.dp)
                        .alpha(0.6f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "아직 등록된 수신자가 없어요.\n수신자를 등록하여 정보를 전달하세요.",
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = Gray4,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(receivers) { receiver ->
                    ReceiverRow(
                        name = receiver.name,
                        onClick = {
                            // TODO: 수신자 상세 화면으로 이동
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "수신자 목록 (데이터 있음)")
@Composable
private fun ReceiverListScreenWithDataPreview() {
    AfternoteTheme {
        ReceiverListScreen(
            onBackClick = {},
            receivers = listOf(
                Recipient(id = "1", name = "김지은", label = "딸"),
                Recipient(id = "2", name = "박서연", label = "친구"),
                Recipient(id = "3", name = "이민수", label = "아들")
            )
        )
    }
}

@Preview(showBackground = true, name = "수신자 목록 (빈 상태)")
@Composable
private fun ReceiverListScreenEmptyPreview() {
    AfternoteTheme {
        ReceiverListScreen(
            onBackClick = {},
            receivers = emptyList()
        )
    }
}
