package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TimeLetterBlockItem(
    receiverName: String,
    sendDate: String,
    title: String,
    content: String,
    imageResId: Int?,  // 배경 이미지 (null이면 기본 배경)
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}) {

    Box(
    modifier = Modifier
        .width(350.dp)
        .height(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .clickable(onClick = onClick)
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            Row(){
                Text(
                    text= "수신인: "
                )
                Text(
                    text="발송 예정일: "
                )
            }


        }
    }

}

@Preview
@Composable
private fun LetterBlockPreview() {
    TimeLetterBlockItem(
        receiverName= "박경민",
        sendDate=  "2030년 8월 30일",
        title= "안녕",
        content="하하하ㅏ하어ㅏㄹ어ㅏ어ㅏㅇ란리너ㅏ어라ㅣ어라너ㅣㅏ어ㅣㅏㅇㄴ리ㅏ이ㅏ리ㅏㅇ너리ㅏ이ㅏㄹ나ㅣㅇㄹ니ㅏㅇ러ㅣㅏ우리ㅏㄴ우리ㅏ위ㅏㄹ뉘ㅏ누이ㅏㅓ리나리ㅏㄴ우러ㅏㄴ울누",
        imageResId=  null,  // 배경 이미지 (null이면 기본 배경)
    )
}
