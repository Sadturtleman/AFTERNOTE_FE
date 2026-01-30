package com.kuit.afternote.ui.theme

import androidx.compose.ui.unit.dp

/**
 * 디자인 시스템 Spacing 토큰
 *
 * 매직 넘버를 피하고 일관된 간격을 유지하기 위한 spacing 값들입니다.
 * 스크롤 컨테이너 내부에서 고정값 Spacer를 사용할 때 이 토큰을 사용해야 합니다.
 */
object Spacing {
    /** Extra Small: 4dp */
    val xs = 4.dp

    /** Small: 8dp */
    val s = 8.dp

    /** Medium: 16dp */
    val m = 16.dp

    /** Medium-Large: 20dp */
    val ml = 20.dp

    /** Large: 24dp */
    val l = 24.dp

    /** Extra Large: 32dp */
    val xl = 32.dp

    // 주의: 큰 하단 여백(socialNetworkBottom, galleryAndFileBottom, memorialGuidelineBottom)은
    // 스크롤 컨테이너에서 반응형 간격을 위해 LocalConfiguration 기반으로 변경되었습니다.
    // 각 Content 컴포넌트에서 screenHeight * percentage 형태로 직접 계산합니다.
}
