package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R

/**
 * 초성 헤더 컴포넌트
 *
 * 수신자 목록에서 초성별 구분을 표시하는 헤더
 * 예: ㄱ, ㄴ, ㄷ 등
 */
@Composable
fun ChosungHeader(
    chosung: Char,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = chosung.toString(),
            color = Color(0xFF9E9E9E), // gray-scale-gray-5
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium, // 500
            lineHeight = 14.sp,
            fontFamily = FontFamily(
                Font(R.font.sansneomedium, FontWeight.Medium)
            ),
            modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 8.dp)
        )
        Divider(
            color = Color(0xFFBDBDBD), // gray-scale-gray-4
            thickness = 1.dp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = modifier.padding(top = 16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun ChosungHeaderPreview() {
    ChosungHeader(chosung = 'ㄱ')
}
