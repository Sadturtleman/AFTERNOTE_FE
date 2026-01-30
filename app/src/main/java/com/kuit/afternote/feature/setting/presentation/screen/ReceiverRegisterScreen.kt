package com.kuit.afternote.feature.setting.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.mainpage.presentation.component.edit.dropdown.DropdownMenuStyle
import com.kuit.afternote.feature.mainpage.presentation.component.edit.dropdown.SelectionDropdown
import com.kuit.afternote.feature.mainpage.presentation.component.edit.dropdown.rememberSelectionDropdownState
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray1

@Composable
fun ReceiverRegisterScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    val nameState = rememberTextFieldState()
    val phoneNumberState = rememberTextFieldState()
    var relationshipSelectedValue by remember { mutableStateOf("친구") }
    val relationshipOptions = listOf("친구", "가족", "연인", "동료", "기타")
    val dropdownState = rememberSelectionDropdownState()

    Scaffold(
        topBar = {
            TopBar(
                title = "수신자 등록",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "수신자 이름",
                    textFieldState = nameState,
                    placeholder = "이름을 입력하세요",
                    keyboardType = KeyboardType.Text,
                    containerColor = Gray1,
                    labelSpacing = 7.95.dp
                )

                SelectionDropdown(
                    label = "수신자와의 관계",
                    selectedValue = relationshipSelectedValue,
                    options = relationshipOptions,
                    onValueSelected = { relationshipSelectedValue = it },
                    menuStyle = DropdownMenuStyle(
                        menuOffset = 5.2.dp,
                        menuBackgroundColor = Gray1,
                        shadowElevation = 0.dp,
                        tonalElevation = 0.dp
                    ),
                    state = dropdownState
                )

                OutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "전화번호",
                    textFieldState = phoneNumberState,
                    placeholder = "전화번호를 입력하세요",
                    keyboardType = KeyboardType.Phone,
                    containerColor = Gray1,
                    labelSpacing = 7.95.dp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            ClickButton(
                color = B2,
                title = "등록하기",
                onButtonClick = {
                    // TODO: 수신자 등록 API 호출
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReceiverRegisterScreenPreview() {
    AfternoteTheme {
        ReceiverRegisterScreen(
            onBackClick = {}
        )
    }
}
