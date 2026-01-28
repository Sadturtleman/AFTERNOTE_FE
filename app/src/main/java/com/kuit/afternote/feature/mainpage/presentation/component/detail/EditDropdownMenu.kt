package com.kuit.afternote.feature.mainpage.presentation.component.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun EditDropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    offset: DpOffset = DpOffset(x = 0.dp, y = 0.dp)
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        offset = offset,
        shape = RoundedCornerShape(8.dp),
        containerColor = Color.White,
        shadowElevation = 4.dp,
        modifier = modifier.width(110.dp)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "수정하기",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            onClick = {
                onDismissRequest()
                onEditClick()
            },
            contentPadding = PaddingValues(all = 16.dp)
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = "삭제하기",
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = Sansneo,
                        fontWeight = FontWeight.Medium,
                        color = Gray9,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            onClick = {
                onDismissRequest()
                onDeleteClick()
            },
            contentPadding = PaddingValues(all = 16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 240, heightDp = 320)
@Composable
private fun EditDropdownMenuPreview() {
    AfternoteTheme {
        EditDropdownMenu(
            expanded = true,
            onDismissRequest = {}
        )
    }
}
