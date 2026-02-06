package com.kuit.afternote.feature.dev.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.BuildConfig
import com.kuit.afternote.R
import com.kuit.afternote.app.compositionlocal.DataProviderLocals
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.feature.dev.presentation.viewmodel.DevModeUiState
import com.kuit.afternote.feature.dev.presentation.viewmodel.DevModeViewModel

data class ScreenInfo(
    val name: String,
    val route: String
)

/**
 * Callbacks for [DevModeContent] (login, logout, navigation, etc.).
 */
data class DevModeContentCallbacks(
    val onScreenClick: (String) -> Unit,
    val onUserModeClick: () -> Unit,
    val onQuickLoginClick: () -> Unit,
    val onLogoutClick: () -> Unit,
    val onCreateQuickTestAccountClick: () -> Unit,
    val onPasswordCycleClick: (currentPassword: String?, newPassword: String) -> Unit
)

@Composable
fun DevModeScreen(
    screens: List<ScreenInfo>,
    onScreenClick: (String) -> Unit,
    onUserModeClick: () -> Unit = {},
    viewModel: DevModeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        DevModeContent(
            screens = screens,
            uiState = uiState,
            callbacks = DevModeContentCallbacks(
                onScreenClick = onScreenClick,
                onUserModeClick = onUserModeClick,
                onQuickLoginClick = {
                    val email = BuildConfig.TEST_EMAIL
                    val password = BuildConfig.TEST_PASSWORD
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.quickLogin(email, password)
                    }
                },
                onLogoutClick = { viewModel.logout() },
                onCreateQuickTestAccountClick = { viewModel.createQuickTestAccount() },
                onPasswordCycleClick = { currentPassword, newPassword ->
                    viewModel.cyclePassword(newPassword, currentPassword)
                }
            )
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun DevModeContent(
    screens: List<ScreenInfo>,
    uiState: DevModeUiState,
    callbacks: DevModeContentCallbacks
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        // 헤더 (전체 너비)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "개발자 모드",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // 로그인 상태 카드 (전체 너비)
        item(span = { GridItemSpan(maxLineSpan) }) {
            LoginStatusCard(
                uiState = uiState,
                onQuickLoginClick = callbacks.onQuickLoginClick,
                onLogoutClick = callbacks.onLogoutClick,
                onCreateQuickTestAccountClick = callbacks.onCreateQuickTestAccountClick,
                onPasswordCycleClick = callbacks.onPasswordCycleClick
            )
        }

        // 사용자 모드로 이동 버튼 (전체 너비)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Button(
                onClick = callbacks.onUserModeClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("사용자 모드로 이동")
            }
        }

        // 데이터 소스: 목(mock) / 실제 전환 (전체 너비)
        item(span = { GridItemSpan(maxLineSpan) }) {
            DataProviderSwitchCard()
        }

        // 화면 버튼들 (2열 그리드)
        items(items = screens) { screen ->
            Button(
                onClick = { callbacks.onScreenClick(screen.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = screen.name,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun DataProviderSwitchCard(modifier: Modifier = Modifier) {
    val switch = DataProviderLocals.LocalDataProviderSwitch.current ?: return
    val useFake by switch.useFakeState.collectAsStateWithLifecycle(
        initialValue = switch.getInitialUseFake()
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(size = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.dev_use_mock_data),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = useFake,
                onCheckedChange = { switch.setUseFake(it) }
            )
        }
    }
}

// Color constants for login status
private val LoggedInBackgroundColor = Color(color = 0xFFE8F5E9)
private val LoggedOutBackgroundColor = Color(color = 0xFFFFEBEE)
private val LoggedInIndicatorColor = Color(color = 0xFF4CAF50)
private val LoggedOutIndicatorColor = Color(color = 0xFFF44336)
private val LoggedInTextColor = Color(color = 0xFF2E7D32)
private val LoggedOutTextColor = Color(color = 0xFFC62828)

@Composable
private fun LoginStatusCard(
    uiState: DevModeUiState,
    onQuickLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onCreateQuickTestAccountClick: () -> Unit,
    onPasswordCycleClick: (currentPassword: String?, newPassword: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (uiState.isLoggedIn) LoggedInBackgroundColor else LoggedOutBackgroundColor

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(size = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            LoginStatusHeader(uiState = uiState)
            Spacer(modifier = Modifier.height(12.dp))
            LoginActionButton(
                uiState = uiState,
                onQuickLoginClick = onQuickLoginClick,
                onLogoutClick = onLogoutClick,
                onCreateQuickTestAccountClick = onCreateQuickTestAccountClick,
                onPasswordCycleClick = onPasswordCycleClick
            )
            TestAccountInfo(isLoggedIn = uiState.isLoggedIn)
        }
    }
}

@Composable
private fun LoginStatusHeader(uiState: DevModeUiState) {
    val indicatorColor = if (uiState.isLoggedIn) LoggedInIndicatorColor else LoggedOutIndicatorColor
    val statusText = if (uiState.isLoggedIn) "로그인됨" else "로그아웃됨"
    val textColor = if (uiState.isLoggedIn) LoggedInTextColor else LoggedOutTextColor

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(indicatorColor)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = statusText,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = textColor
            )
            UserEmailText(uiState = uiState)
        }

        LoadingIndicator(isLoading = uiState.isLoading)
    }
}

@Composable
private fun UserEmailText(uiState: DevModeUiState) {
    if (uiState.isLoggedIn && !uiState.userEmail.isNullOrEmpty()) {
        Text(
            text = uiState.userEmail,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun LoadingIndicator(isLoading: Boolean) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            strokeWidth = 2.dp
        )
    }
}

@Composable
@Suppress("AssignedValueIsNeverRead")
private fun LoginActionButton(
    uiState: DevModeUiState,
    onQuickLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onCreateQuickTestAccountClick: () -> Unit,
    onPasswordCycleClick: (currentPassword: String?, newPassword: String) -> Unit
) {
    var showPasswordCycleDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        if (uiState.isLoggedIn) {
            LogoutButton(
                isLoading = uiState.isLoading,
                onClick = onLogoutClick
            )
            PasswordCycleButton(
                isLoading = uiState.isLoading,
                onClick = { showPasswordCycleDialog = true }
            )
        } else {
            QuickLoginButton(
                isLoading = uiState.isLoading,
                onClick = onQuickLoginClick
            )
            CreateQuickTestAccountButton(
                isLoading = uiState.isLoading,
                onClick = onCreateQuickTestAccountClick
            )
        }
    }

    if (showPasswordCycleDialog) {
        PasswordCycleDialog(
            onDismiss = { showPasswordCycleDialog = false },
            onConfirm = { currentPassword, newPassword ->
                onPasswordCycleClick(currentPassword, newPassword)
                showPasswordCycleDialog = false
            }
        )
    }
}

@Composable
private fun RowScope.LogoutButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        enabled = !isLoading
    ) {
        Text("로그아웃")
    }
}

@Composable
private fun RowScope.QuickLoginButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    val hasCredentials = BuildConfig.TEST_EMAIL.isNotEmpty() &&
        BuildConfig.TEST_PASSWORD.isNotEmpty()
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        enabled = !isLoading && hasCredentials
    ) {
        Text("테스트 로그인")
    }
}

@Composable
private fun RowScope.CreateQuickTestAccountButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        enabled = !isLoading
    ) {
        Text("빠른 테스트 계정")
    }
}

@Composable
private fun RowScope.PasswordCycleButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        enabled = !isLoading
    ) {
        Text("비밀번호 순환")
    }
}

@Composable
private fun PasswordCycleDialog(
    onDismiss: () -> Unit,
    onConfirm: (currentPassword: String?, newPassword: String) -> Unit
) {
    val currentPasswordState = rememberTextFieldState()
    val newPasswordState = rememberTextFieldState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "비밀번호 순환 (개발 모드)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "현재 비밀번호를 입력하거나 비워두면 저장된 값(또는 기본값)을 사용합니다.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlineTextField(
                    label = "현재 비밀번호 (선택)",
                    textFieldState = currentPasswordState,
                    keyboardType = KeyboardType.Password,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = "비워두면 저장된 값 사용"
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlineTextField(
                    label = "새 비밀번호",
                    textFieldState = newPasswordState,
                    keyboardType = KeyboardType.Password,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("취소")
                    }
                    Button(
                        onClick = {
                            if (newPasswordState.text.toString().isNotBlank()) {
                                val currentPassword = currentPasswordState.text.toString().takeIf { it.isNotBlank() }
                                onConfirm(currentPassword, newPasswordState.text.toString())
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = newPasswordState.text.toString().isNotBlank()
                    ) {
                        Text("확인")
                    }
                }
            }
        }
    }
}

@Composable
private fun TestAccountInfo(isLoggedIn: Boolean) {
    if (isLoggedIn) return

    Spacer(modifier = Modifier.height(8.dp))
    val testEmail = BuildConfig.TEST_EMAIL
    val testPassword = BuildConfig.TEST_PASSWORD
    if (testEmail.isNotEmpty() && testPassword.isNotEmpty()) {
        Text(
            text = "테스트 계정: $testEmail",
            fontSize = 10.sp,
            color = Color.Gray
        )
    } else {
        Text(
            text = "local.properties에 TEST_EMAIL, TEST_PASSWORD 설정 후 앱 재빌드 필요",
            fontSize = 10.sp,
            color = Color.Gray
        )
    }
}
