package com.kuit.afternote.feature.receiver.presentation.screen

import ReceiverRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.navigation.TopBar

@Composable
fun ReceiverListScreen(
    onBackClick: () -> Unit,
    onClick: (String) -> Unit
) {
    val receiverList = listOf<String>(
        "박서연",
        "김건국"
    )
    Scaffold(
        topBar = {
            TopBar(
                title = "받은 기록함",
                onBackClick = { onBackClick() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(20.dp)
        ) {
            items(receiverList) { name ->
                ReceiverRow(name) { onClick(name) }
            }
        }
    }
}

@Preview
@Composable
private fun ReceiverListScreenPreview() {
    ReceiverListScreen(
        onBackClick = {}
    ) { }
}
