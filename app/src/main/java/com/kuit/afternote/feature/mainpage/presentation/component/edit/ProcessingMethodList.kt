package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.feature.mainpage.presentation.model.ProcessingMethodItem
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.White

/**
 * 처리 방법 리스트 컴포넌트
 */
@Composable
fun ProcessingMethodList(
    modifier: Modifier = Modifier,
    items: List<ProcessingMethodItem>,
    onAddClick: () -> Unit = {},
    onItemMoreClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = White,
                shape = RoundedCornerShape(16.dp)
            ).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEach { item ->
            ProcessingMethodCheckbox(
                item = item,
                onMoreClick = { onItemMoreClick(item.id) }
            )
            Spacer(
                modifier = Modifier.height(6.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // 추가 버튼
        Image(
            painter = painterResource(R.drawable.ic_add_circle),
            contentDescription = "추가",
            modifier = Modifier
                .clickable(onClick = onAddClick)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProcessingMethodListPreview() {
    AfternoteTheme {
        ProcessingMethodList(
            items = listOf(
                ProcessingMethodItem("1", "게시물 내리기"),
                ProcessingMethodItem("2", "댓글 비활성화")
            ),
            onAddClick = {},
            onItemMoreClick = {}
        )
    }
}

