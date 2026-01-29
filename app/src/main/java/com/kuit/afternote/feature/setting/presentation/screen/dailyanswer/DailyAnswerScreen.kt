package com.kuit.afternote.feature.setting.presentation.screen.dailyanswer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray2
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray8
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Immutable
data class DailyAnswerItemUiModel(
    val question: String,
    val answer: String,
    val dateText: String
)

@Composable
fun DailyAnswerScreen(
    modifier: Modifier = Modifier,
    receiverName: String,
    items: List<DailyAnswerItemUiModel>,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = Gray1,
        topBar = {
            TopBar(
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
//            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item(key = "intro") {
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Spacer(Modifier.width(16.dp))
                    DailyAnswerIntro(
                        receiverName = receiverName
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            itemsIndexed(
                items = items,
                key = { index, _ -> "daily_answer_$index" }
            ) { index, item ->
                DailyAnswerCard(
                    item = item
                )

                if (index != items.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun DailyAnswerIntro(
    receiverName: String
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = B1)) {
                append(receiverName)
            }
            append(" ")
            append(stringResource(R.string.daily_answer_intro_line1_suffix))
            append("\n")
            append(stringResource(R.string.daily_answer_intro_line2))
        },
        style = TextStyle(
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Bold,
            color = Gray9
        )
    )
}

@Composable
private fun DailyAnswerCard(
    modifier: Modifier = Modifier,
    item: DailyAnswerItemUiModel
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row {
            Spacer(Modifier.width(4.dp))
            Text(
                text = item.question,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Gray9
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Gray2,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Text(
                text = item.answer,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray8
                )
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = item.dateText,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray5
            )
        )
    }
    HorizontalDivider(
        thickness = 1.dp,
        color = Gray3
    )
}

@Preview(showBackground = true)
@Composable
private fun DailyAnswerScreenPreview() {
    AfternoteTheme {
        val sampleQuestion = stringResource(R.string.daily_answer_sample_question)
        val sampleAnswer = stringResource(R.string.daily_answer_sample_answer)
        val sampleDateText = stringResource(R.string.daily_answer_sample_date)

        DailyAnswerScreen(
            receiverName = "김지은",
            items = listOf(
                DailyAnswerItemUiModel(
                    question = sampleQuestion,
                    answer = sampleAnswer,
                    dateText = sampleDateText
                ),
                DailyAnswerItemUiModel(
                    question = sampleQuestion,
                    answer = sampleAnswer,
                    dateText = sampleDateText
                ),
                DailyAnswerItemUiModel(
                    question = sampleQuestion,
                    answer = sampleAnswer,
                    dateText = sampleDateText
                ),
                DailyAnswerItemUiModel(
                    question = sampleQuestion,
                    answer = sampleAnswer,
                    dateText = sampleDateText
                )
            ),
            onBackClick = {}
        )
    }
}

