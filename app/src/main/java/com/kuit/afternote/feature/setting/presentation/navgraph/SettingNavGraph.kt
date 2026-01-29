package com.kuit.afternote.feature.setting.presentation.navgraph

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuit.afternote.feature.setting.presentation.dummy.ReceiverDummyData
import com.kuit.afternote.feature.setting.presentation.screen.PasswordChangeScreen
import com.kuit.afternote.feature.setting.presentation.screen.PostDeliveryConditionScreen
import com.kuit.afternote.feature.setting.presentation.screen.ReceiverDetailEditCallbacks
import com.kuit.afternote.feature.setting.presentation.screen.ReceiverDetailScreen
import com.kuit.afternote.feature.setting.presentation.screen.ReceiverDetailScreenParams
import com.kuit.afternote.feature.setting.presentation.screen.ReceiverManagementScreen
import com.kuit.afternote.feature.setting.presentation.screen.ReceiverRegisterScreen
import com.kuit.afternote.feature.setting.presentation.screen.SettingMainScreen

fun NavGraphBuilder.settingNavGraph(navController: NavController) {
    composable<SettingRoute.SettingMainRoute> {
        SettingMainScreen(
            onClick = { title ->
                when (title) {
                    "프로필 수정" -> navController.navigate(SettingRoute.ReceiverDetailRoute())
                    "비밀번호 변경" -> navController.navigate(SettingRoute.PasswordChangeRoute)
                    "수신자 목록" -> navController.navigate(SettingRoute.ReceiverListRoute)
                    "수신자 등록" -> navController.navigate(SettingRoute.ReceiverRegisterRoute)
                    "사후 전달 조건" -> navController.navigate(SettingRoute.PostDeliveryConditionRoute)
                    // 나머지는 다른 사람 담당
                }
            }
        )
    }

    composable<SettingRoute.ReceiverDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<SettingRoute.ReceiverDetailRoute>()
        val receiverDetail = ReceiverDummyData.detailOf(receiverId = route.receiverId)

        val phoneNumberState = rememberTextFieldState()
        val emailState = rememberTextFieldState()

        LaunchedEffect(receiverDetail.receiverId) {
            phoneNumberState.edit {
                replace(
                    start = 0,
                    end = length,
                    text = receiverDetail.phoneNumber
                )
            }
            emailState.edit {
                replace(
                    start = 0,
                    end = length,
                    text = receiverDetail.email
                )
            }
        }

        ReceiverDetailScreen(
            params = ReceiverDetailScreenParams(
                name = receiverDetail.name,
                relationship = receiverDetail.relationship,
                phoneNumberState = phoneNumberState,
                emailState = emailState,
                dailyQuestionCount = receiverDetail.dailyQuestionCount,
                timeLetterCount = receiverDetail.timeLetterCount,
                afternoteCount = receiverDetail.afternoteCount
            ),
            callbacks = ReceiverDetailEditCallbacks(
                onBackClick = { navController.popBackStack() },
                onEditClick = { /* TODO: 프로필 수정 저장 */ },
                onReceiverDetailImageClick = { /* TODO: 프로필 이미지 변경 */ },
                onDailyQuestionClick = { /* TODO: 데일리 질문 답변 목록으로 이동 */ },
                onTimeLetterClick = { /* TODO: 타임레터 목록으로 이동 */ },
                onAfternoteClick = { /* TODO: 애프터노트 목록으로 이동 */ }
            )
        )
    }

    composable<SettingRoute.PasswordChangeRoute> {
        PasswordChangeScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<SettingRoute.ReceiverListRoute> {
        ReceiverManagementScreen(
            onBackClick = { navController.popBackStack() },
            onRegisterClick = { navController.navigate(SettingRoute.ReceiverRegisterRoute) },
            receivers = ReceiverDummyData.receiverList,
            onReceiverClick = { receiver ->
                navController.navigate(SettingRoute.ReceiverDetailRoute(receiverId = receiver.id))
            }
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
