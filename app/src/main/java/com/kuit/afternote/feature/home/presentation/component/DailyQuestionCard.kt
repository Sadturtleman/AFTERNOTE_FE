package com.kuit.afternote.feature.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray8
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun DailyQuestionCard(
    modifier: Modifier = Modifier,
    question: String,
    dateLabel: String,
    onCtaClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // Gradient card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(
                    brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            0.008f to Color(0xFFFFE1CC),
                            0.36f to Color(0xFFF3F3F3),
                            0.56f to Color(0xFFBDE0FF),
                            0.97f to Color(0xFF0757C0)
                        ),
                        start = Offset(350f, 0f),
                        end = Offset(0f, 400f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Question text
        Text(
            text = question,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 30.sp,
            letterSpacing = (-0.25).sp,
            color = Gray8
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Date label
        Text(
            text = dateLabel,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            color = Gray5
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CTA button
        Surface(
            onClick = onCtaClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = B3,
            shadowElevation = 2.dp
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.home_daily_question_cta),
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    color = Gray9
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DailyQuestionCardPreview() {
    AfternoteTheme {
        DailyQuestionCard(
            question = "오늘 하루,\n누구에게 가장 고마웠나요?",
            dateLabel = "11월 15일, 오늘의 질문"
        )
    }
}
