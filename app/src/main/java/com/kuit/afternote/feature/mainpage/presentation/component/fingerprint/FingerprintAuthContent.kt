package com.kuit.afternote.feature.mainpage.presentation.component.fingerprint

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.onboarding.presentation.component.ClickButton
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Black9

/**
 * 지문 인증 컨텐츠 컴포넌트
 *
 * 안내 텍스트, 지문 아이콘, 인증 버튼을 포함하는 컨텐츠 영역
 */
@Composable
fun FingerprintAuthContent(
    modifier: Modifier = Modifier,
    onFingerprintAuthClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 137.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 안내 텍스트
        Text(
            text = "사용자 인증 후 조회가 가능합니다.",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = FontFamily(Font(R.font.sansneomedium)),
                fontWeight = FontWeight(500),
                color = Black9,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // 지문 아이콘
        Box(
            modifier = Modifier
                .size(100.dp, 114.dp)
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_fingerprint),
                contentDescription = "지문 인증",
                modifier = Modifier.size(100.dp, 114.dp)
            )
        }

        // 지문 인증 버튼
        ClickButton(
            title = "지문 인증하기",
            onButtonClick = onFingerprintAuthClick,
            color = B3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 44.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FingerprintAuthContentPreview() {
    AfternoteTheme {
        FingerprintAuthContent()
    }
}
