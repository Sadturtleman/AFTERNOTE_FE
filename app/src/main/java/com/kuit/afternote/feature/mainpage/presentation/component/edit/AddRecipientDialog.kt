package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 수신자 추가 다이얼로그 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 헤더: "수신자 추가" 타이틀, 우측 상단 "추가하기" 버튼 (B3 배경, 흰색 텍스트)
 * - 수신자 이름 입력 필드
 * - 수신자와의 관계 드롭다운
 * - 전화번호로 추가하기 입력 필드
 * - 연락처에서 추가하기 버튼 (B3 배경)
 */
@Composable
fun AddRecipientDialog(
    modifier: Modifier = Modifier,
    recipientNameState: TextFieldState,
    relationshipSelectedValue: String,
    relationshipOptions: List<String>,
    phoneNumberState: TextFieldState,
    onDismiss: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onRelationshipSelected: (String) -> Unit = {},
    onImportContactsClick: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .dropShadow(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.15f),
                    blur = 10.dp,
                    offsetX = 0.dp,
                    offsetY = 2.dp,
                    spread = 0.dp
                ).background(
                    White,
                    RoundedCornerShape(16.dp)
                ).padding(vertical = 32.dp, horizontal = 24.dp)
        ) {
            // 헤더: 타이틀과 추가하기 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "수신자 추가",
                    style = TextStyle(
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Bold,
                        color = Gray9
                    )
                )

                Button(
                    onClick = onAddClick,
                    modifier = Modifier.height(32.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = B3
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                ) {
                    Text(
                        text = "추가하기",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            color = Gray9
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 수신자 이름 입력 필드
            RecipientInputField(
                label = "수신자 이름",
                textFieldState = recipientNameState,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 수신자와의 관계 드롭다운
            SelectionDropdown(
                label = "수신자와의 관계",
                selectedValue = relationshipSelectedValue,
                options = relationshipOptions,
                onValueSelected = onRelationshipSelected
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 전화번호로 추가하기 입력 필드
            RecipientInputField(
                label = "전화번호로 추가하기",
                textFieldState = phoneNumberState,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "연락처에서 추가하기",
                // CaptionLarge-R
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight(400),
                    color = Gray9,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 연락처에서 추가하기 버튼
            Button(
                onClick = onImportContactsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = B3
                )
            ) {
                Text(
                    text = "연락처 가져오기",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                )
            }
        }
    }
}

/**
 * 수신자 입력 필드 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 라벨: 12sp, Regular, Gray9
 * - 텍스트 필드: Gray1 배경, 8dp 모서리, 56dp 높이
 */
@Composable
private fun RecipientInputField(
    label: String,
    textFieldState: TextFieldState,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 7.95.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                color = Gray9
            )
        )

        OutlinedTextField(
            contentPadding = PaddingValues(
                vertical = 16.dp,
                horizontal = 24.dp
            ),
            state = textFieldState,
            lineLimits = TextFieldLineLimits.SingleLine,
            placeholder = {
                Text(
                    text = "Text Field",
                    fontSize = 16.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray4,
                    lineHeight = 20.sp
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = Gray1,
                focusedContainerColor = Gray1
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Gray1, RoundedCornerShape(8.dp)),
            textStyle = TextStyle(
                color = Gray9
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddRecipientDialogPreview() {
    AfternoteTheme {
        AddRecipientDialog(
            recipientNameState = rememberTextFieldState(),
            relationshipSelectedValue = "친구",
            relationshipOptions = listOf("친구", "가족", "동료", "지인"),
            phoneNumberState = rememberTextFieldState(),
            onDismiss = {},
            onAddClick = {},
            onRelationshipSelected = {},
            onImportContactsClick = {}
        )
    }
}
