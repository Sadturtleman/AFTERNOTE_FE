package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.mainpage.presentation.model.ProcessingMethodItem
import com.kuit.afternote.ui.expand.bottomBorder
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 처리 방법 리스트 컴포넌트
 */
@Composable
fun ProcessingMethodList(
    modifier: Modifier = Modifier,
    items: List<ProcessingMethodItem>,
    onAddClick: () -> Unit = {},
    onItemMoreClick: (String) -> Unit = {},
    initialShowTextField: Boolean = false
) {
    var showTextField by remember { mutableStateOf(initialShowTextField) }
    val textFieldState = rememberTextFieldState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(16.dp)
            ).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEach { item ->
            ProcessingMethodCheckbox(
                item = item,
                onMoreClick = { onItemMoreClick(item.id) }
            )
            Spacer(
                modifier = Modifier.height(6.dp)
            )
        }

        // 텍스트 필드 (버튼 클릭 시 표시)
        if (showTextField) {
            Spacer(
                modifier = Modifier.height(7.dp)
            )
            // 텍스트 필드 컨테이너 (하단 보더 포함)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomBorder(color = Gray2, width = 1.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                OutlinedTextField(
                    state = textFieldState,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    placeholder = {
                        Text(
                            text = "Text Field",
                            style = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                fontFamily = Sansneo,
                                fontWeight = FontWeight.Normal,
                                color = Gray4
                            )
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // 추가 버튼
        Image(
            painter = painterResource(R.drawable.ic_add_circle),
            contentDescription = "추가",
            modifier = Modifier
                .clickable(onClick = {
                    showTextField = !showTextField
                    onAddClick()
                })
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProcessingMethodListPreview() {
    AfternoteTheme {
        ProcessingMethodList(
            items = listOf(
                ProcessingMethodItem("1", "게시물 내리기"),
                ProcessingMethodItem("2", "댓글 비활성화")
            ),
            onAddClick = {},
            onItemMoreClick = {},
            initialShowTextField = true
        )
    }
}
