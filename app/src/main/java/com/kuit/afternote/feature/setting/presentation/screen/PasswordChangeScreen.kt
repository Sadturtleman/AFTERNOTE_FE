package com.kuit.afternote.feature.setting.presentation.screen

import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.component.ClickButton
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.onboarding.presentation.util.PasswordValidator
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.PasswordChangeViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.ErrorRed
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

private const val PLACEHOLDER_TEXT_FIELD = "Text Field"

/**
 * 비밀번호 유효성을 검증하고 에러 상태를 반환합니다.
 *
 * @param newPassword 새 비밀번호
 * @param confirmPassword 비밀번호 확인
 * @return 검증 결과에 따른 에러 상태
 */
private fun validatePassword(newPassword: String, confirmPassword: String): PasswordErrorState {
    // 비밀번호 요구사항 검증
    if (PasswordValidator.validate(newPassword) != null) {
        return PasswordErrorState(requirementError = true)
    }

    // 비밀번호 일치 검증
    if (newPassword != confirmPassword) {
        return PasswordErrorState(mismatchError = true)
    }

    return PasswordErrorState()
}

/**
 * 비밀번호 입력 필드 상태를 그룹화한 데이터 클래스.
 */
private data class PasswordFieldStates(
    val currentPassword: TextFieldState,
    val newPassword: TextFieldState,
    val confirmPassword: TextFieldState
)

/**
 * 비밀번호 에러 상태를 그룹화한 데이터 클래스.
 */
private data class PasswordErrorState(
    val mismatchError: Boolean = false,
    val requirementError: Boolean = false
)

@Composable
fun PasswordChangeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: PasswordChangeViewModel = hiltViewModel()
) {
    val fieldStates = PasswordFieldStates(
        currentPassword = rememberTextFieldState(),
        newPassword = rememberTextFieldState(),
        confirmPassword = rememberTextFieldState()
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var errorState by remember { mutableStateOf(PasswordErrorState()) }

    LaunchedEffect(uiState.passwordChangeSuccess, uiState.needsRollback) {
        // Optimistic update 실패 시 navigation 취소
        if (uiState.needsRollback) {
            // Rollback: 성공 상태 취소 (navigation 방지)
            viewModel.clearPasswordChangeSuccess()
            viewModel.clearRollback()
            return@LaunchedEffect
        }
        
        // 성공 시에만 navigation
        if (uiState.passwordChangeSuccess && !uiState.needsRollback) {
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
        fieldStates = fieldStates,
        snackbarHostState = snackbarHostState,
        errorState = errorState,
        onBackClick = onBackClick,
        onChangePasswordClick = { currentPassword, newPassword, confirmPassword ->
            // 비밀번호 검증 및 에러 상태 업데이트
            errorState = validatePassword(newPassword, confirmPassword)

            // 에러가 없으면 비밀번호 변경 요청
            if (!errorState.requirementError && !errorState.mismatchError) {
                viewModel.changePassword(currentPassword, newPassword)
            }
        }
    )
}

@Composable
private fun PasswordChangeContent(
    modifier: Modifier = Modifier,
    fieldStates: PasswordFieldStates,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    errorState: PasswordErrorState = PasswordErrorState(),
    onBackClick: () -> Unit = {},
    onChangePasswordClick: (currentPassword: String, newPassword: String, confirmPassword: String) -> Unit = { _, _, _ -> }
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenHeight = with(density) { windowInfo.containerSize.height.toDp() }

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
            PasswordGuideSection(
                passwordRequirementError = errorState.requirementError
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 비밀번호 입력 필드들
            PasswordFieldsSection(
                currentPasswordState = fieldStates.currentPassword,
                newPasswordState = fieldStates.newPassword,
                confirmPasswordState = fieldStates.confirmPassword,
                newPasswordError = errorState.requirementError
            )

            // 에러 메시지 영역 (고정 높이 26dp = 8dp 상단 여백 + 18dp 텍스트 높이)
            Box(
                modifier = Modifier
                    .height(26.dp)
                    .padding(start = 20.dp, top = 8.dp)
            ) {
                if (errorState.mismatchError) {
                    Text(
                        text = "비밀번호가 일치하지 않습니다. 다시 시도해 주세요.",
                        style = TextStyle(
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            fontFamily = Sansneo,
                            fontWeight = FontWeight.Normal,
                            color = ErrorRed
                        )
                    )
                }
            }

            // 화면 높이의 10% (800dp 화면 기준 약 80dp) - 에러 메시지 영역 제외
            Spacer(modifier = Modifier.height(screenHeight * 0.1f - 26.dp))

            // 비밀번호 변경하기 버튼
            ClickButton(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = B3,
                title = "비밀번호 변경하기",
                onButtonClick = {
                    val currentPassword = fieldStates.currentPassword.text.toString()
                    val newPassword = fieldStates.newPassword.text.toString()
                    val confirmPassword = fieldStates.confirmPassword.text.toString()
                    onChangePasswordClick(currentPassword, newPassword, confirmPassword)
                }
            )
        }
    }
}

@Composable
private fun PasswordGuideSection(
    modifier: Modifier = Modifier,
    passwordRequirementError: Boolean = false
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
            text = "8 ~ 16자의 영문 대소문자, 숫자, 특수문자를 조합하여 설정해 주세요.",
            color = if (passwordRequirementError) ErrorRed else B1
        )
        PasswordRequirementItem(
            text = "이전에 사용한 적 없는 비밀번호가 안전합니다.",
            color = B1
        )
    }
}

@Composable
private fun PasswordRequirementItem(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = B1
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        // Bullet 점 - 텍스트 첫 줄 중앙에 맞춤 (fontSize 12sp 기준, 약 7dp 위치)
        Canvas(
            modifier = Modifier
                .size(4.dp)
                .align(Alignment.CenterVertically)
        ) {
            drawCircle(color = color)
        }

        Text(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                color = color
            )
        )
    }
}

@Composable
private fun PasswordFieldsSection(
    modifier: Modifier = Modifier,
    currentPasswordState: TextFieldState,
    newPasswordState: TextFieldState,
    confirmPasswordState: TextFieldState,
    newPasswordError: Boolean = false
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
            keyboardType = KeyboardType.Password,
            isError = newPasswordError
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
            fieldStates = PasswordFieldStates(
                currentPassword = rememberTextFieldState(),
                newPassword = rememberTextFieldState(),
                confirmPassword = rememberTextFieldState()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordChangeScreenWithMismatchErrorPreview() {
    AfternoteTheme {
        PasswordChangeContent(
            fieldStates = PasswordFieldStates(
                currentPassword = rememberTextFieldState(),
                newPassword = rememberTextFieldState(),
                confirmPassword = rememberTextFieldState()
            ),
            errorState = PasswordErrorState(mismatchError = true)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordChangeScreenWithRequirementErrorPreview() {
    AfternoteTheme {
        PasswordChangeContent(
            fieldStates = PasswordFieldStates(
                currentPassword = rememberTextFieldState(),
                newPassword = rememberTextFieldState(),
                confirmPassword = rememberTextFieldState()
            ),
            errorState = PasswordErrorState(requirementError = true)
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

@Preview(showBackground = true)
@Composable
private fun PasswordGuideSectionWithErrorPreview() {
    AfternoteTheme {
        PasswordGuideSection(passwordRequirementError = true)
    }
}
