package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.CheckCircleIcon
import com.kuit.afternote.feature.mainpage.presentation.model.ProcessingMethodItem
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 처리 방법 체크박스 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 체크박스: 16dp
 * - 텍스트: 14sp, Regular, Gray9
 * - 더보기 아이콘: 오른쪽 정렬
 */
@Composable
fun ProcessingMethodCheckbox(
    modifier: Modifier = Modifier,
    item: ProcessingMethodItem,
    onClick: (() -> Unit)? = null,
    onMoreClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CheckCircleIcon()

        Text(
            text = item.text,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                color = Gray9
            ),
            modifier = Modifier
                .weight(1f)
        )

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
private fun ProcessingMethodCheckboxPreview() {
    AfternoteTheme {
        Column {
            ProcessingMethodCheckbox(
                item = ProcessingMethodItem("1", "게시물 내리기"),
            )
        }
    }
}
