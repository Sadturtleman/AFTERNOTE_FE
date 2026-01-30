package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R

@Composable
fun DraftLetterHeader(
    isEditMode: Boolean,
    onCloseClick: () -> Unit,
    onEditClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(43.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.ic_x),
            contentDescription = "닫기",
            modifier = Modifier
                .padding(start = 20.dp)
                .size(24.dp)
                .clickable { onCloseClick() }
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "임시 저장된 레터",
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.sansneobold)),
            fontWeight = FontWeight(700),
            color = Color(0xFF212121)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = if (isEditMode) "완료" else "편집",
            color = Color(0xFF9E9E9E),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            fontSize = 14.sp,
            fontWeight = FontWeight.W400,
            modifier = Modifier
                .padding(end = 20.dp)
                .clickable {
                    if (isEditMode) onCompleteClick() else onEditClick()
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DraftHeaderPrev() {
    DraftLetterHeader(
        isEditMode = true,
        onCloseClick = {},
        onEditClick = {},
        onCompleteClick = {}
    )
}
