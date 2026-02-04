package com.kuit.afternote.core.ui.component

import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.AfternoteTheme

private const val TAG = "ProfileImage"

/** Ratio: container size / profile image size (157 / 133). */
private const val CONTAINER_TO_PROFILE_RATIO = 157f / 133f
/** Ratio: edit button size / profile image size (52 / 133). */
private const val EDIT_BUTTON_TO_PROFILE_RATIO = 52f / 133f
/** Ratio: edit button shadow elevation / profile image size (10 / 133). */
private const val SHADOW_TO_PROFILE_RATIO = 10f / 133f

private val PROFILE_EDIT_BUTTON_SHADOW_COLOR = Color(0x26000000)

/**
 * 프로필 이미지와 수정 버튼이 포함된 편집 가능한 프로필 컴포넌트
 *
 * [isEditable]이 true일 때: 컨테이너(Box)와 수정 버튼을 사용하며, 컨테이너/버튼/그림자 크기는 [profileImageSize] 대비 비율로 자동 계산됩니다.
 * [isEditable]이 false일 때: 컨테이너를 사용하지 않고 프로필 이미지만 [profileImageSize]로 노출합니다.
 *
 * @param modifier Modifier
 * @param fallbackImageRes 이미지가 없을 때 사용할 drawable 리소스 ID
 * @param profileImageSize 프로필 이미지 크기 (기본값: 133.dp). 이 값만 변경 가능하며, 나머지 크기는 비율에 따라 자동 계산됨
 * @param isEditable 수정 버튼 노출 여부
 * @param onEditClick 수정 버튼 클릭 시 콜백 (isEditable이 true일 때만 유효)
 * @param displayImageUri 표시할 이미지 URI 또는 URL (null이면 fallbackImageRes 사용)
 */
@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    @DrawableRes fallbackImageRes: Int = R.drawable.img_default_profile,
    profileImageSize: Dp = 133.dp,
    isEditable: Boolean = true,
    onEditClick: (() -> Unit)? = null,
    displayImageUri: String? = null
) {
    Log.d(TAG, "displayImageUri=$displayImageUri fallbackImageRes=$fallbackImageRes")

    val imageModifier = Modifier.size(profileImageSize).clip(CircleShape)

    if (isEditable) {
        val containerSize = (profileImageSize.value * CONTAINER_TO_PROFILE_RATIO).dp
        val editButtonSize = (profileImageSize.value * EDIT_BUTTON_TO_PROFILE_RATIO).dp
        val shadowElevation = (profileImageSize.value * SHADOW_TO_PROFILE_RATIO).dp
        Box(
            modifier = modifier.size(containerSize),
            contentAlignment = Alignment.Center
        ) {
            ProfileImageContent(
                displayImageUri = displayImageUri,
                fallbackImageRes = fallbackImageRes,
                modifier = imageModifier
            )
            Image(
                painter = painterResource(R.drawable.ic_add_circle_profile),
                contentDescription = stringResource(R.string.content_description_profile_edit_button),
                modifier = Modifier
                    .size(editButtonSize)
                    .align(Alignment.BottomEnd)
                    .shadow(
                        elevation = shadowElevation,
                        shape = CircleShape,
                        spotColor = PROFILE_EDIT_BUTTON_SHADOW_COLOR,
                        ambientColor = PROFILE_EDIT_BUTTON_SHADOW_COLOR
                    )
                    .clickable(onClick = { onEditClick?.invoke() })
            )
        }
    } else {
        ProfileImageContent(
            displayImageUri = displayImageUri,
            fallbackImageRes = fallbackImageRes,
            modifier = modifier.then(imageModifier)
        )
    }
}

@Composable
private fun ProfileImageContent(
    modifier: Modifier,
    displayImageUri: String?,
    @DrawableRes fallbackImageRes: Int
) {
    if (!displayImageUri.isNullOrBlank()) {
        Log.d(TAG, "using AsyncImage for uri=$displayImageUri")
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(displayImageUri)
                .httpHeaders(
                    NetworkHeaders.Builder().apply {
                        this["User-Agent"] = "Afternote Android App"
                    }.build()
                )
                .build(),
            contentDescription = stringResource(R.string.content_description_profile_image),
            modifier = modifier,
            contentScale = ContentScale.Crop,
            error = painterResource(fallbackImageRes),
            onError = { state: AsyncImagePainter.State.Error ->
                Log.e(
                    TAG,
                    "Coil load failed: uri=$displayImageUri",
                    state.result.throwable
                )
            }
        )
    } else {
        Log.d(TAG, "using fallback drawable")
        Image(
            painter = painterResource(fallbackImageRes),
            contentDescription = stringResource(R.string.content_description_profile_image),
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
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
