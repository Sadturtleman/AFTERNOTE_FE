package com.kuit.afternote.feature.afternote.presentation.author.nav.navigation

/**
 * 온보딩(로그인 등)으로 나가기 위한 목적지. Afternote feature는 onboarding 패키지에 의존하지 않습니다.
 * 앱 계층에서 [OnboardingRoute] 등으로 매핑합니다.
 */
sealed interface AfternoteOnboardingDestination {
    data object Login : AfternoteOnboardingDestination

    data class ProfileSetting(
        val email: String,
        val password: String,
    ) : AfternoteOnboardingDestination
}

/**
 * Afternote에서 인증·온보딩 플로우로 네비게이션할 때 사용합니다.
 */
fun interface AfternoteNavigator {
    fun goToOnboarding(destination: AfternoteOnboardingDestination)
}
