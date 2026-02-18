package com.kuit.afternote.feature.receiverauth.screen

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Alignment
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.button.SignUpContentButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.receiverauth.component.MasterKeyInputContent
import com.kuit.afternote.feature.receiverauth.component.PdfInputContent
import com.kuit.afternote.feature.receiverauth.component.ReceiveEndContent
import com.kuit.afternote.feature.receiverauth.presentation.viewmodel.VerifyErrorType
import com.kuit.afternote.feature.receiverauth.presentation.viewmodel.VerifySelfUiState
import com.kuit.afternote.feature.receiverauth.presentation.viewmodel.VerifySelfViewModel
import com.kuit.afternote.feature.receiverauth.presentation.viewmodel.VerifySelfViewModelContract
import com.kuit.afternote.feature.receiverauth.uimodel.VerifyStep
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Red
import kotlinx.coroutines.launch

private enum class PendingDocumentTarget {
    DEATH_CERT,
    FAMILY_CERT
}

@Composable
fun VerifySelfScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onCompleteClick: (receiverId: Long, authCode: String, senderName: String?) -> Unit = { _, _, _ -> },
    viewModel: VerifySelfViewModelContract = hiltViewModel<VerifySelfViewModel>()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val step = uiState.currentStep
    val deathCertificate = rememberTextFieldState()
    val familyRelationCertificate = rememberTextFieldState()
    val pendingDocumentTargetState = remember { mutableStateOf<PendingDocumentTarget?>(null) }
    val deathCertificateUriState = remember { mutableStateOf<Uri?>(null) }
    val familyCertificateUriState = remember { mutableStateOf<Uri?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val pendingMessage = stringResource(R.string.receiver_verify_wait_for_approval)

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val displayName = getDisplayName(context, selectedUri)
            when (pendingDocumentTargetState.value) {
                PendingDocumentTarget.DEATH_CERT -> {
                    deathCertificate.setTextAndPlaceCursorAtEnd(displayName)
                    deathCertificateUriState.value = selectedUri
                }
                PendingDocumentTarget.FAMILY_CERT -> {
                    familyRelationCertificate.setTextAndPlaceCursorAtEnd(displayName)
                    familyCertificateUriState.value = selectedUri
                }
                null -> { /* No-op when target was cleared. */ }
            }
        }
        pendingDocumentTargetState.value = null
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val displayName = getDisplayName(context, selectedUri)
            when (pendingDocumentTargetState.value) {
                PendingDocumentTarget.DEATH_CERT -> {
                    deathCertificate.setTextAndPlaceCursorAtEnd(displayName)
                    deathCertificateUriState.value = selectedUri
                }
                PendingDocumentTarget.FAMILY_CERT -> {
                    familyRelationCertificate.setTextAndPlaceCursorAtEnd(displayName)
                    familyCertificateUriState.value = selectedUri
                }
                null -> { /* No-op when target was cleared. */ }
            }
        }
        pendingDocumentTargetState.value = null
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TopBar(
                    title = stringResource(R.string.receiver_verify_title),
                    onBackClick = {
                        viewModel.goToPreviousStep() ?: onBackClick()
                    },
                    step = step
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(44.dp))

            when (step) {
                VerifyStep.MASTER_KEY_AUTH -> VerifySelfMasterKeyStep(
                    uiState = uiState,
                    viewModel = viewModel
                )
                VerifyStep.UPLOAD_PDF_AUTH -> VerifySelfPdfStep(
                    deathCertificate = deathCertificate,
                    familyRelationCertificate = familyRelationCertificate,
                    deathCertificateUriState = deathCertificateUriState,
                    familyCertificateUriState = familyCertificateUriState,
                    pendingDocumentTargetState = pendingDocumentTargetState,
                    imagePickerLauncher = imagePickerLauncher,
                    filePickerLauncher = filePickerLauncher,
                    uiState = uiState,
                    viewModel = viewModel
                )
                VerifyStep.END -> VerifySelfEndStep(
                    uiState = uiState,
                    onCompleteClick = onCompleteClick,
                    onShowPendingMessage = {
                        scope.launch {
                            snackbarHostState.showSnackbar(pendingMessage)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun VerifySelfMasterKeyStep(
    uiState: VerifySelfUiState,
    viewModel: VerifySelfViewModelContract
) {
    SignUpContentButton(
        onNextClick = { viewModel.verifyMasterKey() }
    ) {
        MasterKeyInputContent(
            value = uiState.masterKeyInput,
            onValueChange = viewModel::updateMasterKey,
            isError = uiState.verifyError != null
        )
        if (uiState.verifyError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (val err = uiState.verifyError) {
                    is VerifyErrorType.Required ->
                        stringResource(R.string.receiver_verify_master_key_required)
                    is VerifyErrorType.Network ->
                        stringResource(R.string.receiver_verify_error_network)
                    is VerifyErrorType.Server -> err.message
                    VerifyErrorType.Unknown ->
                        stringResource(R.string.receiver_verify_error_unknown)
                    null -> ""
                },
                color = Red
            )
        }
        if (uiState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp),
                color = B3
            )
        }
    }
}

@Composable
private fun VerifySelfPdfStep(
    deathCertificate: TextFieldState,
    familyRelationCertificate: TextFieldState,
    deathCertificateUriState: MutableState<Uri?>,
    familyCertificateUriState: MutableState<Uri?>,
    pendingDocumentTargetState: MutableState<PendingDocumentTarget?>,
    imagePickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    filePickerLauncher: ActivityResultLauncher<String>,
    uiState: VerifySelfUiState,
    viewModel: VerifySelfViewModelContract
) {
    val deathUri = deathCertificateUriState.value
    val familyUri = familyCertificateUriState.value
    val bothSelected = deathUri != null && familyUri != null

    Column(modifier = Modifier.fillMaxSize()) {
        PdfInputContent(
            deadPdf = deathCertificate,
            familyPdf = familyRelationCertificate,
            onDeadImageAdd = {
                pendingDocumentTargetState.value = PendingDocumentTarget.DEATH_CERT
                imagePickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            onDeadFileAdd = {
                pendingDocumentTargetState.value = PendingDocumentTarget.DEATH_CERT
                filePickerLauncher.launch("*/*")
            },
            onFamilyImageAdd = {
                pendingDocumentTargetState.value = PendingDocumentTarget.FAMILY_CERT
                imagePickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            onFamilyFileAdd = {
                pendingDocumentTargetState.value = PendingDocumentTarget.FAMILY_CERT
                filePickerLauncher.launch("*/*")
            }
        )
        if (uiState.submitError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (val err = uiState.submitError) {
                    is VerifyErrorType.Required ->
                        stringResource(R.string.receiver_verify_documents_required)
                    is VerifyErrorType.Network ->
                        stringResource(R.string.receiver_verify_error_network)
                    is VerifyErrorType.Server -> err.message
                    VerifyErrorType.Unknown ->
                        stringResource(R.string.receiver_verify_error_unknown)
                    null -> ""
                },
                color = Red
            )
        }
        if (uiState.isSubmitting) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp),
                color = B3
            )
        }
        Spacer(modifier = Modifier.height(64.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            ClickButton(
                title = stringResource(R.string.receiver_verify_next_button),
                onButtonClick = {
                    viewModel.submitDocuments(
                        deathUri?.toString(),
                        familyUri?.toString()
                    )
                },
                isTrue = bothSelected
            )
        }
    }
}

@Composable
private fun VerifySelfEndStep(
    uiState: VerifySelfUiState,
    onCompleteClick: (receiverId: Long, authCode: String, senderName: String?) -> Unit,
    onShowPendingMessage: () -> Unit
) {
    SignUpContentButton(
        onNextClick = {
            val rid = uiState.verifiedReceiverId
            if (rid == null) return@SignUpContentButton
            val isApproved = uiState.deliveryVerificationStatus?.status
                ?.equals("APPROVED", ignoreCase = true) == true
            if (isApproved) {
                onCompleteClick(rid, uiState.masterKeyInput, uiState.verifiedSenderName)
            } else {
                onShowPendingMessage()
            }
        },
        buttonTitle = stringResource(R.string.receiver_verify_complete_confirm)
    ) {
        ReceiveEndContent(
            deliveryVerificationStatus = uiState.deliveryVerificationStatus
        )
    }
}

private fun getDisplayName(context: Context, uri: Uri): String {
    context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        ?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    return cursor.getString(nameIndex) ?: uri.lastPathSegment.orEmpty()
                }
            }
        }
    return uri.lastPathSegment?.substringAfterLast('/').orEmpty().ifEmpty { "파일" }
}

private class FakeVerifySelfViewModel : VerifySelfViewModelContract {
    private val _uiState = kotlinx.coroutines.flow.MutableStateFlow(VerifySelfUiState())
    override val uiState: kotlinx.coroutines.flow.StateFlow<VerifySelfUiState>
        get() = _uiState
    override fun updateMasterKey(text: String) {
        // Fake: no-op for Preview.
    }
    override fun verifyMasterKey() {
        // Fake: no-op for Preview.
    }
    override fun goToNextStep() {
        // Fake: no-op for Preview.
    }
    override fun goToPreviousStep(): VerifyStep? = null
    override fun clearVerifyError() {
        // Fake: no-op for Preview.
    }
    override fun submitDocuments(deathCertUri: String?, familyCertUri: String?) {
        // Fake: no-op for Preview.
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifySelfScreenPreview() {
    VerifySelfScreen(
        onBackClick = {},
        onNextClick = {},
        onCompleteClick = { _, _, _ -> },
        viewModel = remember { FakeVerifySelfViewModel() }
    )
}
