package com.kuit.afternote.feature.setting.presentation.screen.receiver

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.LabelStyle
import com.kuit.afternote.core.ui.component.LabeledTextFieldStyle
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.mainpage.presentation.component.edit.dropdown.DropdownMenuStyle
import com.kuit.afternote.feature.mainpage.presentation.component.edit.dropdown.SelectionDropdown
import com.kuit.afternote.feature.mainpage.presentation.component.edit.dropdown.rememberSelectionDropdownState
import com.kuit.afternote.feature.user.presentation.viewmodel.RegisterReceiverViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.White

@Composable
fun ReceiverRegisterScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit = {},
    onAddProfileImageClick: () -> Unit = {},
    registerViewModel: RegisterReceiverViewModel? = null
) {
    val nameState = rememberTextFieldState()
    val phoneNumberState = rememberTextFieldState()
    val emailState = rememberTextFieldState()
    var relationshipSelectedValue by remember { mutableStateOf("딸") }
    val relationshipOptions = listOf("딸", "아들", "친구", "가족", "연인", "동료", "기타")
    val dropdownState = rememberSelectionDropdownState()

    val onRegisterAction: () -> Unit = {
        if (registerViewModel != null) {
            registerViewModel.registerReceiver(
                name = nameState.text.toString().trim(),
                relation = relationshipSelectedValue,
                phone = phoneNumberState.text.toString().trim().takeIf { it.isNotBlank() },
                email = emailState.text.toString().trim().takeIf { it.isNotBlank() }
            )
        } else {
            onRegisterClick()
        }
    }

    BackHandler(onBack = onBackClick)
    Scaffold(
        containerColor = Gray1,
        topBar = {
            TopBar(
                title = "수신자 등록",
                onBackClick = onBackClick,
                onActionClick = onRegisterAction,
                actionText = "등록"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Image with Add Button
            ProfileImageWithAddButton(
                onAddClick = onAddProfileImageClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Form Fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 이름
                OutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "이름",
                    textFieldState = nameState,
                    placeholder = "이름",
                    keyboardType = KeyboardType.Text,
                    style = LabeledTextFieldStyle(
                        containerColor = White,
                        labelSpacing = 11.dp,
                        labelFontSize = 16.sp,
                        labelLineHeight = 22.sp,
                        labelFontWeight = FontWeight.Medium
                    )
                )

                // 전화번호
                OutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "전화번호",
                    textFieldState = phoneNumberState,
                    placeholder = "010-1234-1234",
                    keyboardType = KeyboardType.Phone,
                    style = LabeledTextFieldStyle(
                        containerColor = White,
                        labelSpacing = 11.dp,
                        labelFontSize = 16.sp,
                        labelLineHeight = 22.sp,
                        labelFontWeight = FontWeight.Medium
                    )
                )

                // 관계 (Required)
                SelectionDropdown(
                    label = "관계",
                    isRequired = true,
                    labelStyle = LabelStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium
                    ),
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

                // 이메일
                OutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "이메일",
                    textFieldState = emailState,
                    placeholder = "example@email.com",
                    keyboardType = KeyboardType.Email,
                    style = LabeledTextFieldStyle(
                        containerColor = White,
                        labelSpacing = 11.dp,
                        labelFontSize = 16.sp,
                        labelLineHeight = 22.sp,
                        labelFontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileImageWithAddButton(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Profile Image
        Image(
            painter = painterResource(R.drawable.img_profile),
            contentDescription = "프로필 이미지",
            modifier = Modifier
                .size(135.dp)
                .clickable(onClick = onAddClick)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReceiverRegisterScreenPreview() {
    AfternoteTheme {
        ReceiverRegisterScreen(
            onBackClick = {},
            onRegisterClick = {},
            onAddProfileImageClick = {}
        )
    }
}
