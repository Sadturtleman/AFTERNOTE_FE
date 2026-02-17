package com.kuit.afternote.core.ui.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3

@Composable
fun SignUpContentButton(
    onNextClick: () -> Unit,
    buttonAlignment: Alignment = Alignment.Center,
    contentSpacing: Dp? = null,
    buttonTitle: String = "다음",
    content: @Composable ColumnScope.() -> Unit,
) {
    // 1. 컨텐츠 기준으로 띄우고 싶을 때 (순서대로 나열)
    if (contentSpacing != null) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // (1) 컨텐츠 표시
            content()

            // (2) 입력받은 만큼 간격 띄우기 (여기서 64.dp가 적용됨)
            Spacer(modifier = Modifier.height(contentSpacing))

            // (3) 버튼 표시 (가로 중앙 정렬을 위해 Box로 감쌈)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ClickButton(
                    title = buttonTitle,
                    onButtonClick = onNextClick,
                    color = B3
                )
            }
        }
    }
    // 2. 기존 방식 (화면 위치 고정, 다른 화면 깨짐 방지용)
    else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                content = content
            )

            ClickButton(
                title = buttonTitle,
                onButtonClick = onNextClick,
                color = B3,
                modifier = Modifier
                    .align(buttonAlignment)
                    .padding(bottom = if (buttonAlignment == Alignment.BottomCenter) 33.dp else 0.dp)
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun SignUpContentButtonPreview() {
    AfternoteTheme {
        SignUpContentButton(
            onNextClick = {},
            content = {
                // Preview용 콘텐츠
            }
        )
    }
}
