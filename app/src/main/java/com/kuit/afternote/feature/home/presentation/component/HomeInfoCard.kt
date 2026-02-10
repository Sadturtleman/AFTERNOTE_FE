package com.kuit.afternote.feature.home.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun HomeInfoCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String = "",
    badge: @Composable () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = androidx.compose.ui.graphics.Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(
                start = 20.dp,
                top = 15.dp,
                end = 20.dp,
                bottom = 16.dp
            )
        ) {
            Text(
                text = title,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                color = Gray9
            )

            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = Gray9
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            badge()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeInfoCardPreview() {
    AfternoteTheme {
        HomeInfoCard(
            modifier = Modifier.padding(16.dp),
            title = "가족들이 모르는 '주거래 은행' 정보를\n잊으신 건 없나요?",
            badge = {
                RecipientBadge(
                    text = "지금 애프터노트에 기록하기",
                    showCheckIcon = false
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeInfoCardWithSubtitlePreview() {
    AfternoteTheme {
        HomeInfoCard(
            modifier = Modifier.padding(16.dp),
            title = "\"내 인생에서 가장 소중했던 순간은?\"",
            subtitle = "   - 아이가 태어났을 때...",
            badge = {
                RecipientBadge(
                    text = "그날의 기록 다시 읽기",
                    showCheckIcon = false
                )
            }
        )
    }
}
