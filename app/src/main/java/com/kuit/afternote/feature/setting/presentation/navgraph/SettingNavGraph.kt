package com.kuit.afternote.feature.setting.presentation.navgraph

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuit.afternote.R
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.feature.setting.presentation.dummy.ReceiverDummyData
import com.kuit.afternote.feature.setting.presentation.screen.account.ConnectedAccountsScreen
import com.kuit.afternote.feature.setting.presentation.screen.dailyanswer.DailyAnswerScreen
import com.kuit.afternote.feature.setting.presentation.screen.main.SettingMainScreen
import com.kuit.afternote.feature.setting.presentation.screen.notification.NotificationSettingsScreen
import com.kuit.afternote.feature.setting.presentation.screen.notification.PushToastSettingScreen
import com.kuit.afternote.feature.setting.presentation.screen.password.PasswordChangeScreen
import com.kuit.afternote.feature.setting.presentation.screen.password.PasswordConfirmScreen
import com.kuit.afternote.feature.setting.presentation.screen.password.PasswordModifyScreen
import com.kuit.afternote.feature.setting.presentation.screen.postdelivery.PostDeliveryConditionScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverAfternoteListScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverDetailEditCallbacks
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverDetailScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverDetailScreenParams
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverManagementScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverRegisterScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverTimeLetterListScreen
import com.kuit.afternote.feature.setting.presentation.screen.security.PassKeyAddScreen
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem

fun NavGraphBuilder.settingNavGraph(navController: NavController) {
    composable<SettingRoute.SettingMainRoute> {
        SettingMainScreen(
            onClick = { title ->
                when (title) {
                    "프로필 수정" -> navController.navigate(SettingRoute.ReceiverDetailRoute())
                    "비밀번호 변경" -> navController.navigate(SettingRoute.PasswordChangeRoute)
                    "연결된 계정" -> navController.navigate(SettingRoute.ConnectedAccountsRoute)
                    "알림 설정" -> navController.navigate(SettingRoute.NotificationSettingsRoute)
                    "수신자 목록" -> navController.navigate(SettingRoute.ReceiverListRoute)
                    "수신자 등록" -> navController.navigate(SettingRoute.ReceiverRegisterRoute)
                    "사후 전달 조건" -> navController.navigate(SettingRoute.PostDeliveryConditionRoute)
                    "패스키 관리" -> navController.navigate(SettingRoute.PassKeyAddRoute)
                    "앱 잠금 설정" -> navController.navigate(SettingRoute.AppLockPasswordModifyRoute)
                    // TODO: 나머지는 추후 연결 (FAQ, 1:1 문의, 공지사항, 약관 등)
                }
            }
        )
    }

    composable<SettingRoute.ConnectedAccountsRoute> {
        ConnectedAccountsScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<SettingRoute.NotificationSettingsRoute> {
        NotificationSettingsScreen(
            onBackClick = { navController.popBackStack() },
            onDeviceSettingsClick = { navController.navigate(SettingRoute.PushToastSettingRoute) }
        )
    }

    composable<SettingRoute.PushToastSettingRoute> {
        PushToastSettingScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<SettingRoute.PassKeyAddRoute> {
        PassKeyAddScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<SettingRoute.AppLockPasswordConfirmRoute> {
        PasswordConfirmScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<SettingRoute.AppLockPasswordModifyRoute> {
        PasswordModifyScreen(
            onBackClick = { navController.popBackStack() }
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
                onDailyQuestionClick = {
                    navController.navigate(
                        SettingRoute.DailyAnswerRoute(
                            receiverId = receiverDetail.receiverId
                        )
                    )
                },
                onTimeLetterClick = {
                    navController.navigate(
                        SettingRoute.ReceiverTimeLetterListRoute(
                            receiverId = receiverDetail.receiverId
                        )
                    )
                },
                onAfternoteClick = {
                    navController.navigate(
                        SettingRoute.ReceiverAfternoteListRoute(
                            receiverId = receiverDetail.receiverId
                        )
                    )
                }
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
            onBackClick = { navController.popBackStack() },
            onRegisterClick = { navController.popBackStack() }
        )
    }

    composable<SettingRoute.PostDeliveryConditionRoute> {
        PostDeliveryConditionScreen(
            onBackClick = { navController.popBackStack() },
            onRegisterClick = {
                // TODO: 사후 전달 조건 저장 후 이전 화면으로 이동
                navController.popBackStack()
            }
        )
    }

    composable<SettingRoute.DailyAnswerRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<SettingRoute.DailyAnswerRoute>()
        val receiverDetail = ReceiverDummyData.detailOf(receiverId = route.receiverId)
        val sampleQuestion = stringResource(R.string.daily_answer_sample_question)
        val sampleAnswer = stringResource(R.string.daily_answer_sample_answer)
        val sampleDateText = stringResource(R.string.daily_answer_sample_date)
        val dailyAnswerItems = ReceiverDummyData.dailyAnswerItems(
            receiverId = route.receiverId,
            question = sampleQuestion,
            answer = sampleAnswer,
            dateText = sampleDateText
        )

        DailyAnswerScreen(
            receiverName = receiverDetail.name,
            items = dailyAnswerItems,
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<SettingRoute.ReceiverAfternoteListRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<SettingRoute.ReceiverAfternoteListRoute>()
        val receiverDetail = ReceiverDummyData.detailOf(receiverId = route.receiverId)
        val afternoteItems = ReceiverDummyData.defaultAfternoteListSeedsForReceiverDetail().map { seed ->
            AfternoteListDisplayItem(
                id = seed.id,
                serviceName = seed.serviceNameResId?.let { stringResource(it) } ?: (seed.serviceNameLiteral ?: ""),
                date = seed.date,
                iconResId = seed.iconResId
            )
        }

        ReceiverAfternoteListScreen(
            receiverName = receiverDetail.name,
            items = afternoteItems,
            onBackClick = { navController.popBackStack() },
            onItemClick = { /* TODO: 애프터노트 상세로 이동 */ }
        )
    }

    composable<SettingRoute.ReceiverTimeLetterListRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<SettingRoute.ReceiverTimeLetterListRoute>()
        val receiverDetail = ReceiverDummyData.detailOf(receiverId = route.receiverId)
        val timeLetterItems: List<TimeLetterItem> =
            ReceiverDummyData.defaultTimeLetterItems(receiverId = route.receiverId)

        ReceiverTimeLetterListScreen(
            receiverName = receiverDetail.name,
            items = timeLetterItems,
            onBackClick = { navController.popBackStack() },
            onItemClick = { /* TODO: 타임레터 상세로 이동 */ }
        )
    }
}
