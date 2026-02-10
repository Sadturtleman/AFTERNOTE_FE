package com.kuit.afternote.feature.receiver.presentation.screen

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.button.SignUpContentButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.receiver.presentation.component.MasterKeyInputContent
import com.kuit.afternote.feature.receiver.presentation.component.PdfInputContent
import com.kuit.afternote.feature.receiver.presentation.component.ReceiveEndContent
import com.kuit.afternote.feature.receiver.presentation.uimodel.VerifyStep

private enum class PendingDocumentTarget {
    DEATH_CERT,
    FAMILY_CERT
}

@Composable
fun VerifySelfScreen(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit
) {
    val context = LocalContext.current
    var step by remember { mutableStateOf(VerifyStep.MASTER_KEY_AUTH) }
    val masterKey = rememberTextFieldState()
    val deathCertificate = rememberTextFieldState()
    val familyRelationCertificate = rememberTextFieldState()
    var pendingDocumentTarget by remember { mutableStateOf<PendingDocumentTarget?>(null) }
    var deathCertificateUri by remember { mutableStateOf<Uri?>(null) }
    var familyCertificateUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val displayName = getDisplayName(context, selectedUri)
            when (pendingDocumentTarget) {
                PendingDocumentTarget.DEATH_CERT -> {
                    deathCertificate.setTextAndPlaceCursorAtEnd(displayName)
                    deathCertificateUri = selectedUri
                }
                PendingDocumentTarget.FAMILY_CERT -> {
                    familyRelationCertificate.setTextAndPlaceCursorAtEnd(displayName)
                    familyCertificateUri = selectedUri
                }
                null -> { }
            }
        }
        pendingDocumentTarget = null
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            val displayName = getDisplayName(context, selectedUri)
            when (pendingDocumentTarget) {
                PendingDocumentTarget.DEATH_CERT -> {
                    deathCertificate.setTextAndPlaceCursorAtEnd(displayName)
                    deathCertificateUri = selectedUri
                }
                PendingDocumentTarget.FAMILY_CERT -> {
                    familyRelationCertificate.setTextAndPlaceCursorAtEnd(displayName)
                    familyCertificateUri = selectedUri
                }
                null -> { }
            }
        }
        pendingDocumentTarget = null
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "수신자 인증",
                onBackClick = {
                    step.previous()?.let {
                        step = it
                    } ?: onBackClick()
                },
                step = step
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(44.dp))

            when (step) {
                VerifyStep.MASTER_KEY_AUTH -> {
                    SignUpContentButton(
                        onNextClick = { step = VerifyStep.UPLOAD_PDF_AUTH }
                    ) {
                        MasterKeyInputContent(
                            masterKey = masterKey
                        )
                    }
                }

                VerifyStep.UPLOAD_PDF_AUTH -> {
                    SignUpContentButton(
                        onNextClick = { step = VerifyStep.END }
                    ) {
                        PdfInputContent(
                            deadPdf = deathCertificate,
                            familyPdf = familyRelationCertificate,
                            onDeadImageAdd = {
                                pendingDocumentTarget = PendingDocumentTarget.DEATH_CERT
                                imagePickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            onDeadFileAdd = {
                                pendingDocumentTarget = PendingDocumentTarget.DEATH_CERT
                                filePickerLauncher.launch("*/*")
                            },
                            onFamilyImageAdd = {
                                pendingDocumentTarget = PendingDocumentTarget.FAMILY_CERT
                                imagePickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            onFamilyFileAdd = {
                                pendingDocumentTarget = PendingDocumentTarget.FAMILY_CERT
                                filePickerLauncher.launch("*/*")
                            }
                        )
                    }
                }

                VerifyStep.END -> {
                    SignUpContentButton(
                        onNextClick = { onNextClick() }
                    ) {
                        ReceiveEndContent()
                    }
                }
            }
        }
    }
}

private fun getDisplayName(context: android.content.Context, uri: Uri): String {
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

@Preview
@Composable
private fun VerifySelfScreenPreview() {
    VerifySelfScreen(
        onBackClick = {}
    ) { }
}
