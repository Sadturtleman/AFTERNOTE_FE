package com.kuit.afternote.core.ui.component.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3

@Composable
fun SignUpContentButton(
    onNextClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )

        ClickButton(
            title = "다음",
            onButtonClick = onNextClick,
            color = B3,
            modifier = Modifier
                .align(Alignment.Center)
        )
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
