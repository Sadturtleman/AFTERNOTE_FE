package com.kuit.afternote.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 프로필 이미지와 수정 버튼이 포함된 편집 가능한 프로필 컴포넌트
 *
 * @param modifier Modifier
 * @param profileImageRes 프로필 이미지 리소스 ID
 * @param containerSize 전체 컨테이너 크기 (기본값: 157.dp)
 * @param profileImageSize 프로필 이미지 크기 (기본값: 133.dp)
 */
@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    @DrawableRes profileImageRes: Int = R.drawable.img_default_profile,
    containerSize: Dp = 157.dp,
    profileImageSize: Dp = 133.dp,
    isEditable: Boolean = true
) {
    Box(
        modifier = modifier.size(containerSize)
    ) {
        Image(
            painter = painterResource(profileImageRes),
            contentDescription = "프로필 이미지",
            modifier = Modifier
                .size(profileImageSize)
                .align(Alignment.Center)
        )

        if (isEditable) {
            Image(
                painter = painterResource(R.drawable.ic_add_circle_profile),
                contentDescription = "프로필 수정 버튼",
                modifier = Modifier
                    .size(52.dp)
                    .align(Alignment.BottomEnd)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                        spotColor = Color(0x26000000),
                        ambientColor = Color(0x26000000)
                    )
                    .clickable(onClick = {
                        //TODO
                    })
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileImagePreview() {
    AfternoteTheme {
        ProfileImage()
    }
}
