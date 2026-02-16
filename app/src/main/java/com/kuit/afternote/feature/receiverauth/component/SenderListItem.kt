package com.kuit.afternote.feature.receiverauth.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R

@Composable
fun SenderListItem(
    senderName: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .height(74.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_default_profile_receiver),
            contentDescription = "sender_profile",
            modifier = Modifier
                .size(52.91.dp)
        )

        Spacer(modifier = Modifier.width(10.54.dp))

        Text(
            text = senderName,
            color = Color(0xFF000000),
            fontSize = 16.sp,
            fontFamily = FontFamily(
                Font(R.font.sansneomedium, FontWeight.Medium)
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painterResource(R.drawable.ic_arrow_forward_b2),
            contentDescription = "발신자 상세로",
            modifier = Modifier.width(6.dp).height(12.dp),
            colorFilter = ColorFilter.tint(Color(0xFF89C2FF))
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun SenderListItemPreview() {
    SenderListItem(
        senderName = "발신자 박경민"
    )
}
