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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.AfternoteTheme

private val PROFILE_EDIT_BUTTON_SIZE = 52.dp
private val PROFILE_EDIT_BUTTON_SHADOW_ELEVATION = 10.dp
private val PROFILE_EDIT_BUTTON_SHADOW_COLOR = Color(0x26000000)

/**
 * 프로필 이미지와 수정 버튼이 포함된 편집 가능한 프로필 컴포넌트
 *
 * @param modifier Modifier
 * @param fallbackImageRes 이미지가 없을 때 사용할 drawable 리소스 ID
 * @param containerSize 전체 컨테이너 크기 (기본값: 157.dp)
 * @param profileImageSize 프로필 이미지 크기 (기본값: 133.dp)
 * @param isEditable 수정 버튼 노출 여부
 * @param onEditClick 수정 버튼 클릭 시 콜백 (isEditable이 true일 때만 유효)
 * @param displayImageUri 표시할 이미지 URI 또는 URL (null이면 fallbackImageRes 사용)
 */
@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    @DrawableRes fallbackImageRes: Int = R.drawable.img_default_profile,
    containerSize: Dp = 157.dp,
    profileImageSize: Dp = 133.dp,
    isEditable: Boolean = true,
    onEditClick: (() -> Unit)? = null,
    displayImageUri: String? = null
) {
    Box(
        modifier = modifier.size(containerSize)
    ) {
        val painter =
            if (displayImageUri != null) {
                rememberAsyncImagePainter(displayImageUri)
            } else {
                painterResource(fallbackImageRes)
            }
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.content_description_profile_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(profileImageSize)
                .clip(CircleShape)
                .align(Alignment.Center)
        )

        if (isEditable) {
            Image(
                painter = painterResource(R.drawable.ic_add_circle_profile),
                contentDescription = stringResource(R.string.content_description_profile_edit_button),
                modifier = Modifier
                    .size(PROFILE_EDIT_BUTTON_SIZE)
                    .align(Alignment.BottomEnd)
                    .shadow(
                        elevation = PROFILE_EDIT_BUTTON_SHADOW_ELEVATION,
                        shape = CircleShape,
                        spotColor = PROFILE_EDIT_BUTTON_SHADOW_COLOR,
                        ambientColor = PROFILE_EDIT_BUTTON_SHADOW_COLOR
                    )
                    .clickable(onClick = { onEditClick?.invoke() })
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileImagePreviewEditable() {
    AfternoteTheme {
        ProfileImage()
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileImagePreviewNotEditable() {
    AfternoteTheme {
        ProfileImage(isEditable = false)
    }
}
