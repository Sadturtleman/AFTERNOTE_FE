package com.kuit.afternote.feature.receiver.presentation.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.receiver.presentation.uimodel.MindRecordItemUiModel
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun TodayRecordCard(todayRecord: MindRecordItemUiModel? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )

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
