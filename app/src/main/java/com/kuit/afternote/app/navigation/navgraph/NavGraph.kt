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
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kuit.afternote.R
import com.kuit.afternote.app.compositionlocal.DataProviderLocals
import com.kuit.afternote.app.di.ReceiverAuthSessionEntryPoint
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
import com.kuit.afternote.feature.afternote.presentation.navgraph.AfternoteListRefreshParams
import com.kuit.afternote.feature.afternote.presentation.navgraph.AfternoteNavGraphParams
import com.kuit.afternote.feature.afternote.presentation.navgraph.AfternoteRoute
import com.kuit.afternote.feature.afternote.presentation.navgraph.afternoteNavGraph
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditState
import com.kuit.afternote.feature.afternote.presentation.screen.FingerprintLoginScreen
import com.kuit.afternote.feature.afternote.presentation.screen.MemorialPlaylistStateHolder
import com.kuit.afternote.feature.dailyrecord.presentation.navgraph.recordNavGraph
import com.kuit.afternote.feature.home.presentation.screen.HomeScreen
import com.kuit.afternote.feature.home.presentation.screen.HomeScreenEvent
import com.kuit.afternote.feature.onboarding.presentation.navgraph.OnboardingRoute
import com.kuit.afternote.feature.onboarding.presentation.navgraph.onboardingNavGraph
import com.kuit.afternote.feature.receiver.presentation.navgraph.ReceiverAfternoteListRoute
import com.kuit.afternote.feature.receiver.presentation.navgraph.ReceiverMainRoute
import com.kuit.afternote.feature.receiver.presentation.navgraph.ReceiverTimeLetterDetailRoute
import com.kuit.afternote.feature.receiver.presentation.navgraph.ReceiverTimeLetterRoute
import com.kuit.afternote.feature.receiver.presentation.screen.afternote.ReceiverAfternoteListEvent
import com.kuit.afternote.feature.receiver.presentation.uimodel.ReceiverAfternoteListUiState
import com.kuit.afternote.feature.receiverauth.screen.ReceiverOnboardingScreen
import com.kuit.afternote.feature.receiverauth.screen.VerifySelfScreen
import com.kuit.afternote.feature.setting.presentation.navgraph.SettingRoute
import com.kuit.afternote.feature.setting.presentation.navgraph.settingNavGraph
import com.kuit.afternote.core.navigation.ReceiverRoute
import com.kuit.afternote.feature.timeletter.presentation.navgraph.TimeLetterRoute
import com.kuit.afternote.feature.timeletter.presentation.navgraph.timeLetterNavGraph
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.map

private const val TAG_FINGERPRINT = "FingerprintLogin"

private fun ejectToSplashIfLoggedOut(nav: NavHostController, isLoggedIn: Boolean?) {
    if (isLoggedIn != false) return
    val currentRoute = nav.currentBackStackEntry?.destination?.route
    if (currentRoute?.contains("Splash") == true) return
    nav.navigate(OnboardingRoute.SplashRoute) {
        popUpTo(nav.graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
}

private fun dispatchFromRoot(nav: NavHostController, isLoggedIn: Boolean?) {
    when (isLoggedIn) {
        true -> nav.navigate("home") {
            popUpTo("root") { inclusive = true }
            launchSingleTop = true
        }
        false -> nav.navigate(OnboardingRoute.SplashRoute) {
            popUpTo("root") { inclusive = true }
            launchSingleTop = true
        }
        null -> Unit
    }
}

private fun redirectFromHomeIfLoggedOut(nav: NavHostController, isLoggedIn: Boolean?) {
    if (isLoggedIn != false) return
    nav.navigate(OnboardingRoute.SplashRoute) {
        launchSingleTop = true
        popUpTo("home") { inclusive = true }
    }
}

private fun createBottomNavTabSelectedHandler(nav: NavHostController): (BottomNavItem) -> Unit =
    { item ->
        when (item) {
            BottomNavItem.HOME -> nav.navigate("home") { launchSingleTop = true }
            BottomNavItem.AFTERNOTE ->
                nav.navigate(AfternoteRoute.FingerprintLoginRoute) { launchSingleTop = true }
            BottomNavItem.RECORD -> nav.navigate("record_main") { launchSingleTop = true }
            BottomNavItem.TIME_LETTER ->
                nav.navigate(TimeLetterRoute.TimeLetterMainRoute) { launchSingleTop = true }
        }
    }

@Composable
private fun FingerprintLoginRouteContent(
    navHostController: NavHostController,
    onBottomNavTabSelected: (BottomNavItem) -> Unit
) {
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
        },
        onBottomNavTabSelected = onBottomNavTabSelected
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
    val userName = receiverProvider.getDefaultReceiverTitle()
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
    val receiverAuthSessionHolder = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            ReceiverAuthSessionEntryPoint::class.java
        ).receiverAuthSessionHolder()
    }
    // null = still loading from DataStore, true/false = resolved
    val isLoggedIn by remember(tokenManager) {
        tokenManager.isLoggedInFlow.map<Boolean, Boolean?> { it }
    }.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(isLoggedIn) {
        ejectToSplashIfLoggedOut(navHostController, isLoggedIn)
    }

    val afternoteProvider = DataProviderLocals.LocalAfternoteEditDataProvider.current
    val receiverProvider = DataProviderLocals.LocalReceiverDataProvider.current
    var afternoteItems by remember { mutableStateOf(listOf<AfternoteItem>()) }
    val afternoteEditStateHolder = remember { mutableStateOf<AfternoteEditState?>(null) }
    val playlistStateHolder = remember { MemorialPlaylistStateHolder() }
    var listRefreshRequested by remember { mutableStateOf(false) }

    val onBottomNavTabSelected = createBottomNavTabSelectedHandler(navHostController)

    NavHost(
        navController = navHostController,
        startDestination = "root",
        // Disable default transition animations to prevent touch events being
        // blocked by the animation overlay during screen transitions.
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable("root") {
            LaunchedEffect(isLoggedIn) {
                dispatchFromRoot(navHostController, isLoggedIn)
            }
        }

        composable("home") {
            LaunchedEffect(isLoggedIn) {
                redirectFromHomeIfLoggedOut(navHostController, isLoggedIn)
            }
            if (isLoggedIn == true) {
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
            } else {
                Box(Modifier.fillMaxSize())
            }
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
                userName = receiverProvider.getDefaultReceiverTitle(),
                editStateHandling = AfternoteEditStateHandling(
                    holder = afternoteEditStateHolder,
                    onClear = { afternoteEditStateHolder.value = null }
                ),
                listRefresh = AfternoteListRefreshParams(
                    listRefreshRequestedProvider = { listRefreshRequested },
                    onListRefreshConsumed = { listRefreshRequested = false },
                    onAfternoteDeleted = { listRefreshRequested = true }
                ),
                onNavigateToSelectReceiver = {
                    navHostController.navigate(ReceiverRoute.ReceiverListRoute)
                }
            ),
            onBottomNavTabSelected = onBottomNavTabSelected
        )
        timeLetterNavGraph(
            navController = navHostController,
            onNavItemSelected = onBottomNavTabSelected
        )
        settingNavGraph(
            navController = navHostController,
            onBottomNavTabSelected = onBottomNavTabSelected
        )

        composable("receiver_main/{receiverId}") { backStackEntry ->
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: "1"
            ReceiverMainRoute(
                receiverId = receiverId,
                navController = navHostController,
                receiverTitle = receiverProvider.getDefaultReceiverTitle(), // 팀원 의도 반영
                albumCovers = afternoteProvider.getAlbumCovers(),
                receiverAuthSessionHolder = receiverAuthSessionHolder // 경민님 로직 반영
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

        composable("receiver_time_letter_detail/{receiverId}/{timeLetterReceiverId}") {
            ReceiverTimeLetterDetailRoute(
                onBackClick = { navHostController.popBackStack() }
            )
        }

        composable("receiver_onboarding") {
            ReceiverOnboardingScreen(
                onLoginClick = { navHostController.popBackStack() },
                onStartClick = { navHostController.navigate("receiver_verify_self") },
                onSignUpClick = { navHostController.popBackStack() }
            )
        }

        composable("receiver_verify_self") {
            VerifySelfScreen(
                onBackClick = { navHostController.popBackStack() },
                onNextClick = { navHostController.popBackStack() },
                onCompleteClick = { receiverId, authCode, senderName ->
                    receiverAuthSessionHolder.setAuthCode(authCode)
                    receiverAuthSessionHolder.setSenderName(senderName)
                    navHostController.navigate("receiver_main/$receiverId") {
                        launchSingleTop = true
                        popUpTo("receiver_onboarding") { inclusive = true }
                    }
                }
            )
        }

        composable("fingerprint_login") {
            FingerprintLoginRouteContent(
                navHostController = navHostController,
                onBottomNavTabSelected = onBottomNavTabSelected
            )
        }
    }
}
