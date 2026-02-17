package com.kuit.afternote.feature.receiver.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray8
import com.kuit.afternote.ui.theme.Sansneo

private const val HERO_CARD_DEFAULT_MESSAGE = "가족들에게...\n내가 없어도 너무 슬퍼하지마."

@Composable
fun HeroCard(
    leaveMessage: String = HERO_CARD_DEFAULT_MESSAGE
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFD0E4FF), // Light Blue
                        Color(0xFFF0F4F8), // White-ish
                        Color(0xFFFFE0CC) // Light Peach
                    )
                )
            )
    ) {
        Image(
            painter = painterResource(R.drawable.img_hero_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.CenterStart)
        ) {
            Text(
                text = leaveMessage,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Bold,
                color = Gray8,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "고인이 남긴 마지막 인사말",
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray5
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHeroCard() {
    HeroCard(leaveMessage = "가족들에게...\n내가 없어도 너무 슬퍼하지마.")
}
