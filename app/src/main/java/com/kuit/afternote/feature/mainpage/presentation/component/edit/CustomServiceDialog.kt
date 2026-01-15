package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kuit.afternote.core.LabeledTextField
import com.kuit.afternote.core.ui.component.ClickButton
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 직접 입력하기 다이얼로그 콜백
 */
@Immutable
data class CustomServiceDialogCallbacks(
    val onDismiss: () -> Unit = {},
    val onAddClick: () -> Unit = {}
)

/**
 * 직접 입력하기 다이얼로그 파라미터
 */
@Immutable
data class CustomServiceDialogParams(
    val serviceNameState: TextFieldState,
    val callbacks: CustomServiceDialogCallbacks = CustomServiceDialogCallbacks()
)

/**
 * 직접 입력하기 다이얼로그 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 타이틀: "직접 입력하기" (18sp, Bold, Gray9, 중앙 정렬)
 * - 추가 서비스명 입력 필드 (Gray1 배경, 8dp radius)
 * - 추가하기 버튼 (B3 배경, 전체 너비)
 * - 다이얼로그: 흰색 배경, 16dp radius, drop shadow
 */
@Composable
fun CustomServiceDialog(
    modifier: Modifier = Modifier,
    params: CustomServiceDialogParams
) {
    Dialog(
        onDismissRequest = params.callbacks.onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .dropShadow(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.15f),
                    blur = 10.dp,
                    offsetX = 0.dp,
                    offsetY = 2.dp,
                    spread = 0.dp
                )
                .background(
                    color = White,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(
                    horizontal = 24.dp,
                    vertical = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 타이틀
            Text(
                text = "직접 입력하기",
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Bold,
                    color = Gray9
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 추가 서비스명 입력 필드
            LabeledTextField(
                label = "추가 서비스명",
                textFieldState = params.serviceNameState,
                keyboardType = KeyboardType.Text,
                containerColor = Gray1,
                labelSpacing = 8.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 추가하기 버튼
            ClickButton(
                color = B3,
                title = "추가하기",
                onButtonClick = params.callbacks.onAddClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomServiceDialogPreview() {
    AfternoteTheme {
        CustomServiceDialog(
            params = CustomServiceDialogParams(
                serviceNameState = rememberTextFieldState()
            )
        )
    }
}
