package com.kuit.afternote.feature.setting.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Black
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.Spacing
import com.kuit.afternote.ui.theme.White

private const val RECEIVER_DETAIL_FIELD_PLACEHOLDER = "Text Field"

/**
 * 프로필 수정 화면 콜백 그룹
 */
data class ReceiverDetailEditCallbacks(
    val onBackClick: () -> Unit = {},
    val onEditClick: () -> Unit = {},
    val onReceiverDetailImageClick: () -> Unit = {},
    val onDailyQuestionClick: () -> Unit = {},
    val onTimeLetterClick: () -> Unit = {},
    val onAfternoteClick: () -> Unit = {}
)

@Immutable
data class ReceiverDetailScreenParams(
    val name: String,
    val relationship: String,
    val phoneNumberState: TextFieldState,
    val emailState: TextFieldState,
    val dailyQuestionCount: Int,
    val timeLetterCount: Int,
    val afternoteCount: Int
)

@Composable
fun ReceiverDetailScreen(
    modifier: Modifier = Modifier,
    params: ReceiverDetailScreenParams = ReceiverDetailScreenParams(
        name = "김지은",
        relationship = "딸",
        phoneNumberState = rememberTextFieldState(),
        emailState = rememberTextFieldState(),
        dailyQuestionCount = 8,
        timeLetterCount = 12,
        afternoteCount = 4
    ),
    callbacks: ReceiverDetailEditCallbacks = ReceiverDetailEditCallbacks()
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Gray1,
        topBar = {
            TopBar(
                actionText = "수정",
                onBackClick = callbacks.onBackClick,
                onActionClick = callbacks.onEditClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            ReceiverDetailHeader(
                name = params.name,
                relationship = params.relationship,
                onReceiverDetailImageClick = callbacks.onReceiverDetailImageClick
            )

            Spacer(modifier = Modifier.height(Spacing.m))

            ReceiverDetailValueField(
                label = "전화번호",
                textFieldState = params.phoneNumberState
            )

            Spacer(modifier = Modifier.height(Spacing.l))

            ReceiverDetailValueField(
                label = "이메일",
                textFieldState = params.emailState
            )

            Spacer(modifier = Modifier.height(Spacing.l))
            Text(
                text = "전달할 내용",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
            )

            Spacer(modifier = Modifier.height(Spacing.m))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(Spacing.s)
            ) {
                DeliveryCountRow(
                    title = "데일리 질문 답변",
                    countText = "${params.dailyQuestionCount}건",
                    onClick = callbacks.onDailyQuestionClick
                )

                DeliveryCountRow(
                    title = "타임레터",
                    countText = "${params.timeLetterCount}건",
                    onClick = callbacks.onTimeLetterClick
                )

                DeliveryCountRow(
                    title = "애프터노트",
                    countText = "${params.afternoteCount}건",
                    onClick = callbacks.onAfternoteClick
                )
            }

            Spacer(modifier = Modifier.height(Spacing.xl))
        }
    }
}

@Composable
private fun ReceiverDetailHeader(
    modifier: Modifier = Modifier,
    name: String,
    relationship: String,
    onReceiverDetailImageClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_profile),
            contentDescription = "프로필 이미지",
            modifier = Modifier
                .size(134.dp)
                .clickable(onClick = onReceiverDetailImageClick)
        )

        Spacer(modifier = Modifier.height(Spacing.ml))

        Text(
            text = name,
            style = TextStyle(
                fontSize = 32.sp,
                lineHeight = 32.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Bold,
                color = Black
            )
        )

        Spacer(modifier = Modifier.height(Spacing.s))

        Text(
            text = relationship,
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 18.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Bold,
                color = Gray4
            )
        )
    }
}

@Composable
private fun ReceiverDetailValueField(
    modifier: Modifier = Modifier,
    label: String,
    textFieldState: TextFieldState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9
            )
        )

        OutlinedTextField(
            state = textFieldState,
            readOnly = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                color = Gray9
            ),
            placeholder = {
                Text(
                    text = RECEIVER_DETAIL_FIELD_PLACEHOLDER,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                        color = Gray4
                    )
                )
            },
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                unfocusedContainerColor = White,
                focusedContainerColor = White,
                disabledBorderColor = Color.Transparent,
                disabledContainerColor = White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
    }
}

@Composable
private fun DeliveryCountRow(
    modifier: Modifier = Modifier,
    title: String,
    countText: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = countText,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = B1
                )
            )

            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color = B1, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_arrow_right_tab),
                    contentDescription = "이동",
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReceiverDetailScreenPreview() {
    AfternoteTheme {
        val phoneNumberState = rememberTextFieldState()
        val emailState = rememberTextFieldState()

        LaunchedEffect(Unit) {
            phoneNumberState.edit { replace(0, length, "010-1234-1234") }
            emailState.edit { replace(0, length, "jieun01@naver.com") }
        }

        ReceiverDetailScreen(
            params = ReceiverDetailScreenParams(
                name = "김지은",
                relationship = "딸",
                phoneNumberState = phoneNumberState,
                emailState = emailState,
                dailyQuestionCount = 8,
                timeLetterCount = 12,
                afternoteCount = 4
            )
        )
    }
}
