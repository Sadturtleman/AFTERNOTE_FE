package com.kuit.afternote.feature.setting.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

private const val PLACEHOLDER_TEXT_FIELD = "Text Field"

/**
 * 프로필 수정 화면 콜백 그룹
 */
data class ProfileEditCallbacks(
    val onBackClick: () -> Unit = {},
    val onRegisterClick: () -> Unit = {},
    val onProfileImageClick: () -> Unit = {},
    val onEditProfileClick: () -> Unit = {},
    val onChangeEmailClick: () -> Unit = {},
    val onWithdrawClick: () -> Unit = {}
)

@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    nameState: TextFieldState = rememberTextFieldState(),
    contactState: TextFieldState = rememberTextFieldState(),
    emailState: TextFieldState = rememberTextFieldState(),
    callbacks: ProfileEditCallbacks = ProfileEditCallbacks()
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopBar(
                title = "프로필 수정",
                onBackClick = callbacks.onBackClick,
                onActionClick = callbacks.onRegisterClick,
                actionText = "등록"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // 프로필 섹션
            ProfileSection(
                onProfileImageClick = callbacks.onProfileImageClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 이름, 연락처 필드 및 수정하기 버튼
            ProfileInfoSection(
                nameState = nameState,
                contactState = contactState,
                onEditClick = callbacks.onEditProfileClick
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 구분선
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 25.dp),
                thickness = 1.dp,
                color = Gray3
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 이메일 섹션
            EmailSection(
                emailState = emailState,
                onChangeClick = callbacks.onChangeEmailClick
            )

            Spacer(modifier = Modifier.weight(1f))

            // 회원 탈퇴하기
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "회원 탈퇴하기",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            color = Gray4
                        ),
                        modifier = Modifier.clickable { callbacks.onWithdrawClick() }
                    )
                    HorizontalDivider(
                        modifier = Modifier.width(93.dp),
                        thickness = 1.dp,
                        color = Gray3
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileSection(
    modifier: Modifier = Modifier,
    onProfileImageClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "프로필",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.img_profile),
                contentDescription = "프로필 이미지",
                modifier = Modifier
                    .clickable { onProfileImageClick() }
                    .size(135.dp)
            )
        }
    }
}

@Composable
private fun ProfileInfoSection(
    modifier: Modifier = Modifier,
    nameState: TextFieldState,
    contactState: TextFieldState,
    onEditClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(space = 14.dp)
    ) {
        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "이름",
            textFieldState = nameState,
            placeholder = PLACEHOLDER_TEXT_FIELD,
            keyboardType = KeyboardType.Text
        )

        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "연락처",
            textFieldState = contactState,
            placeholder = PLACEHOLDER_TEXT_FIELD,
            keyboardType = KeyboardType.Phone
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SmallActionButton(
                text = "수정하기",
                onClick = onEditClick
            )
        }
    }
}

@Composable
private fun EmailSection(
    modifier: Modifier = Modifier,
    emailState: TextFieldState,
    onChangeClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(space = 14.dp)
    ) {
        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "이메일",
            textFieldState = emailState,
            placeholder = PLACEHOLDER_TEXT_FIELD,
            keyboardType = KeyboardType.Email
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            SmallActionButton(
                text = "변경하기",
                onClick = onChangeClick
            )
        }
    }
}

@Composable
private fun SmallActionButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                color = B3,
                shape = RoundedCornerShape(80.dp)
            ).clickable { onClick() }
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileEditScreenPreview() {
    AfternoteTheme {
        ProfileEditScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun SmallActionButtonPreview() {
    AfternoteTheme {
        SmallActionButton(
            text = "수정하기",
            onClick = {}
        )
    }
}
