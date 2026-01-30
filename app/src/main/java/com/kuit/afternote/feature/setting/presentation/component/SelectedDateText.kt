package com.kuit.afternote.feature.setting.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * 선택된 날짜를 표시하는 텍스트 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 날짜: B1 (파란색), 14sp, Regular
 * - 나머지 텍스트: Gray9, 14sp, Regular
 * - 포맷: "YYYY.MM.DD.에 전달 될 예정입니다."
 *
 * @param modifier Modifier for the component
 * @param date 선택된 날짜
 */
@Composable
fun SelectedDateText(
    modifier: Modifier = Modifier,
    date: LocalDate
) {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.")
    val formattedDate = date.format(dateFormatter)

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = B1)) {
            append(formattedDate)
        }
        withStyle(style = SpanStyle(color = Gray9)) {
            append("에 전달될 예정입니다.")
        }
    }

    Text(
        text = annotatedString,
        style = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Normal
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectedDateTextPreview() {
    AfternoteTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            SelectedDateText(
                date = LocalDate.of(2027, 10, 12)
            )
        }
    }
}
