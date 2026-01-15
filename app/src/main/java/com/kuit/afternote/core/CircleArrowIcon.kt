package com.kuit.afternote.core

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * 원형 또는 둥근 모서리 배경에 화살표 아이콘을 표시하는 컴포넌트
 *
 * @param modifier Modifier (기본: Modifier)
 * @param iconSpec 아이콘 관련 설정 (필수)
 * @param backgroundColor 배경색 (기본: B1)
 * @param size 전체 크기 (기본: 12.dp)
 * @param shape 배경 모양 (기본: CircleShape)
 * @param padding 내부 패딩 (기본: 1.dp, CircleShape 사용 시)
 */
@Composable
fun CircleArrowIcon(
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
                )
                .then(
                    if (iconSpec.offset != DpOffset.Zero) {
                        Modifier.offset(x = iconSpec.offset.x, y = iconSpec.offset.y)
                    } else {
                        Modifier
                    }
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CircleArrowIconPreview() {
    AfternoteTheme {
        Column {
            // 작은 원형 (MemorialPlaylist 스타일)
            CircleArrowIcon(
                iconSpec = ArrowIconSpec(
                    iconRes = R.drawable.ic_arrow_right_playlist,
                    contentDescription = "추가"
                ),
                backgroundColor = B1,
                size = 12.dp
            )

            // 큰 둥근 모서리 (AfternoteListItem 스타일)
            CircleArrowIcon(
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
}
