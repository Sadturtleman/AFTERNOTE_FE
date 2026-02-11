package com.kuit.afternote.feature.setting.presentation.screen.notice

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Immutable
data class NoticeItemUiModel(
    val id: String,
    val title: String,
    val dateText: String
)

@Composable
fun NoticeScreen(
    modifier: Modifier = Modifier,
    items: List<NoticeItemUiModel>,
    onBackClick: () -> Unit,
    onItemClick: (NoticeItemUiModel) -> Unit
) {
    BackHandler(onBack = onBackClick)
    Scaffold(
        containerColor = Gray1,
        topBar = {
            TopBar(
                title = "공지사항",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(Modifier.height(16.dp))
            }
            itemsIndexed(
                items = items,
                key = { _, item -> item.id }
            ) { _, item ->
                NoticeItem(
                    item = item,
                    onClick = { onItemClick(item) }
                )
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = Gray3
                )
            }
        }
    }
}

@Composable
private fun NoticeItem(
    item: NoticeItemUiModel,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "작성일: ${item.dateText}",
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray6
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray9
                )
                Spacer(Modifier.height(20.dp))
            }
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right_tab),
                contentDescription = null,
                modifier = Modifier.size(width = 8.dp, height = 14.dp),
                tint = Gray9
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoticeScreenPreview() {
    AfternoteTheme {
        val sampleItems = List(10) { index ->
            NoticeItemUiModel(
                id = index.toString(),
                title = "애프터노트 서비스 출시 안내",
                dateText = "2025. 11. 20"
            )
        }
        NoticeScreen(
            items = sampleItems,
            onBackClick = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoticeScreenEmptyPreview() {
    AfternoteTheme {
        NoticeScreen(
            items = emptyList(),
            onBackClick = {},
            onItemClick = {}
        )
    }
}
