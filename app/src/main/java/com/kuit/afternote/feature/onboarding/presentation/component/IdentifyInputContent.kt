package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IdentifyInputContent(
    state: TextFieldState,
    modifier: Modifier = Modifier
) {
    val maxLength = 7
    val text = state.text.toString()

    // 1. 포커스 제어를 위한 FocusRequester 생성
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
            .width(335.dp)
            .height(72.dp)
            .background(color = Color(0xFFF8F9FA), shape = RoundedCornerShape(16.dp))
            // 2. 박스 클릭 시 동작 정의
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null // 클릭 시 물결 효과(Ripple) 제거 (원하면 제거하지 않아도 됨)
            ) {
                // 박스를 누르면 포커스를 요청하고
                focusRequester.requestFocus()
                // 커서를 무조건 문자의 맨 마지막으로 이동시킴
                state.edit {
                    selection = TextRange(length)
                }
            }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester), // 3. TextField에 포커스 리퀘스터 연결
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            inputTransformation = {
                if (length > maxLength || !asCharSequence().all { it.isDigit() }) {
                    revertAllChanges()
                }
            },
            decorator = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // 앞 6자리 영역
                    Box(modifier = Modifier.width(100.dp)) {
                        if (text.isEmpty()) {
                            Text(
                                text = "Text Field",
                                style = TextStyle(color = Color(0xFFBCBCBC), fontSize = 18.sp)
                            )
                        } else {
                            Text(
                                text = text.take(6),
                                style = TextStyle(
                                    color = Color.DarkGray,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    letterSpacing = 1.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 하이픈
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(2.dp)
                                .background(Color.Black)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        // 뒷자리 첫 글자
                        val backFirstDigit = if (text.length > 6) text[6].toString() else "T"
                        Text(
                            text = backFirstDigit,
                            style = TextStyle(
                                color = if (text.length > 6) Color.Black else Color(0xFFBCBCBC),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )

                        // 마스킹 도트
                        Row(
                            modifier = Modifier.padding(start = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            repeat(6) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(Color(0xFF212121), shape = RoundedCornerShape(50))
                                )
                            }
                        }
                    }
                }
                // 실제 TextField는 투명하게 숨김
                Box(Modifier.alpha(0f)) { innerTextField() }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IdentifyInputContentPreview() {
    val memberCode = rememberTextFieldState()
    Box(Modifier.padding(20.dp)) {
        IdentifyInputContent(memberCode)
    }
}
