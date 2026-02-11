package com.kuit.afternote.feature.onboarding.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.ProfileImage
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.onboarding.presentation.viewmodel.SignUpViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2

@Composable
fun ProfileSettingScreen(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    onFinishClick: () -> Unit,
    onBackClick: () -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val name = rememberTextFieldState()
    val uiState by signUpViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.signUpSuccess) {
        if (uiState.signUpSuccess) {
            android.util.Log.d("ProfileSettingScreen", "회원가입 성공!")
            signUpViewModel.clearSignUpSuccess()
            onFinishClick()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            android.util.Log.e("ProfileSettingScreen", "회원가입 실패: $error")
            snackBarHostState.showSnackbar(error)
            signUpViewModel.clearError()
        }
    }

    val profileImagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            signUpViewModel.setSelectedProfileImageUri(uri)
        }

    ProfileSettingContent(
        modifier = modifier,
        nameState = name,
        pickedProfileImageUri = uiState.pickedProfileImageUri,
        onProfileImageEditClick = {
            profileImagePickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        },
        onBackClick = onBackClick,
        snackBarHostState = snackBarHostState
    ) {
        val nameText = name.text.toString().trim()
        android.util.Log.d("ProfileSettingScreen", "회원 가입 완료 버튼 클릭됨: email=$email, name=$nameText")
        signUpViewModel.signUp(
            email = email,
            password = password,
            name = nameText,
            profileUrl = null
        )
    }
}

@Composable
private fun ProfileSettingContent(
    modifier: Modifier = Modifier,
    nameState: androidx.compose.foundation.text.input.TextFieldState,
    pickedProfileImageUri: String? = null,
    onProfileImageEditClick: () -> Unit = {},
    onBackClick: () -> Unit,
    snackBarHostState: SnackbarHostState,
    onSignUpClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "프로필 설정",
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.4f))

            ProfileImage(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                displayImageUri = pickedProfileImageUri,
                onEditClick = onProfileImageEditClick
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlineTextField(
                textFieldState = nameState,
                label = "이름을 지정해주세요.",
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.weight(0.4f))

            ClickButton(
                color = B2,
                title = "회원 가입 완료",
                onButtonClick = onSignUpClick
            )

            Spacer(modifier = Modifier.weight(0.7f))
        }
    }
}

@Preview
@Composable
private fun ProfileSettingScreenPreview() {
    AfternoteTheme {
        val nameState = rememberTextFieldState()
        val snackBarHostState = remember { SnackbarHostState() }

        ProfileSettingContent(
            nameState = nameState,
            pickedProfileImageUri = null,
            onProfileImageEditClick = {},
            onBackClick = {},
            snackBarHostState = snackBarHostState,
            onSignUpClick = {}
        )
    }
}
