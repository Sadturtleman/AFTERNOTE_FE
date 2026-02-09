package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.feature.receiver.presentation.component.ContentSection
import com.kuit.afternote.feature.receiver.presentation.component.HeroCard
import com.kuit.afternote.feature.receiver.presentation.component.TopHeader
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
@Suppress("AssignedValueIsNeverRead")
fun ReceiverAfterNoteScreen() {
    var showDialog by remember { mutableStateOf(false) }

    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.AFTERNOTE) }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 타이틀 텍스트
                    Text(
                        text = "모든 기록을 내려받으시겠습니까?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Gray9, // 또는 Color.Black
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 버튼 영역 (Row로 배치하여 반반 나누기)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp) // 버튼 사이 간격
                    ) {
                        // '예' 버튼
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier
                                .weight(1f) // 1:1 비율
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFCBE5FF), // 이미지와 유사한 연한 파랑
                                contentColor = Color.Black
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            Text(text = "예", fontWeight = FontWeight.SemiBold)
                        }

                        // '아니요' 버튼
                        Button(
                            onClick = { showDialog = false },
                            modifier = Modifier
                                .weight(1f) // 1:1 비율
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE0E0E0), // 연한 회색
                                contentColor = Color.Black
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            Text(text = "아니요", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
    Scaffold(
        topBar = { TopHeader() },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedBottomNavItem,
                onItemSelected = { selectedBottomNavItem = it }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            item {
                Text(
                    text = "故 박서연님이 남기신 기록",
                    fontWeight = FontWeight.Bold,
                    color = Gray9,
                    fontFamily = Sansneo,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                HeroCard()
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                ContentSection(
                    title = "마음의 기록",
                    desc = "고인의 일상적인 생각과 감정, 일기들입니다.",
                    subDesc = "150개 마음의 기록이 있습니다.",
                    btnText = "마음의 기록 확인하러 가기",
                    imageResource = painterResource(R.drawable.img_book)
                )
            }

            item {
                ContentSection(
                    title = "타임레터",
                    desc = "고인이 특별한 날에 작성한 편지입니다.",
                    subDesc = "30개 라이프 이벤트 레터가 있습니다.",
                    btnText = "라이프 이벤트 레터 확인하러 가기",
                    imageResource = painterResource(R.drawable.img_letter)
                )
            }

            item {
                ContentSection(
                    title = "애프터노트",
                    desc = "고인이 사후 정리하고자 하는 데이터입니다.",
                    subDesc = "10개의 애프터노트가 있습니다.",
                    btnText = "애프터노트 확인하러 가기",
                    imageResource = painterResource(R.drawable.img_notebook)
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                ClickButton(
                    color = B3,
                    onButtonClick = {
                        showDialog = true
                    },
                    title = "모든 기록 내려받기"
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReceiverAfterNote() {
    MaterialTheme {
        ReceiverAfterNoteScreen()
    }
}
