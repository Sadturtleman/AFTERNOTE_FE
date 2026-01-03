package com.kuit.afternote.feature.mainpage.presentation.component.edit.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.feature.mainpage.presentation.component.edit.InformationProcessingRadioButton
import com.kuit.afternote.feature.mainpage.presentation.model.InformationProcessingMethod
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 갤러리 및 파일 선택 시 표시되는 콘텐츠
 */
@Composable
fun GalleryAndFileEditContent(
    modifier: Modifier = Modifier,
    selectedInformationProcessingMethod: InformationProcessingMethod,
    onInformationProcessingMethodSelected: (InformationProcessingMethod) -> Unit
) {
    // 정보 처리 방법 섹션
    Box {
        var textWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Text(
            text = "정보 처리 방법",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight(500),
                color = Gray9
            ),
            modifier = Modifier.onGloballyPositioned { coordinates ->
                textWidth = with(density) { coordinates.size.width.toDp() }
            }
        )
        // 파란 점: 오른쪽 위 꼭짓점으로부터 오른쪽 8.dp, 아래 4.dp
        Box(
            modifier = Modifier
                .offset(x = textWidth + 8.dp, y = 4.dp)
                .size(4.dp)
                .background(color = B2, shape = CircleShape)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    InformationProcessingRadioButton(
        method = InformationProcessingMethod.TRANSFER_TO_RECIPIENT,
        selected = selectedInformationProcessingMethod == InformationProcessingMethod.TRANSFER_TO_RECIPIENT,
        onClick = { onInformationProcessingMethodSelected(InformationProcessingMethod.TRANSFER_TO_RECIPIENT) }
    )

    Spacer(modifier = Modifier.height(8.dp))

    InformationProcessingRadioButton(
        method = InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT,
        selected = selectedInformationProcessingMethod == InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT,
        onClick = { onInformationProcessingMethodSelected(InformationProcessingMethod.TRANSFER_TO_ADDITIONAL_RECIPIENT) }
    )

    Spacer(modifier = Modifier.height(32.dp))
}

@Preview(showBackground = true)
@Composable
private fun GalleryAndFileEditContentPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 첫 번째 옵션 선택됨 (파란 테두리), 두 번째는 선택 안 됨 (테두리 없음) 상태를 한 화면에 표시
            GalleryAndFileEditContent(
                selectedInformationProcessingMethod = InformationProcessingMethod.TRANSFER_TO_RECIPIENT,
                onInformationProcessingMethodSelected = {}
            )
        }
    }
}
