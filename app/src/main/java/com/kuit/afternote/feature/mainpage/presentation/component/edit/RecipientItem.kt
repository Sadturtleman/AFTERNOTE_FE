package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.mainpage.presentation.model.Recipient
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 수신자 아이템 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 아바타: 회색 원형 배경, 40dp
 * - 이름: 14sp, Regular, Gray9
 * - 라벨: 12sp, Regular, Gray5
 * - 더보기 아이콘: 오른쪽 정렬
 */
@Composable
fun RecipientItem(
    modifier: Modifier = Modifier,
    recipient: Recipient,
    onMoreClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 아바타 (기본 프로필 이미지)
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(R.drawable.img_recipient_profile),
                contentDescription = "프로필 사진",
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        // 이름과 라벨
        Column(
            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = recipient.name,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray9
                )
            )
            Text(
                text = recipient.label,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray5
                )
            )
        }

        // 더보기 아이콘
        Image(
            painter = painterResource(R.drawable.ic_more_horizontal_1),
            contentDescription = "더보기",
            modifier = Modifier
                .clickable(onClick = onMoreClick)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipientItemPreview() {
    AfternoteTheme {
        RecipientItem(
            recipient = Recipient(
                id = "1",
                name = "김지은",
                label = "친구"
            )
        )
    }
}
