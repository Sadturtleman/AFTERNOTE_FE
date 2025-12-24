package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
