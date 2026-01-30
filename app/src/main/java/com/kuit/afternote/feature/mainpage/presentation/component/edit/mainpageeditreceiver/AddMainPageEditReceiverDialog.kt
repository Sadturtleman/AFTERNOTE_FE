package com.kuit.afternote.feature.mainpage.presentation.component.edit.mainpageeditreceiver

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
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.kuit.afternote.core.ui.component.LabeledTextFieldStyle
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.feature.mainpage.presentation.component.edit.dropdown.DropdownMenuStyle
import com.kuit.afternote.feature.mainpage.presentation.component.edit.dropdown.SelectionDropdown
import com.kuit.afternote.feature.mainpage.presentation.component.edit.dropdown.rememberSelectionDropdownState
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 수신자와의 관계 드롭다운 (로컬 상태 사용)
 */
@Composable
private fun RelationshipDropdown(
    selectedValue: String,
    options: List<String>,
    onValueSelected: (String) -> Unit,
    menuStyle: DropdownMenuStyle
) {
    val dropdownState = rememberSelectionDropdownState()

    SelectionDropdown(
        label = "수신자와의 관계",
        selectedValue = selectedValue,
        options = options,
        onValueSelected = onValueSelected,
        menuStyle = menuStyle,
        state = dropdownState
    )
}

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
fun AddMainPageEditReceiverDialog(
    modifier: Modifier = Modifier,
    params: AddMainPageEditReceiverDialogParams
) {
    Dialog(
        onDismissRequest = params.callbacks.onDismiss,
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
                    onClick = params.callbacks.onAddClick,
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
                            fontWeight = FontWeight.Medium,
                            color = Gray9
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 수신자 이름 입력 필드
            OutlineTextField(
                label = "수신자 이름",
                textFieldState = params.mainPageEditReceiverNameState,
                keyboardType = KeyboardType.Text,
                style = LabeledTextFieldStyle(containerColor = Gray1, labelSpacing = 7.95.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 수신자와의 관계 드롭다운
            RelationshipDropdown(
                selectedValue = params.relationshipSelectedValue,
                options = params.relationshipOptions,
                onValueSelected = params.callbacks.onRelationshipSelected,
                menuStyle = DropdownMenuStyle(
                    menuOffset = 5.2.dp,
                    menuBackgroundColor = Gray1,
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 전화번호로 추가하기 입력 필드
            OutlineTextField(
                label = "전화번호로 추가하기",
                textFieldState = params.phoneNumberState,
                keyboardType = KeyboardType.Phone,
                style = LabeledTextFieldStyle(containerColor = Gray1, labelSpacing = 7.95.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "연락처에서 추가하기",
                // CaptionLarge-R
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray9,
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 연락처에서 추가하기 버튼
            ClickButton(
                color = B3,
                title = "연락처 가져오기",
                onButtonClick = params.callbacks.onImportContactsClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMainPageEditReceiverDialogPreview() {
    AfternoteTheme {
        AddMainPageEditReceiverDialog(
            params = AddMainPageEditReceiverDialogParams(
                mainPageEditReceiverNameState = rememberTextFieldState(),
                phoneNumberState = rememberTextFieldState(),
                relationshipSelectedValue = "친구",
                relationshipOptions = listOf("친구", "가족", "연인")
            )
        )
    }
}
