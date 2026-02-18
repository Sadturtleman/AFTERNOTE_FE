package com.kuit.afternote.feature.receiver.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.ConfirmationPopup
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.feature.receiver.presentation.component.ContentSection
import com.kuit.afternote.feature.receiver.presentation.component.HeroCard
import com.kuit.afternote.feature.receiver.presentation.component.TopHeader
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverDownloadAllViewModel
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverDownloadAllViewModelContract
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverDownloadAllUiState
import com.kuit.afternote.ui.theme.Sansneo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
@Suppress("AssignedValueIsNeverRead")
fun ReceiverAfterNoteScreen(
    showBottomBar: Boolean = true,
    receiverId: String = "1",
    authCode: String = "",
    senderName: String = "",
    leaveMessage: String? = null,
    mindRecordTotalCount: Int = 0,
    timeLetterTotalCount: Int = 0,
    afternoteTotalCount: Int = 0,
    onNavigateToRecord: () -> Unit = {},
    onNavigateToTimeLetter: () -> Unit = {},
    onNavigateToAfternote: () -> Unit = {},
    viewModel: ReceiverDownloadAllViewModelContract = hiltViewModel<ReceiverDownloadAllViewModel>()
) {
    var selectedBottomNavItem by remember { mutableStateOf(BottomNavItem.AFTERNOTE) }
    var showDialog by remember { mutableStateOf(false) }
    val downloadAllUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(downloadAllUiState.downloadSuccess) {
        if (downloadAllUiState.downloadSuccess) {
            showDialog = false
            viewModel.clearDownloadSuccess()
        }
    }
    LaunchedEffect(downloadAllUiState.errorMessage) {
        downloadAllUiState.errorMessage?.let { message ->
            showDialog = false
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    if (showDialog) {
        ConfirmationPopup(
            message = stringResource(R.string.receiver_download_all_dialog_message),
            onDismiss = { showDialog = false },
            onConfirm = {
                if (authCode.isNotBlank()) viewModel.confirmDownloadAll(authCode)
            },
            isLoading = downloadAllUiState.isLoading
        )
    }

    Scaffold(
        topBar = {
             TopHeader()
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    selectedItem = selectedBottomNavItem,
                    onItemSelected = { selectedBottomNavItem = it }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            item {
                Text(
                    text = stringResource(
                        R.string.receiver_sender_record_title,
                        senderName.ifBlank { "" }
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Gray9,
                    fontFamily = Sansneo,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                HeroCard(
                    leaveMessage =
                        leaveMessage?.takeIf { it.isNotBlank() }
                            ?: "가족들에게...\n내가 없어도 너무 슬퍼하지마."
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                ContentSection(
                    title = "마음의 기록",
                    desc = "고인의 일상적인 생각과 감정, 일기들입니다.",
                    subDesc = stringResource(
                        R.string.receiver_mindrecord_section_count,
                        mindRecordTotalCount
                    ),
                    btnText = "마음의 기록 확인하러 가기",
                    imageResource = painterResource(R.drawable.img_book),
                    onButtonClick = onNavigateToRecord
                )
            }

            item {
                ContentSection(
                    title = "타임레터",
                    desc = "고인이 특별한 날에 작성한 편지입니다.",
                    subDesc = stringResource(
                        R.string.receiver_timeletter_section_count,
                        timeLetterTotalCount
                    ),
                    btnText = "라이프 이벤트 레터 확인하러 가기",
                    imageResource = painterResource(R.drawable.img_letter),
                    onButtonClick = onNavigateToTimeLetter
                )
            }

            item {
                ContentSection(
                    title = "애프터노트",
                    desc = "고인이 사후 정리하고자 하는 데이터입니다.",
                    subDesc = stringResource(
                        R.string.receiver_afternote_section_count,
                        afternoteTotalCount
                    ),
                    btnText = "애프터노트 확인하러 가기",
                    imageResource = painterResource(R.drawable.img_notebook),
                    onButtonClick = onNavigateToAfternote
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                ClickButton(
                    color = B3,
                    onButtonClick = { showDialog = true },
                    title = stringResource(R.string.receiver_download_all_button)
                )
            }
        }
    }
}

private class FakeReceiverDownloadAllViewModel : ReceiverDownloadAllViewModelContract {
    private val _uiState = MutableStateFlow(ReceiverDownloadAllUiState())
    override val uiState: StateFlow<ReceiverDownloadAllUiState> = _uiState
    override fun confirmDownloadAll(authCode: String) {}
    override fun clearDownloadSuccess() {}
    override fun clearError() {}
}

@Preview(showBackground = true)
@Composable
private fun PreviewReceiverAfterNote() {
    MaterialTheme {
        ReceiverAfterNoteScreen(
            viewModel = remember { FakeReceiverDownloadAllViewModel() }
        )
    }
}
