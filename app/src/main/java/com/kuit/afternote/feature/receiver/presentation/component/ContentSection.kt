package com.kuit.afternote.feature.receiver.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.icon.RightArrowIcon
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun ContentSection(
    title: String,
    desc: String,
    subDesc: String,
    btnText: String,
    imageResource: Painter
) {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontFamily = Sansneo,
            fontWeight = FontWeight.Bold,
            color = Gray9,
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Flat style as per image
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Background decoration (Simulating the 3D icons in the image)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                ) {
                    Image(
                        painter = imageResource,
                        contentDescription = null,
                        modifier = Modifier
                            .size(170.dp)
                    )
                }

                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = desc,
                        fontSize = 16.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9
                    )
                    Text(
                        text = subDesc,
                        fontSize = 12.sp,
                        fontFamily = Sansneo,
                        color = Gray9,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = B3),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = btnText,
                            fontSize = 12.sp,
                            color = Gray9,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Sansneo
                        )

                        Spacer(modifier = Modifier.width(4.dp))
                        RightArrowIcon(B1)
                    }
                }
            }
        }
    }
}
