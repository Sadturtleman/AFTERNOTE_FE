package com.kuit.afternote.feature.receiver.presentation.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.kuit.afternote.R
import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordItemUiModel
import com.kuit.afternote.ui.theme.Sansneo

private const val TAG = "TodayRecordCard"

/** 이미지 없을 때 오늘의 기록 카드 배경용 디폴트 테마 (TimeLetterBlockItem과 동일한 비주얼). */
private object TodayRecordCardDefaultTheme {
    val backgroundColor: Color = Color(0xFFFFE1CC)
    val logoResId: Int = R.drawable.logo_yellow
}

@Composable
fun TodayRecordCard(
    todayRecord: MindRecordItemUiModel? = null,
    todayRecordImageUrl: String? = null
) {
    val hasImage = !todayRecordImageUrl.isNullOrBlank()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            if (hasImage) {
                val context = LocalContext.current
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(todayRecordImageUrl)
                        .httpHeaders(
                            NetworkHeaders.Builder()
                                .set("User-Agent", "Afternote Android App")
                                .build()
                        )
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = painterResource(R.drawable.img_time_letter_placeholder),
                    onError = { state: AsyncImagePainter.State.Error ->
                        Log.e(TAG, "Coil load failed: url=$todayRecordImageUrl", state.result.throwable)
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x660E0E0E)
                                )
                            )
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            TodayRecordCardDefaultTheme.backgroundColor.copy(alpha = 0.2f)
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.00f to Color(0x000E0E0E),
                                    0.09f to Color(0x170E0E0E),
                                    0.45f to Color(0x730E0E0E),
                                    1.00f to Color(0x800E0E0E)
                                )
                            )
                        )
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = TodayRecordCardDefaultTheme.logoResId),
                        contentDescription = stringResource(R.string.receiver_mindrecord_card_logo)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = todayRecord?.tags.orEmpty(),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = todayRecord?.date.orEmpty(),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                val displayText = when {
                    todayRecord == null -> stringResource(R.string.receiver_mindrecord_today_empty)
                    else -> todayRecord.question.ifBlank { todayRecord.content }
                        .ifBlank { stringResource(R.string.receiver_mindrecord_today_empty) }
                }
                Text(
                    text = displayText,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    fontFamily = Sansneo
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTodayRecordCardDefault() {
    TodayRecordCard(
        todayRecord = MindRecordItemUiModel(
            mindRecordId = 1L,
            date = "2025년 2월 19일",
            tags = "#DIARY",
            question = "오늘 하루는 어땠나요?",
            content = "기록 내용",
            hasImage = false
        ),
        todayRecordImageUrl = null
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewTodayRecordCardEmpty() {
    TodayRecordCard(todayRecord = null, todayRecordImageUrl = null)
}
