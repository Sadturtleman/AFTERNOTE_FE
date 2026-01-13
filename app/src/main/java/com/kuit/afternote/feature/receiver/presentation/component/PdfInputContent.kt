package com.kuit.afternote.feature.receiver.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun PdfInputContent(
    deadPdf: TextFieldState,
    familyPdf: TextFieldState,
    onDeadFileAdd: () -> Unit,
    onFamilyFileAdd: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "증빙 서류 업로드",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Sansneo
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "사망진단서와 가족관계증명서를 업로드 해주세요.\n담당자의 확인 후 승인됩니다.",
            fontSize = 16.sp,
            fontFamily = Sansneo,
            color = Gray6,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 사망진단서 Row -> onDeadFileAdd 연결
        SinglePdfInputRow(
            title = "사망진단서 업로드",
            textFieldState = deadPdf,
            label = "서류 촬영 또는 파일 첨부",
            onActionClick = onDeadFileAdd
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 가족관계증명서 Row -> onFamilyFileAdd 연결
        SinglePdfInputRow(
            title = "가족관계증명서 업로드",
            textFieldState = familyPdf,
            label = "서류 촬영 또는 파일 첨부",
            onActionClick = onFamilyFileAdd
        )
    }
}

@Composable
private fun SinglePdfInputRow(
    title: String,
    textFieldState: TextFieldState,
    label: String,
    onActionClick: () -> Unit
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

        // 1. Box는 전체 너비를 채웁니다.
        Box(modifier = Modifier.fillMaxWidth()) {
            // 2. 텍스트 필드 그리기
            OutlineTextField(
                textFieldState = textFieldState,
                label = label,
                onFileAddClick = { expanded = true }
            )

            // 3. 드롭다운 메뉴 위치 잡기
            // 메뉴를 감싸는 투명한 Box를 만들어 우측 상단(버튼 위치 근처)으로 보냅니다.
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 25.dp)
            ) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    // 4. offset 조정: x는 0 (이미 우측 정렬함), y는 TextField 높이(약 60~70dp)만큼 내려줌
                    offset = DpOffset(x = 0.dp, y = 60.dp),
                    modifier = Modifier.background(Color.White) // 배경색 명시 (혹시 투명해서 안보일까봐)
                ) {
                    DropdownMenuItem(
                        text = { Text("이미지 추가") },
                        leadingIcon = { Icon(Icons.Outlined.PhotoCamera, contentDescription = null, tint = B2) },
                        onClick = {
                            onActionClick()
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("파일 추가") },
                        leadingIcon = { Icon(Icons.Outlined.Photo, contentDescription = null, tint = B2) },
                        onClick = {
                            onActionClick()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
