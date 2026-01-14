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

    /** Large: 24dp */
    val l = 24.dp

    /** Extra Large: 32dp */
    val xl = 32.dp

    /** Social Network Bottom Spacing: 81dp (소셜네트워크 탭 하단 여백) */
    val socialNetworkBottom = 81.dp

    /** Gallery and File Bottom Spacing: 459dp (갤러리 및 파일 탭 하단 여백) */
    val galleryAndFileBottom = 459.dp

    /** Memorial Guideline Bottom Spacing: 272dp (추모 가이드라인 탭 하단 여백) */
    val memorialGuidelineBottom = 272.dp
}
