package com.kuit.afternote.feature.setting.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuit.afternote.core.ui.component.ClickButton
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.PasswordChangeViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import kotlinx.coroutines.launch

private const val PLACEHOLDER_TEXT_FIELD = "Text Field"

@Composable
fun PasswordChangeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: PasswordChangeViewModel = viewModel()
) {
    val currentPasswordState = rememberTextFieldState()
    val newPasswordState = rememberTextFieldState()
    val confirmPasswordState = rememberTextFieldState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.passwordChangeSuccess) {
        if (uiState.passwordChangeSuccess) {
            snackbarHostState.showSnackbar("비밀번호가 변경되었습니다.")
            viewModel.clearPasswordChangeSuccess()
            onBackClick()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    PasswordChangeContent(
        modifier = modifier,
        currentPasswordState = currentPasswordState,
        newPasswordState = newPasswordState,
        confirmPasswordState = confirmPasswordState,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onChangePasswordClick = { currentPassword, newPassword, confirmPassword ->
            if (newPassword != confirmPassword) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("새 비밀번호가 일치하지 않습니다.")
                }
                return@PasswordChangeContent
            }
            viewModel.changePassword(currentPassword, newPassword)
        }
    )
}

@Composable
private fun PasswordChangeContent(
    modifier: Modifier = Modifier,
    currentPasswordState: TextFieldState,
    newPasswordState: TextFieldState,
    confirmPasswordState: TextFieldState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackClick: () -> Unit = {},
    onChangePasswordClick: (currentPassword: String, newPassword: String, confirmPassword: String) -> Unit = { _, _, _ -> }
) {
    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
        topBar = {
            TopBar(
                title = "비밀번호 변경",
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            // 안내 문구 섹션
            PasswordGuideSection()

            Spacer(modifier = Modifier.height(24.dp))

            // 비밀번호 입력 필드들
            PasswordFieldsSection(
                currentPasswordState = currentPasswordState,
                newPasswordState = newPasswordState,
                confirmPasswordState = confirmPasswordState
            )

            // 화면 높이의 10% (800dp 화면 기준 약 80dp)
            Spacer(modifier = Modifier.height(screenHeight * 0.1f))

            // 비밀번호 변경하기 버튼
            ClickButton(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = B3,
                title = "비밀번호 변경하기",
                onButtonClick = {
                    val currentPassword = currentPasswordState.text.toString()
                    val newPassword = newPasswordState.text.toString()
                    val confirmPassword = confirmPasswordState.text.toString()
                    onChangePasswordClick(currentPassword, newPassword, confirmPassword)
                }
            )
        }
    }
}

@Composable
private fun PasswordGuideSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // 메인 안내 문구
        Text(
            text = "안전한 비밀번호로 내 정보를 보호하세요.",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 비밀번호 요구사항 목록
        PasswordRequirementItem(
            text = "8 ~ 16자의 영문 대소문자, 숫자, 특수문자를 조합하여 설정해 주세요."
        )
        PasswordRequirementItem(
            text = "이전에 사용한 적 없는 비밀번호가 안전합니다."
        )
    }
}

@Composable
private fun PasswordRequirementItem(
    modifier: Modifier = Modifier,
    text: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        // 파란 점 (Bullet)
        Canvas(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(4.dp)
        ) {
            drawCircle(color = B1)
        }

        Text(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                color = B1
            )
        )
    }
}

@Composable
private fun PasswordFieldsSection(
    modifier: Modifier = Modifier,
    currentPasswordState: TextFieldState,
    newPasswordState: TextFieldState,
    confirmPasswordState: TextFieldState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(space = 20.dp)
    ) {
        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "현재 비밀번호",
            textFieldState = currentPasswordState,
            placeholder = PLACEHOLDER_TEXT_FIELD,
            keyboardType = KeyboardType.Password
        )

        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "새 비밀번호",
            textFieldState = newPasswordState,
            placeholder = PLACEHOLDER_TEXT_FIELD,
            keyboardType = KeyboardType.Password
        )

        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "새 비밀번호 확인",
            textFieldState = confirmPasswordState,
            placeholder = PLACEHOLDER_TEXT_FIELD,
            keyboardType = KeyboardType.Password
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordChangeScreenPreview() {
    AfternoteTheme {
        PasswordChangeContent(
            currentPasswordState = rememberTextFieldState(),
            newPasswordState = rememberTextFieldState(),
            confirmPasswordState = rememberTextFieldState()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordGuideSectionPreview() {
    AfternoteTheme {
        PasswordGuideSection()
    }
}
