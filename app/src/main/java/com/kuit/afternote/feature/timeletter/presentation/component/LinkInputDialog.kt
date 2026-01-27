package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kuit.afternote.R

/**
 * 링크 입력 다이얼로그
 *
 * @param onDismiss 다이얼로그 닫기 콜백
 * @param onConfirm 링크 확인 콜백 (입력된 URL 전달)
 */
@Composable
fun LinkInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var linkText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "링크 추가",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                    color = Color(0xFF212121)
                )

                Spacer(modifier = Modifier.height(16.dp))

                BasicTextField(
                    value = linkText,
                    onValueChange = { linkText = it },
                    textStyle = TextStyle(
                        color = Color(0xFF212121),
                        fontFamily = FontFamily(Font(R.font.sansneoregular)),
                        fontSize = 14.sp
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Column {
                            if (linkText.isEmpty()) {
                                Text(
                                    text = "https://",
                                    color = Color(0xFF9E9E9E),
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.sansneoregular))
                                )
                            }
                            innerTextField()
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(
                                color = Color(0xFF9E9E9E),
                                thickness = 0.4.dp
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 버튼 영역
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = "취소",
                            color = Color(0xFF9E9E9E),
                            fontFamily = FontFamily(Font(R.font.sansneoregular))
                        )
                    }
                    TextButton(
                        onClick = { onConfirm(linkText) },
                        enabled = linkText.isNotBlank()
                    ) {
                        Text(
                            text = "추가",
                            color = if (linkText.isNotBlank()) Color(0xFF212121) else Color(0xFF9E9E9E),
                            fontFamily = FontFamily(Font(R.font.sansneoregular))
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LinkInputDialogPreview() {
    LinkInputDialog(
        onDismiss = {},
        onConfirm = {}
    )
}
