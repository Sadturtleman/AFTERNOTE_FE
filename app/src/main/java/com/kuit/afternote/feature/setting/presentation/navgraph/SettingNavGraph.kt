package com.kuit.afternote.feature.setting.presentation.navgraph

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuit.afternote.R
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.setting.presentation.screen.account.ConnectedAccountsScreen
import com.kuit.afternote.feature.setting.presentation.screen.dailyanswer.DailyAnswerItemUiModel
import com.kuit.afternote.feature.setting.presentation.screen.dailyanswer.DailyAnswerScreen
import com.kuit.afternote.feature.setting.presentation.screen.main.SettingMainScreen
import com.kuit.afternote.feature.setting.presentation.screen.notification.NotificationSettingsScreen
import com.kuit.afternote.feature.setting.presentation.screen.notification.PushToastSettingScreen
import com.kuit.afternote.feature.setting.presentation.screen.password.PasswordChangeScreen
import com.kuit.afternote.feature.setting.presentation.screen.password.PasswordConfirmScreen
import com.kuit.afternote.feature.setting.presentation.screen.password.PasswordModifyScreen
import com.kuit.afternote.feature.setting.presentation.screen.postdelivery.PostDeliveryConditionScreen
import com.kuit.afternote.feature.setting.presentation.screen.profile.ProfileEditCallbacks
import com.kuit.afternote.feature.setting.presentation.screen.profile.ProfileEditScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverAfternoteListScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverDetailEditCallbacks
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverDetailScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverDetailScreenParams
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverManagementScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverRegisterScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.ReceiverTimeLetterListScreen
import com.kuit.afternote.feature.setting.presentation.screen.notice.NoticeItemUiModel
import com.kuit.afternote.feature.setting.presentation.screen.notice.NoticeScreen
import com.kuit.afternote.feature.setting.presentation.screen.receiver.getAfternoteSourceDisplayResIds
import com.kuit.afternote.feature.setting.presentation.screen.security.PassKeyAddScreen
import com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverAfternotesListViewModel
import com.kuit.afternote.feature.receiver.presentation.viewmodel.ReceiverTimeLettersListViewModel
import com.kuit.afternote.feature.user.presentation.viewmodel.ReceiverDailyQuestionsViewModel
import com.kuit.afternote.feature.user.presentation.viewmodel.ReceiverDetailViewModel
import com.kuit.afternote.feature.user.presentation.viewmodel.ReceiverListViewModel
import com.kuit.afternote.feature.user.presentation.viewmodel.RegisterReceiverViewModel

fun NavGraphBuilder.settingNavGraph(navController: NavController) {
    composable<SettingRoute.SettingMainRoute> {
        SettingMainRouteContent(navController)
    }
    composable<SettingRoute.ConnectedAccountsRoute> {
        ConnectedAccountsScreen(onBackClick = { navController.popBackStack() })
    }
    composable<SettingRoute.NotificationSettingsRoute> {
        NotificationSettingsScreen(
            onBackClick = { navController.popBackStack() },
            onDeviceSettingsClick = { navController.navigate(SettingRoute.PushToastSettingRoute) }
        )
    }
    composable<SettingRoute.PushToastSettingRoute> {
        PushToastSettingScreen(onBackClick = { navController.popBackStack() })
    }
    composable<SettingRoute.PassKeyAddRoute> {
        PassKeyAddScreen(onBackClick = { navController.popBackStack() })
    }
    composable<SettingRoute.AppLockPasswordConfirmRoute> {
        PasswordConfirmScreen(onBackClick = { navController.popBackStack() })
    }
    composable<SettingRoute.AppLockPasswordModifyRoute> {
        PasswordModifyScreen(onBackClick = { navController.popBackStack() })
    }
    composable<SettingRoute.ReceiverDetailRoute> { backStackEntry ->
        ReceiverDetailRouteContent(navController, backStackEntry.toRoute())
    }
    composable<SettingRoute.ProfileEditRoute> {
        ProfileEditScreen(
            callbacks = ProfileEditCallbacks(
                onBackClick = { navController.popBackStack() },
                onUpdateSuccess = { navController.popBackStack() }
            )
        )
    }
    composable<SettingRoute.PasswordChangeRoute> {
        PasswordChangeScreen(onBackClick = { navController.popBackStack() })
    }
    composable<SettingRoute.ReceiverListRoute> {
        ReceiverListRouteContent(navController)
    }
    composable<SettingRoute.ReceiverRegisterRoute> {
        ReceiverRegisterRouteContent(navController)
    }
    composable<SettingRoute.PostDeliveryConditionRoute> {
        PostDeliveryConditionScreen(
            onBackClick = { navController.popBackStack() },
            onRegisterClick = { navController.popBackStack() }
        )
    }
    composable<SettingRoute.DailyAnswerRoute> { backStackEntry ->
        DailyAnswerRouteContent(navController, backStackEntry.toRoute())
    }
    composable<SettingRoute.ReceiverAfternoteListRoute> { backStackEntry ->
        ReceiverAfternoteListRouteContent(navController, backStackEntry.toRoute())
    }
    composable<SettingRoute.ReceiverTimeLetterListRoute> { backStackEntry ->
        ReceiverTimeLetterListRouteContent(navController, backStackEntry.toRoute())
    }
    composable<SettingRoute.NoticeRoute> {
        NoticeScreen(
            items = sampleNoticeItems(),
            onBackClick = { navController.popBackStack() },
            onItemClick = { }
        )
    }
}

private fun sampleNoticeItems(): List<NoticeItemUiModel> = List(10) { index ->
    NoticeItemUiModel(
        id = index.toString(),
        title = "애프터노트 서비스 출시 안내",
        dateText = "2025. 11. 20"
    )
}

private const val TAG_SETTING_NAV = "SettingNavGraph"

@Composable
private fun SettingMainRouteContent(navController: NavController) {
    val context = LocalContext.current
    val unhandledMessage = stringResource(R.string.setting_menu_not_connected)
    SettingMainScreen(
        onClick = { title ->
            when (title) {
                "프로필 수정" -> navController.navigate(SettingRoute.ProfileEditRoute)
                "비밀번호 변경" -> navController.navigate(SettingRoute.PasswordChangeRoute)
                "연결된 계정" -> navController.navigate(SettingRoute.ConnectedAccountsRoute)
                "알림 설정" -> navController.navigate(SettingRoute.PushToastSettingRoute)
                "수신자 목록" -> navController.navigate(SettingRoute.ReceiverListRoute)
                "수신자 등록" -> navController.navigate(SettingRoute.ReceiverRegisterRoute)
                "사후 전달 조건" -> navController.navigate(SettingRoute.PostDeliveryConditionRoute)
                "패스키 관리" -> navController.navigate(SettingRoute.PassKeyAddRoute)
                "앱 잠금 설정" -> navController.navigate(SettingRoute.AppLockPasswordModifyRoute)
                "공지사항" -> navController.navigate(SettingRoute.NoticeRoute)
                else -> {
                    Log.w(TAG_SETTING_NAV, "Unhandled setting menu: title=$title")
                    Toast
                        .makeText(context, unhandledMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    )
}

@Composable
private fun ReceiverDetailRouteContent(
    navController: NavController,
    route: SettingRoute.ReceiverDetailRoute
) {
    val detailViewModel: ReceiverDetailViewModel = hiltViewModel()
    val detailState by detailViewModel.uiState.collectAsStateWithLifecycle()

    val phoneNumberState = rememberTextFieldState()
    val emailState = rememberTextFieldState()
    LaunchedEffect(detailState.receiverId, detailState.name) {
        if (detailState.receiverId != 0L && detailState.name.isNotEmpty()) {
            phoneNumberState.edit { replace(0, length, detailState.phone ?: "") }
            emailState.edit { replace(0, length, detailState.email ?: "") }
        }
    }

    ReceiverDetailScreen(
        params = ReceiverDetailScreenParams(
            name = detailState.name,
            relationship = detailState.relation,
            phoneNumberState = phoneNumberState,
            emailState = emailState,
            dailyQuestionCount = detailState.dailyQuestionCount,
            timeLetterCount = detailState.timeLetterCount,
            afternoteCount = detailState.afterNoteCount
        ),
        callbacks = ReceiverDetailEditCallbacks(
            onBackClick = { navController.popBackStack() },
            onEditClick = { },
            onReceiverDetailImageClick = { },
            onDailyQuestionClick = {
                navController.navigate(
                    SettingRoute.DailyAnswerRoute(
                        receiverId = route.receiverId,
                        receiverName = detailState.name
                    )
                )
            },
            onTimeLetterClick = {
                navController.navigate(
                    SettingRoute.ReceiverTimeLetterListRoute(
                        receiverId = route.receiverId,
                        receiverName = detailState.name
                    )
                )
            },
            onAfternoteClick = {
                navController.navigate(
                    SettingRoute.ReceiverAfternoteListRoute(
                        receiverId = route.receiverId,
                        receiverName = detailState.name
                    )
                )
            }
        )
    )
}

@Composable
private fun ReceiverListRouteContent(navController: NavController) {
    val listViewModel: ReceiverListViewModel = hiltViewModel()
    val listState by listViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) { listViewModel.loadReceivers() }
    val receiversAsAfternote = listState.receivers.map { r ->
        AfternoteEditReceiver(id = r.receiverId.toString(), name = r.name, label = r.relation)
    }
    ReceiverManagementScreen(
        onBackClick = { navController.popBackStack() },
        onRegisterClick = { navController.navigate(SettingRoute.ReceiverRegisterRoute) },
        receivers = receiversAsAfternote,
        onReceiverClick = { receiver ->
            navController.navigate(SettingRoute.ReceiverDetailRoute(receiverId = receiver.id))
        }
    )
}

@Composable
private fun ReceiverRegisterRouteContent(navController: NavController) {
    val registerViewModel: RegisterReceiverViewModel = hiltViewModel()
    val registerState by registerViewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(registerState.registeredReceiverId) {
        if (registerState.registeredReceiverId != null) {
            registerViewModel.clearRegisteredReceiverId()
            navController.popBackStack()
        }
    }
    ReceiverRegisterScreen(
        onBackClick = { navController.popBackStack() },
        onRegisterClick = { navController.popBackStack() },
        registerViewModel = registerViewModel
    )
}

@Composable
private fun DailyAnswerRouteContent(
    navController: NavController,
    route: SettingRoute.DailyAnswerRoute
) {
    val dailyQuestionsViewModel: ReceiverDailyQuestionsViewModel = hiltViewModel()
    val dailyState by dailyQuestionsViewModel.uiState.collectAsStateWithLifecycle()
    val items = dailyState.items.map { item ->
        DailyAnswerItemUiModel(
            question = item.question,
            answer = item.answer,
            dateText = item.recordDate
        )
    }
    val receiverName = route.receiverName.ifBlank { "수신인" }
    DailyAnswerScreen(
        receiverName = receiverName,
        items = items,
        onBackClick = { navController.popBackStack() }
    )
}

@Composable
private fun ReceiverAfternoteListRouteContent(
    navController: NavController,
    route: SettingRoute.ReceiverAfternoteListRoute
) {
    val afterNotesViewModel: ReceiverAfternotesListViewModel = hiltViewModel()
    val afterNotesState by afterNotesViewModel.uiState.collectAsStateWithLifecycle()
    val afternoteItems = afterNotesState.items.map { item ->
        val (stringResId, iconResId) = getAfternoteSourceDisplayResIds(item.sourceType)
        val serviceName = stringResId?.let { stringResource(it) } ?: item.sourceType
        AfternoteListDisplayItem(
            id = item.sourceType,
            serviceName = serviceName,
            date = item.lastUpdatedAt,
            iconResId = iconResId
        )
    }
    val receiverName = route.receiverName.ifBlank { "수신인" }
    ReceiverAfternoteListScreen(
        receiverName = receiverName,
        items = afternoteItems,
        onBackClick = { navController.popBackStack() },
        onItemClick = { }
    )
}

@Composable
private fun ReceiverTimeLetterListRouteContent(
    navController: NavController,
    route: SettingRoute.ReceiverTimeLetterListRoute
) {
    val timeLettersViewModel: ReceiverTimeLettersListViewModel = hiltViewModel()
    val timeLettersState by timeLettersViewModel.uiState.collectAsStateWithLifecycle()
    val timeLetterItems = timeLettersState.items.map { item ->
        TimeLetterItem(
            id = item.timeLetterId.toString(),
            receivername = item.senderName,
            sendDate = item.sendAt,
            title = item.title,
            content = item.content,
            imageResId = null,
            theme = LetterTheme.BLUE
        )
    }
    val receiverName = route.receiverName.ifBlank { "수신인" }
    ReceiverTimeLetterListScreen(
        receiverName = receiverName,
        items = timeLetterItems,
        onBackClick = { navController.popBackStack() },
        onItemClick = { }
    )
}
