package com.kuit.afternote.feature.dailyrecord.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 주간리포트에 쓰이는 글자 컴포넌트
 *
 */
@Composable
fun RecordOneEyeLook(
    modifier: Modifier = Modifier,
    title: String
    ) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)

    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Sansneo
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun Recordrev() {
    RecordOneEyeLook(title = "활동 한 눈에 보기")
}
