package com.kuit.afternote.feature.setting.presentation.screen.notification

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.setting.presentation.component.ThumbSwitch
import com.kuit.afternote.feature.user.presentation.uimodel.PushSettingsUiState
import com.kuit.afternote.feature.user.presentation.viewmodel.PushSettingsViewModel
import com.kuit.afternote.feature.user.presentation.viewmodel.PushSettingsViewModelContract
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PushToastSettingScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: PushSettingsViewModelContract = hiltViewModel<PushSettingsViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadPushSettings()
    }

    BackHandler(onBack = onBackClick)
    Scaffold(
        topBar = {
            TopBar(
                title = "푸시 알림 설정",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                PushSettingRow(
                    name = "타임레터",
                    isChecked = uiState.timeLetter,
                    enabled = !uiState.isLoading,
                    onCheckedChange = {
                        viewModel.updatePushSettings(
                            timeLetter = it,
                            mindRecord = uiState.mindRecord,
                            afterNote = uiState.afterNote
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                PushSettingRow(
                    name = "마음의 기록",
                    isChecked = uiState.mindRecord,
                    enabled = !uiState.isLoading,
                    onCheckedChange = {
                        viewModel.updatePushSettings(
                            timeLetter = uiState.timeLetter,
                            mindRecord = it,
                            afterNote = uiState.afterNote
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                PushSettingRow(
                    name = "애프터노트",
                    isChecked = uiState.afterNote,
                    enabled = !uiState.isLoading,
                    onCheckedChange = {
                        viewModel.updatePushSettings(
                            timeLetter = uiState.timeLetter,
                            mindRecord = uiState.mindRecord,
                            afterNote = it
                        )
                    }
                )
                uiState.errorMessage?.let { message ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        fontFamily = Sansneo,
                        fontSize = 14.sp,
                        color = Gray9
                    )
                }
            }
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun PushSettingRow(
    modifier: Modifier = Modifier,
    name: String,
    isChecked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = enabled
            ) { onCheckedChange(!isChecked) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Gray9
        )
        ThumbSwitch(
            checked = isChecked,
            onCheckedChange = { newValue -> onCheckedChange(newValue) },
            enabled = enabled
        )
    }
}

private class FakePushSettingsViewModel : PushSettingsViewModelContract {
    private val _uiState = MutableStateFlow(
        PushSettingsUiState(
            timeLetter = true,
            mindRecord = true,
            afterNote = false
        )
    )
    override val uiState: StateFlow<PushSettingsUiState> = _uiState

    override fun loadPushSettings() {
        // No-op: Fake ViewModel for Preview only; no API call.
    }

    override fun updatePushSettings(
        timeLetter: Boolean?,
        mindRecord: Boolean?,
        afterNote: Boolean?
    ) {
        _uiState.value = _uiState.value.copy(
            timeLetter = timeLetter ?: _uiState.value.timeLetter,
            mindRecord = mindRecord ?: _uiState.value.mindRecord,
            afterNote = afterNote ?: _uiState.value.afterNote
        )
    }

    override fun clearUpdateSuccess() {
        // No-op: Fake ViewModel for Preview only.
    }

    override fun clearError() {
        // No-op: Fake ViewModel for Preview only.
    }
}

@Preview(showBackground = true)
@Composable
private fun PushToastSettingScreenPreview() {
    PushToastSettingScreen(
        viewModel = remember { FakePushSettingsViewModel() }
    )
}
