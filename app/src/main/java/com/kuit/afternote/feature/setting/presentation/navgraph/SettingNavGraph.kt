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
import com.kuit.afternote.feature.setting.presentation.screen.dailyanswer.DailyAnswerItemUiModel
import com.kuit.afternote.feature.setting.presentation.screen.dailyanswer.DailyAnswerScreen
import com.kuit.afternote.feature.setting.presentation.screen.main.SettingMainScreen
import com.kuit.afternote.feature.setting.presentation.screen.password.PasswordChangeScreen
import com.kuit.afternote.feature.setting.presentation.screen.postdelivery.PostDeliveryConditionScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverAfternoteListScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverDetailEditCallbacks
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverDetailScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverDetailScreenParams
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverManagementScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverRegisterScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverTimeLetterListScreen
import com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem

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
            onBackClick = { navController.popBackStack() }
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

        DailyAnswerScreen(
            receiverName = receiverDetail.name,
            items = List(receiverDetail.dailyQuestionCount) {
                DailyAnswerItemUiModel(
                    question = sampleQuestion,
                    answer = sampleAnswer,
                    dateText = sampleDateText
                )
            },
            onBackClick = { navController.popBackStack() }
        )
    }

    composable<SettingRoute.ReceiverAfternoteListRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<SettingRoute.ReceiverAfternoteListRoute>()
        val receiverDetail = ReceiverDummyData.detailOf(receiverId = route.receiverId)

        ReceiverAfternoteListScreen(
            receiverName = receiverDetail.name,
            items = listOf(
                AfternoteListDisplayItem(
                    id = "instagram",
                    serviceName = stringResource(R.string.receiver_afternote_item_instagram),
                    date = "2025.11.26",
                    iconResId = R.drawable.img_insta_pattern
                ),
                AfternoteListDisplayItem(
                    id = "gallery",
                    serviceName = stringResource(R.string.receiver_afternote_item_gallery),
                    date = "2025.11.26",
                    iconResId = R.drawable.ic_gallery
                ),
                AfternoteListDisplayItem(
                    id = "memorial_guideline",
                    serviceName = stringResource(R.string.receiver_afternote_item_memorial_guideline),
                    date = "2025.11.26",
                    iconResId = R.drawable.ic_memorial_guideline
                ),
                AfternoteListDisplayItem(
                    id = "naver_mail",
                    serviceName = stringResource(R.string.receiver_afternote_item_naver_mail),
                    date = "2025.11.26",
                    iconResId = R.drawable.img_naver_mail
                )
            ),
            onBackClick = { navController.popBackStack() },
            onItemClick = { /* TODO: 애프터노트 상세로 이동 */ }
        )
    }

    composable<SettingRoute.ReceiverTimeLetterListRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<SettingRoute.ReceiverTimeLetterListRoute>()
        val receiverDetail = ReceiverDummyData.detailOf(receiverId = route.receiverId)

        ReceiverTimeLetterListScreen(
            receiverName = receiverDetail.name,
            items = List(receiverDetail.timeLetterCount) { index ->
                TimeLetterItem(
                    id = "timeletter_$index",
                    receivername = "박채연",
                    sendDate = "2027. 11. 24",
                    title = "채연아 20번째 생일을 축하해",
                    content = "너가 태어난 게 엊그제같은데 벌써 스무살이라니..엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶...",
                    imageResId = if (index == 0) R.drawable.ic_test_block else null,
                    theme = when (index % 3) {
                        0 -> LetterTheme.BLUE
                        1 -> LetterTheme.YELLOW
                        else -> LetterTheme.PEACH
                    }
                )
            },
            onBackClick = { navController.popBackStack() },
            onItemClick = { /* TODO: 타임레터 상세로 이동 */ }
        )
    }
}
