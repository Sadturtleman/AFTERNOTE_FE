package com.kuit.afternote.feature.setting.presentation.screen.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.component.LabeledTextFieldStyle
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.core.ui.component.ProfileImage
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.user.presentation.uimodel.ProfileUiState
import com.kuit.afternote.feature.user.presentation.viewmodel.ProfileEditViewModelContract
import com.kuit.afternote.feature.user.presentation.viewmodel.ProfileViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val PLACEHOLDER_TEXT_FIELD = "Text Field"
private const val TAG_PROFILE_EDIT = "ProfileEdit"

private val PROFILE_EDIT_LABELED_STYLE = LabeledTextFieldStyle(
    labelFontSize = 14.sp,
    labelLineHeight = 20.sp,
    labelColor = Color(0xFF000000)
)

/**
 * 프로필 수정 화면 텍스트 필드 상태 그룹 (LongParameterList 해결)
 */
data class ProfileEditFormState(
    val nameState: TextFieldState,
    val contactState: TextFieldState,
    val emailState: TextFieldState
)

/**
 * 프로필 이미지 섹션 파라미터 (LongParameterList 해결)
 */
data class ProfileSectionParams(
    val savedProfileImageUrl: String?,
    val pickedProfileImageUri: String?,
    val onProfileImageEditClick: () -> Unit
)

/**
 * 프로필 수정 화면 콜백 그룹
 */
data class ProfileEditCallbacks(
    val onBackClick: () -> Unit = {},
    val onRegisterClick: () -> Unit = {},
    val onChangeEmailClick: () -> Unit = {},
    val onWithdrawClick: () -> Unit = {},
    val onUpdateSuccess: () -> Unit = {}
)

@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileEditViewModelContract = hiltViewModel<ProfileViewModel>(),
    callbacks: ProfileEditCallbacks = ProfileEditCallbacks()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val nameState = rememberTextFieldState()
    val contactState = rememberTextFieldState()
    val emailState = rememberTextFieldState()

    val profileImagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            viewModel.setSelectedProfileImageUri(uri)
        }

    LaunchedEffect(uiState.name) {
        Log.d(TAG_PROFILE_EDIT, "LaunchedEffect(name): uiState.name='${uiState.name}' syncing to field")
        if (nameState.text != uiState.name) {
            nameState.edit { replace(0, length, uiState.name) }
        }
    }
    LaunchedEffect(uiState.phone) {
        val phone = uiState.phone ?: ""
        if (contactState.text != phone) {
            contactState.edit { replace(0, length, phone) }
        }
    }
    LaunchedEffect(uiState.email) {
        Log.d(TAG_PROFILE_EDIT, "LaunchedEffect(email): uiState.email='${uiState.email}' syncing to field")
        if (emailState.text != uiState.email) {
            emailState.edit { replace(0, length, uiState.email) }
        }
    }
    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            viewModel.clearUpdateSuccess()
            callbacks.onUpdateSuccess()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

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
        ProfileEditContent(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            scrollState = scrollState,
            formState = ProfileEditFormState(
                nameState = nameState,
                contactState = contactState,
                emailState = emailState
            ),
            callbacks = callbacks,
            profileSectionParams = ProfileSectionParams(
                savedProfileImageUrl = uiState.savedProfileImageUrl,
                pickedProfileImageUri = uiState.pickedProfileImageUri,
                onProfileImageEditClick = {
                    profileImagePickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ),
            onEditClick = {
                viewModel.updateProfile(
                    name = nameState.text.toString().ifBlank { null },
                    phone = contactState.text.toString().ifBlank { null },
                    profileImageUrl = uiState.savedProfileImageUrl
                )
            }
        )
    }
}

@Composable
private fun ProfileEditContent(
    modifier: Modifier,
    scrollState: ScrollState,
    formState: ProfileEditFormState,
    callbacks: ProfileEditCallbacks,
    profileSectionParams: ProfileSectionParams,
    onEditClick: () -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        ProfileEditScrollColumn(
            scrollState = scrollState,
            maxHeight = maxHeight,
            formState = formState,
            callbacks = callbacks,
            profileSectionParams = profileSectionParams,
            onEditClick = onEditClick
        )
    }
}

@Composable
private fun ProfileEditScrollColumn(
    scrollState: ScrollState,
    maxHeight: Dp,
    formState: ProfileEditFormState,
    callbacks: ProfileEditCallbacks,
    profileSectionParams: ProfileSectionParams,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // 프로필 섹션
        ProfileSection(
            savedProfileImageUrl = profileSectionParams.savedProfileImageUrl,
            pickedProfileImageUri = profileSectionParams.pickedProfileImageUri,
            onProfileImageEditClick = profileSectionParams.onProfileImageEditClick
        )

        Spacer(modifier = Modifier.height((maxHeight.value * 0.056f).dp))

        // 이름, 연락처 필드 및 수정하기 버튼
        ProfileInfoSection(
            nameState = formState.nameState,
            contactState = formState.contactState,
            onEditClick = onEditClick
        )

        Spacer(modifier = Modifier.height((maxHeight.value * 0.08f).dp))

        // 구분선
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 25.dp),
            thickness = 1.dp,
            color = Gray3
        )

        Spacer(modifier = Modifier.height((maxHeight.value * 0.05f).dp))

        // 이메일 섹션
        EmailSection(
            emailState = formState.emailState,
            onChangeClick = callbacks.onChangeEmailClick
        )

        Spacer(Modifier.height((maxHeight.value * 0.225f).dp))
        // 회원 탈퇴하기
        Box(
            modifier = Modifier.fillMaxWidth(),
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
        Spacer(Modifier.height((maxHeight.value * 0.128f).dp))
    }
}

@Composable
private fun ProfileSection(
    modifier: Modifier = Modifier,
    savedProfileImageUrl: String? = null,
    pickedProfileImageUri: String? = null,
    onProfileImageEditClick: () -> Unit = {}
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
                color = Color(0xFF000000)
            )
        )

        Spacer(modifier = Modifier.height(29.dp))

        ProfileImage(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            displayImageUri = pickedProfileImageUri ?: savedProfileImageUrl,
            onEditClick = onProfileImageEditClick
        )
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
            .padding(horizontal = 20.dp)
    ) {
        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "이름",
            textFieldState = nameState,
            placeholder = PLACEHOLDER_TEXT_FIELD,
            keyboardType = KeyboardType.Text,
            style = PROFILE_EDIT_LABELED_STYLE
        )
        Spacer(Modifier.height(20.dp))
        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "연락처",
            textFieldState = contactState,
            placeholder = PLACEHOLDER_TEXT_FIELD,
            keyboardType = KeyboardType.Phone,
            style = PROFILE_EDIT_LABELED_STYLE
        )

        Spacer(Modifier.height(28.dp))
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
        verticalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            label = "이메일",
            textFieldState = emailState,
            placeholder = PLACEHOLDER_TEXT_FIELD,
            keyboardType = KeyboardType.Email,
            style = PROFILE_EDIT_LABELED_STYLE
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
            )
            .clickable { onClick() }
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

/**
 * Fake ViewModel for Preview so Hilt is not required.
 */
private class FakeProfileEditViewModel : ProfileEditViewModelContract {
    private val _uiState =
        MutableStateFlow(
            ProfileUiState(
                name = "김소희",
                email = "afternote@example.com",
                phone = "01012345678"
            )
        )
    override val uiState: StateFlow<ProfileUiState> = _uiState
    override fun loadProfile() {
        // No-op: Fake for Preview only; no API call.
    }

    override fun updateProfile(
        name: String?,
        phone: String?,
        profileImageUrl: String?
    ) {
        // No-op: Fake for Preview only; no state update.
    }

    override fun setSelectedProfileImageUri(uri: Uri?) {
        // No-op: Fake for Preview only.
    }

    override fun clearUpdateSuccess() {
        // No-op: Fake for Preview only.
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileEditScreenPreview() {
    AfternoteTheme {
        ProfileEditScreen(viewModel = remember { FakeProfileEditViewModel() })
    }
}
