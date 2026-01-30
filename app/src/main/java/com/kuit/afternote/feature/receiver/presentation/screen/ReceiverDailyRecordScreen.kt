package com.kuit.afternote.feature.receiver.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.receiver.presentation.component.CalendarGrid
import com.kuit.afternote.feature.receiver.presentation.component.RecordListItem
import com.kuit.afternote.feature.receiver.presentation.component.TodayRecordCard
import com.kuit.afternote.feature.receiver.presentation.component.ReceiverWheelDatePickerDialog
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
@Suppress("AssignedValueIsNeverRead")
fun MindRecordScreen() {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableIntStateOf(10) }
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.RECORD) }

    if (showDatePicker) {
        ReceiverWheelDatePickerDialog(
            onDismiss = { showDatePicker = false },
            onConfirm = { showDatePicker = false }
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "마음의 기록",
                onBackClick = { }
            )
        },
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
                .padding(20.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(vertical = 10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "오늘의 기록",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = Sansneo
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Go",
                            tint = B2
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    TodayRecordCard()
                }
            }

            item {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "날짜별 기록",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = Sansneo,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 20.dp)) {
                            // 년월 선택 버튼
                            OutlinedButton(
                                onClick = { showDatePicker = true },
                                shape = RoundedCornerShape(20.dp),
                                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                                    width = 1.dp,
                                    brush = SolidColor(B3)
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(
                                    text = "2025년 11월",
                                    fontSize = 12.sp,
                                    color = Gray9,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            CalendarGrid(selectedDay = selectedDate, onDaySelected = { selectedDate = it })
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(vertical = 20.dp)) {
                    RecordListItem(date = "2025년 11월 10일", content = "오늘 하루, 누구에게 가장 고마웠나요?")
                    Spacer(modifier = Modifier.height(12.dp))
                    RecordListItem(date = "2025년 11월 10일", content = "오늘의 일기")

                    Spacer(modifier = Modifier.height(24.dp))

                    ClickButton(
                        color = B3,
                        title = "확인하기",
                        onButtonClick = {}
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMindRecord() {
    MindRecordScreen()
}
