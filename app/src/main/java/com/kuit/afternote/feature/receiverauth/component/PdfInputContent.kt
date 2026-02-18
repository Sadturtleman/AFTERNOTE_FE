package com.kuit.afternote.feature.receiverauth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun PdfInputContent(
    deadPdf: TextFieldState,
    familyPdf: TextFieldState,
    onDeadImageAdd: () -> Unit,
    onDeadFileAdd: () -> Unit,
    onFamilyImageAdd: () -> Unit,
    onFamilyFileAdd: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.receiver_verify_document_upload_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Sansneo
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.receiver_verify_document_upload_description),
            fontSize = 16.sp,
            fontFamily = Sansneo,
            color = Gray6,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        SinglePdfInputRow(
            title = stringResource(R.string.receiver_verify_death_cert_title),
            textFieldState = deadPdf,
            label = stringResource(R.string.receiver_verify_document_input_label),
            onImageAdd = onDeadImageAdd,
            onFileAdd = onDeadFileAdd
        )

        Spacer(modifier = Modifier.height(16.dp))

        SinglePdfInputRow(
            title = stringResource(R.string.receiver_verify_family_cert_title),
            textFieldState = familyPdf,
            label = stringResource(R.string.receiver_verify_document_input_label),
            onImageAdd = onFamilyImageAdd,
            onFileAdd = onFamilyFileAdd
        )
    }
}

@Composable
private fun SinglePdfInputRow(
    title: String,
    textFieldState: TextFieldState,
    label: String,
    onImageAdd: () -> Unit,
    onFileAdd: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = Gray9,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlineTextField(
                textFieldState = textFieldState,
                label = label,
                onFileAddClick = { expanded = true }
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 25.dp)
            ) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(x = 0.dp, y = 60.dp),
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.receiver_verify_add_image)) },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.PhotoCamera,
                                contentDescription = null,
                                tint = B2
                            )
                        },
                        onClick = {
                            onImageAdd()
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.receiver_verify_add_file)) },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Photo,
                                contentDescription = null,
                                tint = B2
                            )
                        },
                        onClick = {
                            onFileAdd()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PdfInputContentPreview() {
    val deadState = rememberTextFieldState()
    val familyState = rememberTextFieldState()

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            PdfInputContent(
                deadPdf = deadState,
                familyPdf = familyState,
                onDeadImageAdd = {},
                onDeadFileAdd = {},
                onFamilyImageAdd = {},
                onFamilyFileAdd = {}
            )
        }
    }
}
