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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuit.afternote.core.ui.component.ClickButton
import kotlinx.coroutines.launch
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.TopBar
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.PasswordChangeViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2

@Composable
fun PasswordChangeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "현재 비밀번호",
                    textFieldState = currentPasswordState,
                    placeholder = "현재 비밀번호를 입력하세요",
                    keyboardType = KeyboardType.Password
                )

                OutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "새 비밀번호",
                    textFieldState = newPasswordState,
                    placeholder = "새 비밀번호를 입력하세요",
                    keyboardType = KeyboardType.Password
                )

                OutlineTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = "새 비밀번호 확인",
                    textFieldState = confirmPasswordState,
                    placeholder = "새 비밀번호를 다시 입력하세요",
                    keyboardType = KeyboardType.Password
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            ClickButton(
                color = B2,
                title = "변경하기",
                onButtonClick = {
                    val currentPassword = currentPasswordState.text.toString()
                    val newPassword = newPasswordState.text.toString()
                    val confirmPassword = confirmPasswordState.text.toString()

                    if (newPassword != confirmPassword) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("새 비밀번호가 일치하지 않습니다.")
                        }
                        return@ClickButton
                    }

                    viewModel.changePassword(currentPassword, newPassword)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordChangeScreenPreview() {
    AfternoteTheme {
        PasswordChangeScreen(
            onBackClick = {}
        )
    }
}
