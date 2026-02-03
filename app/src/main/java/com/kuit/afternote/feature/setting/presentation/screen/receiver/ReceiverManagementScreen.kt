package com.kuit.afternote.feature.setting.presentation.screen.receiver

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiver
import com.kuit.afternote.feature.setting.presentation.dummy.ReceiverDummyData
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray8
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.util.KoreanConsonantUtil

@Composable
fun ReceiverManagementScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit = {},
    receivers: List<MainPageEditReceiver> = emptyList(),
    onReceiverClick: (MainPageEditReceiver) -> Unit = {}
) {
    val groupedReceivers = remember(receivers) {
        KoreanConsonantUtil.groupByInitialConsonant(receivers) { it.name }
    }

    BackHandler(onBack = onBackClick)
    Scaffold(
        containerColor = Gray1,
        topBar = {
            TopBar(
                title = "수신인 목록",
                onBackClick = onBackClick,
                onActionClick = onRegisterClick,
                actionText = "등록"
            )
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
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(space = 28.dp)
            ) {
                groupedReceivers.forEach { (consonant, receiversInGroup) ->
                    item(key = "section_$consonant") {
                        ConsonantSection(
                            consonant = consonant,
                            receivers = receiversInGroup,
                            onReceiverClick = onReceiverClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConsonantSection(
    modifier: Modifier = Modifier,
    consonant: Char,
    receivers: List<MainPageEditReceiver>,
    onReceiverClick: (MainPageEditReceiver) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        // 초성 헤더
        Column {
            Text(
                text = consonant.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp),
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = Sansneo,
                color = Gray5
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Gray4
            )
        }

        // 수신자 목록
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            receivers.forEach { receiver ->
                ReceiverManagementItem(
                    receiver,
                    onReceiverClick = onReceiverClick
                )
            }
        }
    }
}

@Composable
private fun ReceiverManagementItem(
    receiver: MainPageEditReceiver,
    onReceiverClick: (MainPageEditReceiver) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onReceiverClick(receiver) }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.img_recipient_profile),
            contentDescription = "프로필 사진",
            modifier = Modifier.size(52.dp),
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = receiver.name,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Black,
                )
            )
            Text(
                text = receiver.label,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray8,
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "수신인 목록 (데이터 있음)")
@Composable
private fun ReceiverManagementScreenWithDataPreview() {
    AfternoteTheme {
        ReceiverManagementScreen(
            onBackClick = {},
            receivers = ReceiverDummyData.receiverList
        )
    }
}

@Preview(showBackground = true, name = "수신인 목록 (빈 상태)")
@Composable
private fun ReceiverManagementScreenEmptyPreview() {
    AfternoteTheme {
        ReceiverManagementScreen(
            onBackClick = {},
            receivers = emptyList()
        )
    }
}
