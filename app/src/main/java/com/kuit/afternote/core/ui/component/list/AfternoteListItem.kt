package com.kuit.afternote.core.ui.component.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.ui.expand.dropShadow
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.ShadowBlack
import com.kuit.afternote.ui.theme.White

/**
 * Shared list row for 애프터노트 list (writer main and receiver list).
 * White card, icon, serviceName, "최종 작성일 {date}", arrow.
 */
@Composable
fun AfternoteListItem(
    modifier: Modifier = Modifier,
    item: AfternoteListDisplayItem,
    onClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(16.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(74.dp)
            .dropShadow(
                shape = shape,
                color = ShadowBlack,
                blur = 5.dp,
                offsetY = 2.dp,
                offsetX = 0.dp,
                spread = 0.dp
            ),
        shape = shape,
        color = White,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(item.iconResId),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = item.serviceName,
                        color = Color(0xFF000000),
                        lineHeight = 22.sp,
                        fontSize = 16.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "최종 작성일 ${item.date}",
                        color = Gray5,
                        lineHeight = 16.sp,
                        fontSize = 10.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Normal,
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(B2)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_arrow_forward_b2),
                        contentDescription = null,
                        modifier = Modifier
                            .size(6.dp, 12.dp)
                            .offset(x = 9.9.dp, y = 6.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AfternoteListItemPreview() {
    AfternoteTheme {
        AfternoteListItem(
            item = AfternoteListDisplayItem(
                id = "1",
                serviceName = "인스타그램",
                date = "2023.11.24",
                iconResId = R.drawable.img_insta_pattern
            )
        )
    }
}
