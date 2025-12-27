package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Black9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun ClickButton(
    color: Color,
    onButtonClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onButtonClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .dropShadow(
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = 0.05f),
                offsetX = 5.dp,
                offsetY = 0.dp,
                blur = 5.dp,
                spread = 0.dp
            ),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Black9,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
private fun ClickButtonPreview() {
    ClickButton(
        onButtonClick = {},
        title = "시작하기",
        color = B3
    )
}
