package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.kuit.afternote.R
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterReceiver
import com.kuit.afternote.feature.timeletter.presentation.component.ReceiverInfoItem
import com.kuit.afternote.feature.timeletter.presentation.component.WritingPlusMenu
import com.kuit.afternote.feature.timeletter.presentation.component.chosungGroupedItems
import com.kuit.afternote.feature.timeletter.presentation.component.groupByChosung
import com.kuit.afternote.core.ui.component.DateWheelPicker
import com.kuit.afternote.core.ui.component.DateWheelPickerDefaults
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterWriterBottomBar
import com.kuit.afternote.feature.timeletter.presentation.component.TimeWheelPicker
import java.time.LocalDate
import java.time.LocalTime

private val RecipientDropdownMaxHeight = 300.dp

/**
 * 타임레터 작성 화면
 *
 * @param recipientName 수신자 이름
 * @param title 제목
 * @param content 내용
 * @param sendDate 선택된 발송 날짜
 * @param sendTime 선택된 발송 시간 (HH:mm)
 * @param showDatePicker DatePicker 표시 여부
 * @param showTimePicker TimePicker 표시 여부
 * @param draftCount 임시저장된 레터 개수
 * @param onTitleChange 제목 변경 콜백
 * @param onContentChange 내용 변경 콜백
 * @param onNavigateBack 뒤로가기 콜백
 * @param onRecipientClick 수신자 선택 클릭 콜백
 * @param onRegisterClick 등록 클릭 콜백
 * @param onSaveDraftClick 임시저장 클릭 콜백
 * @param onDraftCountClick 임시저장 개수 클릭 콜백 (DraftLetterScreen으로 이동)
 * @param onDateClick 날짜 선택 클릭 콜백
 * @param onTimeClick 시간 선택 클릭 콜백
 * @param onDatePickerDismiss DatePicker 닫기 콜백
 * @param onDateSelected 날짜 선택 완료 콜백
 * @param onTimePickerDismiss TimePicker 닫기 콜백
 * @param onTimeSelected 시간 선택 콜백 (24시간 형식 hour, minute)
 * @param showWritingPlusMenu 더보기(작성 플러스) 메뉴 표시 여부
 * @param onMoreClick 더보기 아이콘 클릭 콜백
 * @param onDismissPlusMenu 더보기 메뉴 닫기 콜백
 * @param receivers 수신자 목록 (드롭다운에 표시)
 * @param showRecipientDropdown 수신자 선택 드롭다운 표시 여부
 * @param onRecipientDropdownDismiss 수신자 드롭다운 닫기 콜백
 * @param onReceiverSelected 수신자 선택 콜백
 * @param modifier Modifier
 */
@Composable
fun TimeLetterWriterScreen(
    modifier: Modifier = Modifier,
    recipientName: String,
    title: String,
    content: String,
    sendDate: String = "",
    sendTime: String = "",
    showDatePicker: Boolean = false,
    showTimePicker: Boolean = false,
    draftCount: Int = 0,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onRecipientClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onSaveDraftClick: () -> Unit,
    onDraftCountClick: () -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onDatePickerDismiss: () -> Unit = {},
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit = { _, _, _ -> },
    onTimePickerDismiss: () -> Unit = {},
    onTimeSelected: (hour: Int, minute: Int) -> Unit = { _, _ -> },
    showWritingPlusMenu: Boolean = false,
    onMoreClick: () -> Unit = {},
    onDismissPlusMenu: () -> Unit = {},
    receivers: List<TimeLetterReceiver> = emptyList(),
    showRecipientDropdown: Boolean = false,
    onRecipientDropdownDismiss: () -> Unit = {},
    onReceiverSelected: (TimeLetterReceiver) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current

    Box(modifier = modifier.fillMaxSize()) {
        if (showWritingPlusMenu) {
            Popup(
                alignment = Alignment.BottomStart,
                offset = IntOffset(
                    with(density) { 64.dp.roundToPx() },
                    with(density) { (-232).dp.roundToPx() }
                ),
                onDismissRequest = onDismissPlusMenu,
                properties = PopupProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
            ) {
                WritingPlusMenu()
            }
        }

        if (showRecipientDropdown) {
            val groupedReceivers = groupByChosung(receivers) { it.receiver_name }
            val topBarHeightPx = with(density) { 44.dp.roundToPx() }
            val dropdownOffsetY = WindowInsets.statusBars.getTop(density) + topBarHeightPx
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(0, dropdownOffsetY),
                onDismissRequest = onRecipientDropdownDismiss,
                properties = PopupProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(RecipientDropdownMaxHeight)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                        .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                        .background(Color.White)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp)
                    ) {
                        chosungGroupedItems(groupedReceivers) { receiver ->
                            Box(
                                modifier = Modifier.clickable {
                                    onReceiverSelected(receiver)
                                    onRecipientDropdownDismiss()
                                }
                            ) {
                                ReceiverInfoItem(receiver = receiver)
                            }
                        }
                    }
                }
            }
        }
        Scaffold(
            modifier = Modifier.fillMaxWidth(),
            topBar = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .height(43.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(start = 24.dp)
                                .clickable { keyboardController?.hide() }
                        ) {
                            Image(
                                painter = painterResource(R.drawable.img_writing),
                                contentDescription = "키보드 숨기기",
                                modifier = Modifier.size(width = 20.dp, height = 22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Row(
                            modifier = Modifier.clickable { onRecipientClick() },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${recipientName}님께",
                                fontSize = 20.sp,
                                fontWeight = FontWeight(700),
                                fontFamily = FontFamily(Font(R.font.sansneobold)),
                                color = Color(0xFF212121)
                            )
                            Image(
                                painter = painterResource(R.drawable.ic_down),
                                contentDescription = "수신자 선택",
                                modifier = Modifier.padding(start = 14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "등록",
                            color = Color(0xFF9E9E9E),
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.sansneoregular)),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 20.sp,
                            modifier = Modifier
                                .padding(end = 24.dp)
                                .clickable { onRegisterClick() }
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF9E9E9E),
                        thickness = 0.4.dp
                    )
                }
            },
            bottomBar = {
                TimeLetterWriterBottomBar(
                    draftCount = draftCount,
                    onAddClick = {},
                    onSaveDraftClick = onSaveDraftClick,
                    onDraftCountClick = onDraftCountClick,
                    onLinkClick = {},
                    onMoreClick = onMoreClick
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .height(62.dp)
                        .padding(horizontal = 20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onDateClick() }
                    ) {
                        Text(
                            text = "발송 날짜",
                            color = Color(0xFF000000),
                            fontFamily = FontFamily(Font(R.font.sansneoregular)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 18.sp
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // 선택된 날짜 표시
                            if (sendDate.isNotEmpty()) {
                                Text(
                                    text = sendDate,
                                    color = Color(0xFF212121),
                                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                            Image(
                                painter = painterResource(R.drawable.ic_down_vector),
                                contentDescription = "날짜 선택",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(top = 15.dp)
                                    .size(width = 12.dp, height = 6.dp)
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 15.dp),
                            color = Color(0xFF9E9E9E),
                            thickness = 0.4.dp
                        )
                    }

                    Spacer(modifier = Modifier.width(26.dp))

                    Column(
                        modifier = Modifier
                            .width(106.dp)
                            .clickable { onTimeClick() }
                    ) {
                        Text(
                            text = "발송 시간",
                            color = Color(0xFF000000),
                            fontFamily = FontFamily(Font(R.font.sansneoregular)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 18.sp
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (sendTime.isNotEmpty()) {
                                Text(
                                    text = sendTime,
                                    color = Color(0xFF212121),
                                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                            Image(
                                painter = painterResource(R.drawable.ic_down_vector),
                                contentDescription = "시간 선택",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(top = 15.dp)
                                    .size(width = 12.dp, height = 6.dp)
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 15.dp),
                            color = Color(0xFF9E9E9E),
                            thickness = 0.4.dp
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 16.dp)
                ) {
                    BasicTextField(
                        value = title,
                        onValueChange = onTitleChange,
                        textStyle = TextStyle(
                            color = Color(0xFF000000),
                            fontFamily = FontFamily(Font(R.font.sansneoregular)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        cursorBrush = SolidColor(Color(0xFF000000)),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                if (title.isEmpty()) {
                                    Text(
                                        text = "제목",
                                        // BodyLarge-R
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            lineHeight = 24.sp,
                                            fontFamily = FontFamily(Font(R.font.sansneoregular)),
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFF212121),
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 15.dp),
                        color = Color(0xFF9E9E9E),
                        thickness = 0.4.dp
                    )
                }

                Spacer(modifier = Modifier.height(17.dp))

                BasicTextField(
                    value = content,
                    onValueChange = onContentChange,
                    textStyle = TextStyle(
                        color = Color(0xFF212121),
                        fontFamily = FontFamily(Font(R.font.sansneoregular)),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    cursorBrush = SolidColor(Color(0xFF212121)),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (content.isEmpty()) {
                                Text(
                                    text = "소중한 사람에게 타임 레터를 작성하세요.",
                                    color = Color(0xFF9E9E9E),
                                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }

        // DatePicker 오버레이
        if (showDatePicker) {
            var selectedDate by remember { mutableStateOf(LocalDate.now()) }
            // 배경 딤 처리 + 클릭 시 닫기
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDatePickerDismiss() }
                    .zIndex(1f)
            )

            // DateWheelPicker 오버레이 (발송 날짜 아래 위치) - zIndex로 딤 위에 그려 휠 터치 전달
            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp)
                    ).clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(vertical = 16.dp)
            ) {
                DateWheelPicker(
                    modifier = Modifier.width(DateWheelPickerDefaults.ContainerWidth),
                    currentDate = selectedDate,
                    onDateChanged = { date ->
                        selectedDate = date
                        onDateSelected(date.year, date.monthValue, date.dayOfMonth)
                    }
                )
            }
        }

        // TimePicker 오버레이 (TimeWheelPicker)
        if (showTimePicker) {
            val now = LocalTime.now()
            val (initialHour, initialMinute) = if (sendTime.isNotBlank()) {
                val parts = sendTime.split(":")
                if (parts.size == 2) {
                    val h = parts[0].toIntOrNull() ?: now.hour
                    val m = parts[1].toIntOrNull() ?: now.minute
                    h to m
                } else {
                    now.hour to now.minute
                }
            } else {
                now.hour to now.minute
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onTimePickerDismiss() }
                    .zIndex(1f)
            )

            // TimeWheelPicker 카드 - zIndex로 딤 위에 그려 휠 터치 전달
            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp)
                    ).clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(vertical = 16.dp)
            ) {
                TimeWheelPicker(
                    initialHour = initialHour,
                    initialMinute = initialMinute,
                    onTimeChanged = { hour, minute -> onTimeSelected(hour, minute) }
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun TimeLetterWriterScreenPreview() {
    TimeLetterWriterScreen(
        recipientName = "박채연",
        title = "",
        content = "",
        sendDate = "",
        sendTime = "",
        draftCount = 3,
        onTitleChange = {},
        onContentChange = {},
        onNavigateBack = {},
        onRecipientClick = {},
        onRegisterClick = {},
        onSaveDraftClick = {},
        onDraftCountClick = {},
        onDateClick = {},
        onTimeClick = {},
        onTimePickerDismiss = {},
        onTimeSelected = { _, _ -> }
    )
}
