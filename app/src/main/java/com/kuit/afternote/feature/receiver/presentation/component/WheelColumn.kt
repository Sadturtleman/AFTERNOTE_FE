package com.kuit.afternote.feature.receiver.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.Gray9

@Composable
fun WheelColumn(
    items: List<String>,
    initialIndex: Int,
    width: Dp
) {
    LazyColumn(
        modifier = Modifier
            .width(width)
            .height(150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 50.dp)
    ) {
        items(items) { item ->
            Text(
                text = item,
                fontSize = 20.sp,
                color = if (item == "2025" || item == "11") Gray9 else Color.LightGray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}
