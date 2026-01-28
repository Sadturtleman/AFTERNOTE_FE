package com.kuit.afternote.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B2

/**
 * 아이콘 관련 설정을 묶는 불변 데이터 클래스
 *
 * @param iconRes 화살표 아이콘 리소스 ID
 * @param contentDescription 접근성 설명
 * @param size 아이콘 크기 (기본: null, 아이콘 리소스의 원본 크기 사용)
 * @param offset 아이콘 오프셋 (기본: DpOffset.Zero, 중앙 정렬)
 */
@Immutable
data class ArrowIconSpec(
    @DrawableRes val iconRes: Int,
    val contentDescription: String? = null,
    val size: Dp? = null,
    val offset: DpOffset = DpOffset.Zero
)

/**
 * Material Icons를 사용하는 간단한 오른쪽 화살표 아이콘
 *
 * @param color 배경색
 * @param size 전체 크기 (기본: 12.dp)
 */
@Composable
fun RightArrowIcon(
    color: Color,
    size: Dp = 12.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color = color)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(size = size),
            tint = Color.White
        )
    }
}

/**
 * Drawable 리소스를 사용하는 원형 또는 둥근 모서리 배경에 화살표 아이콘을 표시하는 컴포넌트
 * (CircleArrowIcon 호환)
 *
 * @param modifier Modifier (기본: Modifier)
 * @param iconSpec 아이콘 관련 설정 (필수)
 * @param backgroundColor 배경색 (기본: B1)
 * @param size 전체 크기 (기본: 12.dp)
 * @param shape 배경 모양 (기본: CircleShape)
 * @param padding 내부 패딩 (기본: 1.dp, CircleShape 사용 시)
 */
@Composable
fun RightArrowIcon(
    modifier: Modifier = Modifier,
    iconSpec: ArrowIconSpec,
    backgroundColor: Color = B1,
    size: Dp = 12.dp,
    shape: Shape = CircleShape,
    padding: Dp = 1.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (shape == CircleShape) {
                    Modifier.padding(padding)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(iconSpec.iconRes),
            contentDescription = iconSpec.contentDescription,
            modifier = Modifier
                .then(
                    if (iconSpec.size != null) {
                        Modifier.size(iconSpec.size)
                    } else {
                        Modifier
                    }
                ).then(
                    if (iconSpec.offset != DpOffset.Zero) {
                        Modifier.offset(x = iconSpec.offset.x, y = iconSpec.offset.y)
                    } else {
                        Modifier
                    }
                )
        )
    }
}

@Preview(showBackground = true, name = "Material Icons")
@Composable
private fun RightArrowIconMaterialPreview() {
    AfternoteTheme {
        RightArrowIcon(
            color = B1,
            size = 16.dp
        )
    }
}

@Preview(showBackground = true, name = "Drawable - 작은 원형 (Tab)")
@Composable
private fun RightArrowIconDrawableTabPreview() {
    AfternoteTheme {
        RightArrowIcon(
            iconSpec = ArrowIconSpec(
                iconRes = R.drawable.ic_arrow_right_tab,
                contentDescription = "추가"
            ),
            backgroundColor = B1,
            size = 12.dp
        )
    }
}

@Preview(showBackground = true, name = "Drawable - 작은 원형 (Playlist)")
@Composable
private fun RightArrowIconDrawablePlaylistPreview() {
    AfternoteTheme {
        RightArrowIcon(
            iconSpec = ArrowIconSpec(
                iconRes = R.drawable.ic_arrow_right_playlist,
                contentDescription = "추가"
            ),
            backgroundColor = B1,
            size = 12.dp
        )
    }
}

@Preview(showBackground = true, name = "Drawable - 큰 둥근 모서리")
@Composable
private fun RightArrowIconDrawableRoundedPreview() {
    AfternoteTheme {
        RightArrowIcon(
            iconSpec = ArrowIconSpec(
                iconRes = R.drawable.ic_arrow_forward_b2,
                size = 6.dp,
                offset = DpOffset(x = 9.9.dp, y = 6.dp)
            ),
            backgroundColor = B2,
            size = 24.dp,
            shape = RoundedCornerShape(12.dp),
            padding = 0.dp
        )
    }
}
