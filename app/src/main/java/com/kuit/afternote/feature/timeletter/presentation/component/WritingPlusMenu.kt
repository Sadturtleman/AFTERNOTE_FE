package com.kuit.afternote.feature.timeletter.presentation.component

import android.R.attr.onClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R

@Composable
fun WritingPlusMenu(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .shadow(elevation = 10.dp, spotColor = Color(0x26000000), ambientColor = Color(0x26000000))
            .width(142.dp)
            .height(224.dp)
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 8.dp))
    )
    {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = modifier
                    .clickable(onClick = {})
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.ic_camera),
                    contentDescription = "카메라",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier= Modifier.width(8.dp))
                Text(
                    text = "이미지 추가",
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontFamily = FontFamily(Font(R.font.sansneomedium)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFF212121),
                    )
            }
            Row(
                modifier = modifier
                    .clickable(onClick = {})
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.ic_sound),
                    contentDescription = "음성 추가",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier= Modifier.width(8.dp))
                Text(
                    text = "음성 추가",
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = FontFamily(Font(R.font.sansneomedium)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF212121),
                )
            }
            Row(
                modifier = modifier
                    .clickable(onClick = {})
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.ic_file),
                    contentDescription = "파일",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier= Modifier.width(8.dp))
                Text(
                    text = "파일 추가",
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = FontFamily(Font(R.font.sansneomedium)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF212121),
                )
            }
            Row(
                modifier = modifier
                    .clickable(onClick = {})
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.ic_plus_link),
                    contentDescription = "링크",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier= Modifier.width(8.dp))
                Text(
                    text = "링크 추가",
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontFamily = FontFamily(Font(R.font.sansneomedium)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF212121),
                )
            }

        }
    }
}

@Preview
@Composable
private fun PlusMenuPrev() {
    WritingPlusMenu()
}
