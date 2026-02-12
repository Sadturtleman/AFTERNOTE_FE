package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.receiver.presentation.component.SenderListItem

@Composable
fun SenderListScreen(
    onBackClick: () -> Unit = {},
    // 발신자 클릭 시 동작할 로직 추가 (예: 상세 화면 이동)
    onSenderClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // 예시 데이터 (실제로는 ViewModel 등에서 가져오게 됩니다)
    val senderList = listOf("발신자 박경민", "발신자 김철수", "발신자 이영희")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = "받은 기록함",
                    onBackClick = onBackClick,
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_vector),
                                contentDescription = "뒤로가기",
                                modifier = Modifier.size(width = 6.dp, height = 12.dp)
                            )
                        }
                    }
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            // TopBar와의 간격을 32.dp로 설정
            contentPadding = PaddingValues(top = 32.dp)
        ) {
            items(senderList.size) { index ->
                SenderListItem(
                    senderName = senderList[index],
                    onClick = { onSenderClick(senderList[index]) }
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun ReceiveListPreview() {

    SenderListScreen(
        onBackClick = {}
    )
}
