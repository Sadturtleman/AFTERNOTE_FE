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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.BuildConfig
import com.kuit.afternote.feature.dev.presentation.viewmodel.DevModeUiState
import com.kuit.afternote.feature.dev.presentation.viewmodel.DevModeViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme

data class ScreenInfo(
    val name: String,
    val route: String
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
            onScreenClick = onScreenClick,
            onUserModeClick = onUserModeClick,
            onQuickLoginClick = {
                val email = BuildConfig.TEST_EMAIL
                val password = BuildConfig.TEST_PASSWORD
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.quickLogin(email, password)
                }
            },
            onLogoutClick = { viewModel.logout() }
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
    onScreenClick: (String) -> Unit,
    onUserModeClick: () -> Unit,
    onQuickLoginClick: () -> Unit,
    onLogoutClick: () -> Unit
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
                onQuickLoginClick = onQuickLoginClick,
                onLogoutClick = onLogoutClick
            )
        }

        // 사용자 모드로 이동 버튼 (전체 너비)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Button(
                onClick = onUserModeClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("사용자 모드로 이동")
            }
        }

        // 화면 버튼들 (2열 그리드)
        items(items = screens) { screen ->
            Button(
                onClick = { onScreenClick(screen.route) },
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
                onLogoutClick = onLogoutClick
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
private fun LoginActionButton(
    uiState: DevModeUiState,
    onQuickLoginClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        if (uiState.isLoggedIn) {
            LogoutButton(
                isLoading = uiState.isLoading,
                onClick = onLogoutClick
            )
        } else {
            QuickLoginButton(
                isLoading = uiState.isLoading,
                onClick = onQuickLoginClick
            )
        }
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
private fun TestAccountInfo(isLoggedIn: Boolean) {
    if (isLoggedIn) return

    Spacer(modifier = Modifier.height(8.dp))
    val testEmail = BuildConfig.TEST_EMAIL
    if (testEmail.isNotEmpty()) {
        Text(
            text = "테스트 계정: $testEmail",
            fontSize = 10.sp,
            color = Color.Gray
        )
    } else {
        Text(
            text = "local.properties에 TEST_EMAIL, TEST_PASSWORD 설정 필요",
            fontSize = 10.sp,
            color = Color.Red
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DevModeScreenLoggedOutPreview() {
    AfternoteTheme {
        DevModeContent(
            screens = listOf(
                ScreenInfo("메인 화면 (빈 상태)", "main_empty"),
                ScreenInfo("메인 화면 (목록 있음)", "main_with_items"),
                ScreenInfo("애프터노트 상세 화면", "afternote_detail"),
                ScreenInfo("설정 화면", "setting_main")
            ),
            uiState = DevModeUiState(isLoggedIn = false),
            onScreenClick = {},
            onUserModeClick = {},
            onQuickLoginClick = {},
            onLogoutClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DevModeScreenLoggedInPreview() {
    AfternoteTheme {
        DevModeContent(
            screens = listOf(
                ScreenInfo("메인 화면 (빈 상태)", "main_empty"),
                ScreenInfo("메인 화면 (목록 있음)", "main_with_items"),
                ScreenInfo("애프터노트 상세 화면", "afternote_detail"),
                ScreenInfo("설정 화면", "setting_main")
            ),
            uiState = DevModeUiState(
                isLoggedIn = true,
                userEmail = "test@example.com"
            ),
            onScreenClick = {},
            onUserModeClick = {},
            onQuickLoginClick = {},
            onLogoutClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginStatusCardLoggedOutPreview() {
    AfternoteTheme {
        LoginStatusCard(
            uiState = DevModeUiState(isLoggedIn = false),
            onQuickLoginClick = {},
            onLogoutClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginStatusCardLoggedInPreview() {
    AfternoteTheme {
        LoginStatusCard(
            uiState = DevModeUiState(
                isLoggedIn = true,
                userEmail = "test@example.com"
            ),
            onQuickLoginClick = {},
            onLogoutClick = {}
        )
    }
}
