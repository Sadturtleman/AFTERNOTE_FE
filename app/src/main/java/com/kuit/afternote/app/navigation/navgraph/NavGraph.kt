@file:Suppress("AssignedValueIsNeverRead")

package com.kuit.afternote.app.navigation.navgraph

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuit.afternote.R
import com.kuit.afternote.app.compositionlocal.DataProviderLocals
import com.kuit.afternote.app.di.TokenManagerEntryPoint
import com.kuit.afternote.core.dummy.receiver.AfternoteListItemSeed
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailCallbacks
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailScreen
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailState
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailCallbacks
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailScreen
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailState
import com.kuit.afternote.core.ui.screen.afternotedetail.SocialNetworkDetailContent
import com.kuit.afternote.core.ui.screen.afternotedetail.SocialNetworkDetailScreen
import com.kuit.afternote.core.ui.screen.afternotedetail.rememberAfternoteDetailState
import com.kuit.afternote.core.uimodel.AfternoteListDisplayItem
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.presentation.navgraph.AfternoteEditStateHandling
import com.kuit.afternote.feature.afternote.presentation.navgraph.AfternoteNavGraphParams
import com.kuit.afternote.feature.afternote.presentation.navgraph.AfternoteRoute
import com.kuit.afternote.feature.afternote.presentation.navgraph.afternoteNavGraph
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditState
import com.kuit.afternote.feature.afternote.presentation.screen.FingerprintLoginScreen
import com.kuit.afternote.feature.afternote.presentation.screen.MemorialPlaylistStateHolder
import com.kuit.afternote.feature.dailyrecord.presentation.navigiation.recordNavGraph
import com.kuit.afternote.feature.dev.presentation.screen.DevModeScreen
import com.kuit.afternote.feature.dev.presentation.screen.ModeSelectionScreen
import com.kuit.afternote.feature.dev.presentation.screen.ScreenInfo
import com.kuit.afternote.feature.home.presentation.screen.HomeScreen
import com.kuit.afternote.feature.home.presentation.screen.HomeScreenEvent
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute
import com.kuit.afternote.feature.onboarding.presentation.navgraph.onboardingNavGraph
import com.kuit.afternote.feature.onboarding.presentation.screen.LoginScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.ProfileSettingScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SignUpScreen
import com.kuit.afternote.feature.onboarding.presentation.screen.SplashScreen
import com.kuit.afternote.feature.receiver.presentation.navgraph.ReceiverAfternoteListRoute
import com.kuit.afternote.feature.receiver.presentation.navgraph.ReceiverMainRoute
import com.kuit.afternote.feature.receiver.presentation.navgraph.ReceiverTimeLetterDetailRoute
import com.kuit.afternote.feature.receiver.presentation.navgraph.ReceiverTimeLetterRoute
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverAfternoteListEvent
import com.kuit.afternote.feature.receiver.presentation.screen.ReceiverOnboardingScreen
import com.kuit.afternote.feature.receiver.presentation.screen.VerifyReceiverScreen
import com.kuit.afternote.feature.receiver.presentation.screen.VerifySelfScreen
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverAfternoteListUiState
import com.kuit.afternote.feature.setting.presentation.navgraph.SettingRoute
import com.kuit.afternote.feature.setting.presentation.navgraph.settingNavGraph
import com.kuit.afternote.feature.timeletter.presentation.navgraph.TimeLetterRoute
import com.kuit.afternote.feature.timeletter.presentation.navgraph.timeLetterNavGraph
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG_FINGERPRINT = "FingerprintLogin"

private val devModeScreensList =
    listOf(
        ScreenInfo("애프터노트 목록 화면", "afternote_list"),
        ScreenInfo("애프터노트 상세 화면", "afternote_detail"),
        ScreenInfo("애프터노트 수정 화면", "afternote_edit"),
        ScreenInfo("스플래시 화면", "dev_splash"),
        ScreenInfo("로그인 화면", "dev_login"),
        ScreenInfo("마음의기록 메인 화면", "record_main"),
        ScreenInfo("지문 로그인 화면", "fingerprint_login"),
        ScreenInfo("타임레터 화면", "time_letter_main"),
        ScreenInfo("타임레터 작성 화면", "time_letter_writer"),
        ScreenInfo("임시저장 화면", "draft_letter"),
        ScreenInfo("수신자 목록 화면", "receive_list"),
        ScreenInfo("수신자 메인 (4탭)", "receiver_main/1"),
        ScreenInfo("수신자 타임레터 목록", "receiver_time_letter_list/1"),
        ScreenInfo("수신자 온보딩", "receiver_onboarding"),
        //ScreenInfo("타임레터 빈 화면", "letter_empty"),
        ScreenInfo("설정 화면", "setting_main")
    )

private fun navigateFromDevMode(route: String, nav: NavHostController) {
    when (route) {
        "afternote_list" -> nav.navigate(AfternoteRoute.AfternoteListRoute)
        "main" -> nav.navigate(AfternoteRoute.AfternoteListRoute)
        "afternote_detail" -> nav.navigate(AfternoteRoute.DetailRoute(itemId = ""))
        "afternote_edit" -> nav.navigate(AfternoteRoute.EditRoute())
        "fingerprint_login" -> nav.navigate(AfternoteRoute.FingerprintLoginRoute)
        "time_letter_main" -> nav.navigate(TimeLetterRoute.TimeLetterMainRoute)
        "time_letter_writer" -> nav.navigate(TimeLetterRoute.TimeLetterWriterRoute())
        "draft_letter" -> nav.navigate(TimeLetterRoute.DraftLetterRoute)
        "receive_list" -> nav.navigate(TimeLetterRoute.ReceiveListRoute)
        "receiver_afternote_main" -> nav.navigate("receiver_main/1")
        "receiver_main/1" -> nav.navigate("receiver_main/1")
        "setting_main" -> nav.navigate(SettingRoute.SettingMainRoute)
        else -> nav.navigate(route)
    }
}

@Composable
private fun FingerprintLoginRouteContent(navHostController: NavHostController) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val promptTitle = stringResource(R.string.biometric_prompt_title)
    val promptSubtitle = stringResource(R.string.biometric_prompt_subtitle)
    val notAvailableMessage = stringResource(R.string.biometric_not_available)
    val biometricPrompt =
        remember(activity) {
            try {
                activity?.let { fragActivity ->
                    val executor = ContextCompat.getMainExecutor(fragActivity)
                    BiometricPrompt(
                        fragActivity,
                        executor,
                        object : BiometricPrompt.AuthenticationCallback() {
                            override fun onAuthenticationSucceeded(
                                result: BiometricPrompt.AuthenticationResult
                            ) {
                                navHostController.popBackStack()
                            }
                        }
                    )
                }
            } catch (e: Throwable) {
                Log.e(TAG_FINGERPRINT, "BiometricPrompt creation failed", e)
                null
            }
        }
    val promptInfo =
        remember(promptTitle, promptSubtitle) {
            BiometricPrompt.PromptInfo.Builder()
                .setTitle(promptTitle)
                .setSubtitle(promptSubtitle)
                .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                .build()
        }
    FingerprintLoginScreen(
        onFingerprintAuthClick = {
            if (activity == null) return@FingerprintLoginScreen
            val biometricManager = BiometricManager.from(context)
            when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS ->
                    biometricPrompt?.authenticate(promptInfo)
                else ->
                    android.widget.Toast
                        .makeText(
                            context,
                            notAvailableMessage,
                            android.widget.Toast.LENGTH_SHORT
                        )
                        .show()
            }
        }
    )
}

@Composable
private fun ReceiverAfternoteListRouteContent(navHostController: NavHostController) {
    val receiverProvider = DataProviderLocals.LocalReceiverDataProvider.current
    BackHandler { navHostController.popBackStack() }
    val afternoteItems = remember(receiverProvider) {
        receiverProvider.getAfternoteListSeedsForReceiverList().map { seed ->
            AfternoteListDisplayItem(
                id = seed.id,
                serviceName = seed.serviceNameLiteral ?: "",
                date = seed.date,
                iconResId = seed.iconResId
            )
        }
    }
    var listState by remember {
        mutableStateOf(ReceiverAfternoteListUiState(items = afternoteItems))
    }
    ReceiverAfternoteListRoute(
        uiState = listState,
        onEvent = { event ->
            listState = when (event) {
                is ReceiverAfternoteListEvent.SelectTab ->
                    listState.copy(selectedTab = event.tab)
                is ReceiverAfternoteListEvent.SelectBottomNav ->
                    listState.copy(selectedBottomNavItem = event.navItem)
                is ReceiverAfternoteListEvent.ClickItem -> {
                    navHostController.navigate("receiver_afternote_detail/${event.itemId}")
                    listState
                }
            }
        }
    )
}

private enum class ReceiverDetailCategory { GALLERY, MEMORIAL_GUIDELINE, SOCIAL }

@Composable
private fun ReceiverAfternoteDetailContent(
    navHostController: NavHostController,
    itemId: String?
) {
    val receiverProvider = DataProviderLocals.LocalReceiverDataProvider.current
    val seed =
        remember(receiverProvider, itemId) {
            receiverProvider
                .getAfternoteListSeedsForReceiverList()
                .firstOrNull { it.id == itemId }
                ?: receiverProvider.getAfternoteListSeedsForReceiverList().firstOrNull()
        }
    val category = receiverDetailCategoryFromSeed(seed)
    val serviceName = seed?.serviceNameLiteral ?: ""
    val userName = receiverProvider.getDefaultReceiverTitleForDev()
    val defaultState = rememberAfternoteDetailState(
        defaultBottomNavItem = BottomNavItem.AFTERNOTE
    )
    when (category) {
        ReceiverDetailCategory.GALLERY -> GalleryDetailScreen(
            detailState = GalleryDetailState(
                serviceName = serviceName.ifEmpty { "갤러리" },
                userName = userName,
                finalWriteDate = seed?.date ?: ""
            ),
            callbacks = GalleryDetailCallbacks(
                onBackClick = { navHostController.popBackStack() },
                onEditClick = {}
            ),
            isEditable = false,
            uiState = defaultState
        )
        ReceiverDetailCategory.MEMORIAL_GUIDELINE -> MemorialGuidelineDetailScreen(
            detailState = MemorialGuidelineDetailState(
                userName = userName,
                finalWriteDate = seed?.date ?: ""
            ),
            callbacks = MemorialGuidelineDetailCallbacks(
                onBackClick = { navHostController.popBackStack() }
            ),
            isEditable = false,
            uiState = defaultState
        )
        ReceiverDetailCategory.SOCIAL -> SocialNetworkDetailScreen(
            content = SocialNetworkDetailContent(
                serviceName = serviceName,
                userName = userName
            ),
            isEditable = false,
            onBackClick = { navHostController.popBackStack() },
            state = defaultState
        )
    }
}

private fun receiverDetailCategoryFromSeed(seed: AfternoteListItemSeed?): ReceiverDetailCategory {
    return when (seed?.serviceNameLiteral) {
        "갤러리" -> ReceiverDetailCategory.GALLERY
        "추모 가이드라인" -> ReceiverDetailCategory.MEMORIAL_GUIDELINE
        else -> ReceiverDetailCategory.SOCIAL
    }
}

@Composable
private fun HomeScreenContent(
    onBottomNavTabSelected: (BottomNavItem) -> Unit,
    onSettingsClick: () -> Unit = {},
    onDailyQuestionCtaClick: () -> Unit,
    onTImeLetterClick: () -> Unit,
    onAfternoteClick: () -> Unit
) {
    HomeScreen(
        event = object : HomeScreenEvent {
            override fun onBottomNavTabSelected(item: BottomNavItem) =
                onBottomNavTabSelected(item)
            override fun onProfileClick() = Unit
            override fun onSettingsClick() = onSettingsClick()
            override fun onDailyQuestionCtaClick() = onDailyQuestionCtaClick()
            override fun onTimeLetterClick() = onTImeLetterClick()
            override fun onAfterNoteClick() = onAfternoteClick()
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(navHostController: NavHostController) {
    val context = LocalContext.current
    val tokenManager = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            TokenManagerEntryPoint::class.java
        ).tokenManager()
    }
    val scope = rememberCoroutineScope()
    val navigateToUserMode: () -> Unit = {
        scope.launch {
            val token = tokenManager.getAccessToken()
            withContext(Dispatchers.Main.immediate) {
                if (!token.isNullOrEmpty()) {
                    navHostController.navigate("home") {
                        launchSingleTop = true
                    }
                } else {
                    navHostController.navigate(OnboardingRoute.SplashRoute)
                }
            }
        }
    }

    val afternoteProvider = DataProviderLocals.LocalAfternoteEditDataProvider.current
    val receiverProvider = DataProviderLocals.LocalReceiverDataProvider.current
    var afternoteItems by remember { mutableStateOf(listOf<AfternoteItem>()) }
    val afternoteEditStateHolder = remember { mutableStateOf<AfternoteEditState?>(null) }
    val playlistStateHolder = remember { MemorialPlaylistStateHolder() }
    val devModeScreens = devModeScreensList

    val onBottomNavTabSelected: (BottomNavItem) -> Unit = { item ->
        when (item) {
            BottomNavItem.HOME ->
                navHostController.navigate("home") {
                    launchSingleTop = true
                }
            BottomNavItem.AFTERNOTE ->
                navHostController.navigate(AfternoteRoute.FingerprintLoginRoute) {
                    launchSingleTop = true
                }
            BottomNavItem.RECORD ->
                navHostController.navigate("record_main") {
                    launchSingleTop = true
                }
            BottomNavItem.TIME_LETTER ->
                navHostController.navigate(TimeLetterRoute.TimeLetterMainRoute) {
                    launchSingleTop = true
                }
        }
    }

    NavHost(
        navController = navHostController,
        startDestination = "dev"
    ) {
        composable("mode_selection") {
            ModeSelectionScreen(
                onUserModeClick = navigateToUserMode,
                onDevModeClick = { navHostController.navigate("dev") }
            )
        }

        composable("home") {
            HomeScreenContent(
                onBottomNavTabSelected = onBottomNavTabSelected,
                onSettingsClick = {
                    navHostController.navigate(SettingRoute.SettingMainRoute) {
                        launchSingleTop = true
                    }
                },
                onDailyQuestionCtaClick = {
                    navHostController.navigate("record_main") {
                        launchSingleTop = true
                    }
                },
                onAfternoteClick = {
                    navHostController.navigate(AfternoteRoute.FingerprintLoginRoute) {
                        launchSingleTop = true
                    }
                },
                onTImeLetterClick = {
                    navHostController.navigate(TimeLetterRoute.TimeLetterMainRoute) {
                        launchSingleTop = true
                    }
                }
            )
        }

        onboardingNavGraph(
            navController = navHostController,
            onNavigateToReceiverOnboarding = { navHostController.navigate("receiver_onboarding") }
        )
        recordNavGraph(
            navController = navHostController,
            onBottomNavTabSelected = onBottomNavTabSelected
        )
        afternoteNavGraph(
            navController = navHostController,
            params = AfternoteNavGraphParams(
                afternoteItemsProvider = { afternoteItems },
                onItemsUpdated = { newItems ->
                    afternoteItems = newItems
                },
                playlistStateHolder = playlistStateHolder,
                afternoteProvider = afternoteProvider,
                userName = receiverProvider.getDefaultReceiverTitleForDev(),
                editStateHandling = AfternoteEditStateHandling(
                    holder = afternoteEditStateHolder,
                    onClear = { afternoteEditStateHolder.value = null }
                )
            ),
            onBottomNavTabSelected = onBottomNavTabSelected
        )
        timeLetterNavGraph(
            navController = navHostController,
            onNavItemSelected = onBottomNavTabSelected
        )
        settingNavGraph(navController = navHostController)

        composable("dev") {
            DevModeScreen(
                screens = devModeScreens,
                onScreenClick = { navigateFromDevMode(it, navHostController) },
                onUserModeClick = navigateToUserMode
            )
        }

        // 개발자 모드용 화면들
        composable("dev_splash") {
            SplashScreen(
                onLoginClick = { navHostController.navigate("dev_login") },
                onStartClick = { navHostController.navigate("dev_signup") },
                onCheckClick = { navHostController.navigate("receiver_onboarding") },
                onSignUpClick = { navHostController.navigate("dev_signup") }
            )
        }

        composable("dev_login") {
            LoginScreen(
                onBackClick = { navHostController.popBackStack() },
                onSignUpClick = { navHostController.navigate("dev_signup") },
                onFindIdClick = {},
                onLoginSuccess = { navHostController.navigate(AfternoteRoute.AfternoteListRoute) }
            )
        }

        composable("dev_signup") {
            SignUpScreen(
                onBackClick = { navHostController.popBackStack() },
                onSettingClick = { email, password ->
                    navHostController.navigate("dev_profile_setting/$email/$password")
                }
            )
        }

        composable("dev_profile_setting/{email}/{password}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val password = backStackEntry.arguments?.getString("password") ?: ""
            ProfileSettingScreen(
                email = email,
                password = password,
                onFinishClick = { navHostController.navigate(AfternoteRoute.AfternoteListRoute) },
                onBackClick = { navHostController.popBackStack() }
            )
        }

        composable("receiver_main/{receiverId}") { backStackEntry ->
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: "1"
            ReceiverMainRoute(
                receiverId = receiverId,
                navController = navHostController,
                receiverTitle = receiverProvider.getDefaultReceiverTitleForDev(),
                albumCovers = afternoteProvider.getAlbumCovers()
            )
        }

        composable("receiver_afternote_list") {
            ReceiverAfternoteListRouteContent(navHostController = navHostController)
        }

        composable("receiver_afternote_detail/{itemId}") { backStackEntry ->
            ReceiverAfternoteDetailContent(
                navHostController = navHostController,
                itemId = backStackEntry.arguments?.getString("itemId")
            )
        }

        composable("receiver_time_letter_list/{receiverId}") {
            ReceiverTimeLetterRoute(
                navController = navHostController,
                onBackClick = { navHostController.popBackStack() }
            )
        }

        composable("receiver_time_letter_detail/{receiverId}/{timeLetterId}") {
            ReceiverTimeLetterDetailRoute(
                onBackClick = { navHostController.popBackStack() }
            )
        }

        composable("receiver_onboarding") {
            ReceiverOnboardingScreen(
                onLoginClick = { navHostController.popBackStack() },
                onStartClick = { navHostController.navigate("receiver_verify") },
                onSignUpClick = { navHostController.popBackStack() }
            )
        }

        composable("receiver_verify") {
            VerifyReceiverScreen(
                onBackClick = { navHostController.popBackStack() },
                onVerifySuccess = { navHostController.navigate("receiver_verify_self") }
            )
        }

        composable("receiver_verify_self") {
            VerifySelfScreen(
                onBackClick = { navHostController.popBackStack() },
                onNextClick = { navHostController.popBackStack() }
            )
        }

        composable("fingerprint_login") {
            FingerprintLoginRouteContent(navHostController = navHostController)
        }
    }
}
