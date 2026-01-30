package com.kuit.afternote.feature.setting.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.feature.setting.presentation.uimodel.KeyAction
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun KeyPad(
    modifier: Modifier = Modifier,
    onAction: (KeyAction) -> Unit
) {
    val onActionStable by rememberUpdatedState(onAction)

    val keys = remember {
        listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("delete", "0", "check")
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp) // 간격의 명시적 제어
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { key ->
                    KeyButton(
                        key = key,
                        modifier = Modifier.weight(1f), // 균등한 클릭 영역 확보
                        onClick = {
                            val action = when (key) {
                                "delete" -> KeyAction.Delete
                                "check" -> KeyAction.Confirm
                                else -> KeyAction.Number(key)
                            }
                            onActionStable(action)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun KeyButton(
    key: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 3. 터치 영역 최적화: Box를 활용해 텍스트보다 넓은 클릭 영역 제공
    Box(
        modifier = modifier
            .height(64.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(), // 시각적 피드백 제공
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        when (key) {
            "delete" -> Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "삭제")
            "check" -> Text(text = "확인", style = KeyTextStyle.copy(fontSize = 16.sp))
            else -> Text(text = key, style = KeyTextStyle)
        }
    }
}

private val KeyTextStyle = TextStyle(
    fontFamily = Sansneo,
    fontSize = 24.sp,
    fontWeight = FontWeight.Bold
)
