package com.kuit.afternote.feature.setting.presentation.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kuit.afternote.feature.setting.presentation.screen.PasswordChangeScreen
import com.kuit.afternote.feature.setting.presentation.screen.PostDeliveryConditionScreen
import com.kuit.afternote.feature.setting.presentation.screen.ProfileEditCallbacks
import com.kuit.afternote.feature.setting.presentation.screen.ProfileEditScreen
import com.kuit.afternote.feature.setting.presentation.screen.ReceiverListScreen
import com.kuit.afternote.feature.setting.presentation.screen.ReceiverRegisterScreen
import com.kuit.afternote.feature.setting.presentation.screen.SettingMainScreen

fun NavGraphBuilder.settingNavGraph(navController: NavController) {
    composable<SettingRoute.SettingMainRoute> {
        SettingMainScreen(
            onClick = { title ->
                when (title) {
                    "프로필 수정" -> navController.navigate(SettingRoute.ProfileEditRoute)
                    "비밀번호 변경" -> navController.navigate(SettingRoute.PasswordChangeRoute)
                    "수신자 목록" -> navController.navigate(SettingRoute.ReceiverListRoute)
                    "수신자 등록" -> navController.navigate(SettingRoute.ReceiverRegisterRoute)
                    "사후 전달 조건" -> navController.navigate(SettingRoute.PostDeliveryConditionRoute)
                    // 나머지는 다른 사람 담당
                }
            }
        )
    }

    composable<SettingRoute.ProfileEditRoute> {
        ProfileEditScreen(
            callbacks = ProfileEditCallbacks(
                onBackClick = { navController.popBackStack() },
                onRegisterClick = { /* TODO: 프로필 수정 저장 */ },
                onProfileImageClick = { /* TODO: 프로필 이미지 변경 */ },
                onEditProfileClick = { /* TODO: 프로필 정보 수정 */ },
                onChangeEmailClick = { /* TODO: 이메일 변경 */ },
                onWithdrawClick = { /* TODO: 회원 탈퇴 */ }
            )
        )
    }

    composable<SettingRoute.PasswordChangeRoute> {
        PasswordChangeScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<SettingRoute.ReceiverListRoute> {
        ReceiverListScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<SettingRoute.ReceiverRegisterRoute> {
        ReceiverRegisterScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<SettingRoute.PostDeliveryConditionRoute> {
        PostDeliveryConditionScreen(
            onBackClick = { navController.popBackStack() }
        )
    }
}
