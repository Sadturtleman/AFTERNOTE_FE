package com.kuit.afternote.feature.dailyrecord.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.DateWheelPicker
import com.kuit.afternote.core.ui.component.DateWheelPickerDefaults
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecordDiaryContentItem(
    modifier: Modifier = Modifier,
    standard: String,
    onDateSelected: (Int, Int, Int) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit,

    //작성 날짜
    onDateClick: () -> Unit,
    sendDate: String,
    showDatePicker: Boolean = false,
    onDatePickerDismiss: () -> Unit,

    // 깊은 생각 기록 시 카테고리 (기록 주제)
    selectedCategory: String = "나의 가치관",
    onCategoryChange: (String) -> Unit = {},
    showCategoryDropdown: Boolean = false,
    onCategoryClick: () -> Unit = {},
    onCategoryDropdownDismiss: () -> Unit = {}
) {
    // 오늘 날짜 기준으로 받아오기
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    val formattedDate = today.format(formatter)

    var s = " "
    if (standard == "일기 기록하기") {
        s = "작성 날짜"
    } else {
        s = "기록 주제"
    }
    Column(
        modifier = Modifier
            .padding(top = 24.dp, start = 20.dp, end = 20.dp)
    ) {
        Text(
            text = s,
            color = Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Sansneo
        )

        Box(
            modifier = Modifier
                .padding(all = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (s == "작성 날짜") {
                    Text(
                        text = if (sendDate.isNotEmpty()) sendDate else formattedDate,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else if (s == "기록 주제") {
                    Box {
                        Text(
                            text = selectedCategory,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        DropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = onCategoryDropdownDismiss,
                            offset = DpOffset(x = 0.dp, y = 0.dp),
                            modifier = Modifier.width(350.dp)
                        ) {
                            listOf(
                                "나의 가치관",
                                "오늘 떠올린 생각",
                                "인생을 되돌아 보며"
                            ).forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = option,
                                            fontSize = 14.sp,
                                            fontFamily = Sansneo
                                        )
                                    },
                                    onClick = {
                                        onCategoryChange(option)
                                        onCategoryDropdownDismiss()
                                    }
                                )
                            }
                        }
                    }
                }

                Image(
                    painter = painterResource(id = R.drawable.ic_under),
                    contentDescription = "밑 화살표",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            if (s == "기록 주제") onCategoryClick() else onDateClick()
                        }
                )
                if (sendDate.isNotEmpty()) {
                    val sendDate = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
                }
            }
            Divider(color = Color.LightGray, thickness = 0.8.dp)
        }
        BasicTextField(
            value = title,
            onValueChange = onTitleChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 16.dp, bottom = 8.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 18.sp,
                color = Black
            ),
            decorationBox = { innerTextField ->
                if (title.isEmpty()) {
                    Text(
                        text = "제목",
                        color = Gray5,
                        fontSize = 16.sp
                    )
                }
                innerTextField()
            }
        )
        if (showDatePicker) {
            var selectedDate by remember { mutableStateOf(LocalDate.now()) }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onDateSelected(selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth)
                        onDatePickerDismiss()
                    }
                    .zIndex(1f)
            )

            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                DateWheelPicker(
                    modifier = Modifier.width(DateWheelPickerDefaults.ContainerWidth),
                    currentDate = selectedDate,
                    onDateChanged = { date -> selectedDate = date },
                    minDate = LocalDate.now()
                )
            }
        }
        Divider(color = Color.LightGray, thickness = 0.8.dp)

        BasicTextField(
            value = content,
            onValueChange = onContentChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 18.sp,
                color = Black
            ),
            decorationBox = { innerTextField ->
                if (content.isEmpty()) {
                    Text(
                        text = "당신의 오늘을 기록해보세요",
                        color = Gray5,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                innerTextField()
            }
        )
    }
}
