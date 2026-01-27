package com.kuit.afternote.feature.setting.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun KeychainStatusCard(
    title: String = "icloud keychain",
    date: LocalDateTime = LocalDateTime.now(),
    onClose: () -> Unit = {}
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm")

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.6.dp, Gray4),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Apple Logo Circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE0E0E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // 실제 프로젝트에서는 Icon(painterResource(R.drawable.ic_apple)...) 사용
                Text("🍎", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = Sansneo
                )
                Text(
                    text = "생성일시 ${formatter.format(date)}.",
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    fontFamily = Sansneo
                )
            }

            // Close Button
            IconButton(onClick = onClose, modifier = Modifier.size(14.dp)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Gray5,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}
