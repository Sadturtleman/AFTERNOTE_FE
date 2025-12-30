package com.kuit.afternote.feature.mainpage.presentation.component.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Black9

/**
 * 처리 방법 아이템 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 체크 아이콘: 파란색 원 (B1), 16dp
 * - 텍스트: 14sp, Regular
 */
@Composable
fun ProcessingMethodItem(
    modifier: Modifier = Modifier,
    text: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 체크 아이콘 (파란색 원)
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(B1)
        )

        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontFamily = FontFamily(Font(R.font.sansneoregular)),
                fontWeight = FontWeight(400),
                color = Black9
            )
        )
    }
}
