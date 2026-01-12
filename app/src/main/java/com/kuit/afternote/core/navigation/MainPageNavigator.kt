package com.kuit.afternote.core.navigation

import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute

/**
 * MainPage 도메인에서 다른 도메인으로 네비게이션하기 위한 인터페이스
 * MainPage 도메인은 다른 도메인의 Route를 직접 알지 않고, 이 인터페이스를 통해 네비게이션합니다.
 */
interface MainPageNavigator {
    fun goToOnboarding(route: OnboardingRoute)
}
