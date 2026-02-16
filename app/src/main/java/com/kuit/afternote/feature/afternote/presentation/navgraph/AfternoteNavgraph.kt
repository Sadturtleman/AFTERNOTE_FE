package com.kuit.afternote.feature.afternote.presentation.navgraph

import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.list.AfternoteTab
import com.kuit.afternote.core.ui.component.list.AlbumCover
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailCallbacks
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailScreen
import com.kuit.afternote.core.ui.screen.afternotedetail.GalleryDetailState
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailCallbacks
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailScreen
import com.kuit.afternote.core.ui.screen.afternotedetail.MemorialGuidelineDetailState
import com.kuit.afternote.core.ui.screen.afternotedetail.SocialNetworkDetailContent
import com.kuit.afternote.core.ui.screen.afternotedetail.SocialNetworkDetailScreen
import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.ServiceType
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.screen.AddSongCallbacks
import com.kuit.afternote.feature.afternote.presentation.screen.AddSongScreen
import com.kuit.afternote.feature.afternote.presentation.screen.AddSongViewModel
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteDetailViewModel
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditSaveError
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditScreen
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteValidationError
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditScreenCallbacks
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditState
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditViewModel
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteItemMapper
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteListRoute
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteListRouteCallbacks
import com.kuit.afternote.feature.afternote.presentation.screen.FingerprintLoginScreen
import com.kuit.afternote.feature.afternote.presentation.screen.MemorialPlaylistRouteScreen
import com.kuit.afternote.feature.afternote.presentation.screen.MemorialPlaylistStateHolder
import com.kuit.afternote.feature.afternote.presentation.screen.RegisterAfternotePayload
import com.kuit.afternote.feature.afternote.presentation.screen.rememberAfternoteEditState
import com.kuit.afternote.ui.theme.AfternoteTheme

private const val TAG_AFTERNOTE_EDIT = "AfternoteEdit"
private const val TAG_FINGERPRINT = "FingerprintLogin"
private const val TAG_AFTERNOTE_DETAIL = "AfternoteDetail"

/**
 * Holder and clear callback for hoisted edit state (keeps param count ≤7).
 */
data class AfternoteEditStateHandling(
    val holder: MutableState<AfternoteEditState?>,
    val onClear: () -> Unit
)

/**
 * Triggers list refresh when an afternote is deleted so the list reflects the deletion.
 */
data class AfternoteListRefreshParams(
    val listRefreshRequestedProvider: () -> Boolean,
    val onListRefreshConsumed: () -> Unit,
    val onAfternoteDeleted: () -> Unit
)

/**
 * Parameters for [afternoteNavGraph]. Groups arguments to keep function param count ≤7.
 */
data class AfternoteNavGraphParams(
    val afternoteItemsProvider: () -> List<AfternoteItem>,
    val onItemsUpdated: (List<AfternoteItem>) -> Unit,
    val playlistStateHolder: MemorialPlaylistStateHolder,
    val afternoteProvider: AfternoteEditDataProvider,
    val userName: String,
    val editStateHandling: AfternoteEditStateHandling,
    val listRefresh: AfternoteListRefreshParams? = null
)

/**
 * afternote feature 전용 라이트 모드 테마 래퍼
 */
@Composable
fun AfternoteLightTheme(content: @Composable () -> Unit) {
    AfternoteTheme(darkTheme = false, content = content)
}

/**
 * afternote feature 전용 composable 래퍼
 * 내부적으로 라이트 모드를 강제 적용하여 다크모드를 비활성화합니다.
 */
inline fun <reified T : Any> NavGraphBuilder.afternoteComposable(noinline content: @Composable (NavBackStackEntry) -> Unit) {
    composable<T> { backStackEntry ->
        AfternoteTheme(darkTheme = false) {
            content(backStackEntry)
        }
    }
}

private fun resolveListItems(
    afternoteItems: List<AfternoteItem>,
    afternoteProvider: AfternoteEditDataProvider
): List<AfternoteItem> =
    afternoteItems.ifEmpty {
        AfternoteItemMapper.toAfternoteItemsWithStableIds(afternoteProvider.getDefaultAfternoteItems())
    }

@Composable
private fun AfternoteListRouteContent(
    navController: NavController,
    onBottomNavTabSelected: (BottomNavItem) -> Unit = {},
    onItemsUpdated: (List<AfternoteItem>) -> Unit,
    editStateHandling: AfternoteEditStateHandling,
    playlistStateHolder: MemorialPlaylistStateHolder,
    listRefresh: AfternoteListRefreshParams? = null
) {
    AfternoteListRoute(
        listRefreshRequested = listRefresh?.listRefreshRequestedProvider?.invoke() == true,
        onListRefreshConsumed = listRefresh?.onListRefreshConsumed ?: {},
        callbacks =
            AfternoteListRouteCallbacks(
                onNavigateToDetail = { itemId ->
                    navController.navigate(AfternoteRoute.DetailRoute(itemId = itemId))
                },
                onNavigateToGalleryDetail = { itemId ->
                    navController.navigate(AfternoteRoute.GalleryDetailRoute(itemId = itemId))
                },
                onNavigateToMemorialGuidelineDetail = { itemId ->
                    navController.navigate(AfternoteRoute.MemorialGuidelineDetailRoute(itemId = itemId))
                },
                onNavigateToAdd = { selectedTab ->
                    // 새 작성 진입 시 이전 편집 세션 상태를 항상 초기화하여
                    // 이전 세션의 카테고리/처리 방법이 남지 않도록 한다.
                    editStateHandling.onClear()
                    // 새 추모 가이드라인 작성 시 플레이리스트도 비워 서로 다른 애프터노트가 곡을 공유하지 않도록 한다.
                    playlistStateHolder.clearAllSongs()
                    val initialCategory =
                        if (selectedTab == AfternoteTab.ALL) null else selectedTab.label
                    Log.d(
                        "AfternoteNav",
                        "FAB onNavigateToAdd → navigate(EditRoute initialCategory=$initialCategory)"
                    )
                    navController.navigate(AfternoteRoute.EditRoute(initialCategory = initialCategory))
                },
                onBottomNavTabSelected = onBottomNavTabSelected
            ),
        initialItems = emptyList(),
        onItemsChanged = onItemsUpdated
    )
}

/** Types with a designed detail screen on the generic DetailRoute (social-style layout). */
private val DESIGNED_DETAIL_TYPES = setOf(ServiceType.SOCIAL_NETWORK, ServiceType.OTHER)

@Composable
private fun DetailLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DesignPendingDetailContent(onBackClick: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(title = "", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(Modifier.padding(paddingValues)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.design_pending))
        }
    }
}

// -- Detail Route (Social / Other types) --

@Composable
private fun AfternoteDetailRouteContent(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    userName: String,
    onAfternoteDeleted: () -> Unit = {}
) {
    val route = backStackEntry.toRoute<AfternoteRoute.DetailRoute>()
    val viewModel: AfternoteDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(route.itemId) {
        route.itemId.toLongOrNull()?.let { viewModel.loadDetail(it) }
    }

    LaunchedEffect(uiState.deleteSuccess) {
        if (uiState.deleteSuccess) {
            onAfternoteDeleted()
            navController.popBackStack()
        }
    }

    val detail = uiState.detail

    Log.d(
        TAG_AFTERNOTE_DETAIL,
        "DetailRoute: itemId=${route.itemId}, type=${detail?.type}, " +
            "isLoading=${uiState.isLoading}"
    )

    when {
        uiState.isLoading -> DetailLoadingContent()
        detail == null || detail.type !in DESIGNED_DETAIL_TYPES ->
            DesignPendingDetailContent(onBackClick = { navController.popBackStack() })
        else -> SocialNetworkDetailScreen(
            content = SocialNetworkDetailContent(
                serviceName = detail.title,
                userName = userName,
                accountId = detail.credentialsId ?: "",
                password = detail.credentialsPassword ?: "",
                accountProcessingMethod = detail.processMethod ?: "",
                processingMethods = detail.actions,
                message = detail.leaveMessage ?: "",
                finalWriteDate = detail.updatedAt
            ),
            onBackClick = { navController.popBackStack() },
            onEditClick = {
                navController.navigate(
                    AfternoteRoute.EditRoute(itemId = detail.id.toString())
                )
            },
            onDeleteConfirm = { viewModel.deleteAfternote(detail.id) }
        )
    }
}

// -- Gallery Detail Route --

@Composable
private fun AfternoteGalleryDetailRouteContent(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    userName: String,
    onAfternoteDeleted: () -> Unit = {}
) {
    val route = backStackEntry.toRoute<AfternoteRoute.GalleryDetailRoute>()
    val viewModel: AfternoteDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(route.itemId) {
        route.itemId.toLongOrNull()?.let { viewModel.loadDetail(it) }
    }

    LaunchedEffect(uiState.deleteSuccess) {
        if (uiState.deleteSuccess) {
            onAfternoteDeleted()
            navController.popBackStack()
        }
    }

    val detail = uiState.detail

    when {
        uiState.isLoading -> DetailLoadingContent()
        detail == null -> DesignPendingDetailContent(
            onBackClick = { navController.popBackStack() }
        )
        else -> GalleryDetailScreen(
            detailState = GalleryDetailState(
                serviceName = detail.title,
                userName = userName,
                finalWriteDate = detail.updatedAt,
                afternoteEditReceivers = detail.receivers.map { r ->
                    AfternoteEditReceiver(
                        id = "",
                        name = r.name,
                        label = r.relation
                    )
                },
                informationProcessingMethod = detail.processMethod ?: "",
                processingMethods = detail.actions,
                message = detail.leaveMessage ?: ""
            ),
            callbacks = GalleryDetailCallbacks(
                onBackClick = { navController.popBackStack() },
                onEditClick = {
                    navController.navigate(
                        AfternoteRoute.EditRoute(itemId = detail.id.toString())
                    )
                },
                onDeleteConfirm = { viewModel.deleteAfternote(detail.id) }
            )
        )
    }
}

// -- Memorial Guideline Detail Route --

@Composable
private fun AfternoteMemorialGuidelineDetailContent(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    userName: String,
    onAfternoteDeleted: () -> Unit = {}
) {
    val route = backStackEntry.toRoute<AfternoteRoute.MemorialGuidelineDetailRoute>()
    val viewModel: AfternoteDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(route.itemId) {
        route.itemId.toLongOrNull()?.let { viewModel.loadDetail(it) }
    }

    LaunchedEffect(uiState.deleteSuccess) {
        if (uiState.deleteSuccess) {
            onAfternoteDeleted()
            navController.popBackStack()
        }
    }

    val detail = uiState.detail

    when {
        uiState.isLoading -> DetailLoadingContent()
        detail == null -> DesignPendingDetailContent(
            onBackClick = { navController.popBackStack() }
        )
        else -> MemorialGuidelineDetailScreen(
            detailState = MemorialGuidelineDetailState(
                userName = userName,
                finalWriteDate = detail.updatedAt,
                profileImageUri = detail.playlist?.profilePhoto,
                albumCovers = detail.playlist?.songs?.map { s ->
                    AlbumCover(
                        id = (s.id ?: 0L).toString(),
                        imageUrl = s.coverUrl,
                        title = s.title
                    )
                } ?: emptyList(),
                songCount = detail.playlist?.songs?.size ?: 0,
                // Memorial only: "남기고 싶은 당부" comes from playlist.atmosphere; other categories use leaveMessage.
                lastWish = detail.playlist?.atmosphere ?: ""
            ),
            callbacks = MemorialGuidelineDetailCallbacks(
                onBackClick = { navController.popBackStack() },
                onEditClick = {
                    navController.navigate(
                        AfternoteRoute.EditRoute(itemId = detail.id.toString())
                    )
                },
                onDeleteConfirm = { viewModel.deleteAfternote(detail.id) }
            )
        )
    }
}

// -- Edit Route --

@Composable
private fun AfternoteEditRouteContent(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    afternoteItems: List<AfternoteItem>,
    playlistStateHolder: MemorialPlaylistStateHolder,
    afternoteProvider: AfternoteEditDataProvider,
    editStateHandling: AfternoteEditStateHandling,
    onBottomNavTabSelected: (BottomNavItem) -> Unit = {}
) {
    val route = backStackEntry.toRoute<AfternoteRoute.EditRoute>()
    val listItems = remember(afternoteItems, afternoteProvider) {
        resolveListItems(afternoteItems, afternoteProvider)
    }
    val initialItem = remember(route.itemId, listItems) {
        route.itemId?.let { id -> listItems.find { it.id == id } }
    }
    if (route.itemId != null && initialItem == null) {
        Log.w(
            TAG_AFTERNOTE_EDIT,
            "Edit opened but item not found: itemId=${route.itemId}, " +
                "listSize=${listItems.size}"
        )
    }

    val editViewModel: AfternoteEditViewModel = hiltViewModel()
    val saveState by editViewModel.saveState.collectAsStateWithLifecycle()
    val newState = rememberAfternoteEditState()
    // Nav-level holder keeps edit state alive across sub-routes (e.g. playlist, add song).
    // Both create and edit modes reuse the holder within a single edit session so that
    // category (e.g. 추모 가이드라인) and other form fields are not reset when returning.
    val existingState = editStateHandling.holder.value
    val state = existingState ?: newState
    LaunchedEffect(Unit) {
        if (editStateHandling.holder.value == null) {
            editStateHandling.holder.value = state
        }
    }

    // 새 작성 시 목록에서 선택한 탭(카테고리)을 반영한다.
    LaunchedEffect(route.initialCategory, route.itemId) {
        if (route.itemId == null && route.initialCategory != null) {
            state.onCategorySelected(route.initialCategory)
        }
    }

    // 편집 진입 시 상세 API를 통해 최신 데이터를 불러와 편집 상태를 채운다.
    // 추모 가이드라인인 경우 플레이리스트 홀더에도 상세 곡 목록을 채워 편집/상세 화면 곡 수가 일치하도록 한다.
    LaunchedEffect(route.itemId) {
        val id = route.itemId?.toLongOrNull() ?: return@LaunchedEffect
        if (state.loadedItemId != route.itemId) {
            editViewModel.loadForEdit(
                afternoteId = id,
                state = state,
                playlistStateHolder = playlistStateHolder
            )
        }
    }

    // 초기 진입 시 더미 곡으로 플레이리스트를 채우지 않는다.
    // 사용자는 "노래 추가하기" 플로우를 통해 직접 곡을 추가하며,
    // 실 서비스에서는 RealAfternoteEditDataProvider/실제 API가 곡 목록을 공급한다.

    LaunchedEffect(saveState.saveSuccess) {
        if (saveState.saveSuccess) {
            editStateHandling.onClear()
            navController.navigate(AfternoteRoute.AfternoteListRoute) {
                popUpTo(AfternoteRoute.AfternoteListRoute) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    val saveError = when {
        saveState.validationError == AfternoteValidationError.PLAYLIST_SONGS_REQUIRED &&
            playlistStateHolder.songs.isNotEmpty() -> null
        saveState.validationError != null -> AfternoteEditSaveError(
            stringResource(saveState.validationError!!.messageResId)
        )
        saveState.error != null -> AfternoteEditSaveError(saveState.error!!)
        else -> null
    }

    AfternoteEditScreen(
        callbacks = AfternoteEditScreenCallbacks(
            onBackClick = {
                editStateHandling.onClear()
                navController.popBackStack()
            },
            onRegisterClick = { payload: RegisterAfternotePayload ->
                editViewModel.saveAfternote(
                    editingId = route.itemId?.toLongOrNull() ?: initialItem?.id?.toLongOrNull(),
                    category = state.selectedCategory,
                    payload = payload,
                    receivers = state.afternoteEditReceivers,
                    playlistStateHolder = playlistStateHolder
                )
            },
            onNavigateToAddSong = { navController.navigate(AfternoteRoute.MemorialPlaylistRoute) },
            onBottomNavTabSelected = onBottomNavTabSelected
        ),
        playlistStateHolder = playlistStateHolder,
        // Don't pass initialItem when loadForEdit is used (route.itemId != null).
        // The list item has empty processMethod fields; passing it would race with
        // loadForEdit and set loadedItemId before the detail API responds,
        // causing the full detail data (including processMethod) to be discarded.
        initialItem = if (route.itemId != null) null else initialItem,
        state = state,
        saveError = saveError
    )
}

// -- Fingerprint Login --

@Composable
private fun AfternoteFingerprintLoginContent(
    navController: NavController,
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
                                navController.navigate(AfternoteRoute.AfternoteListRoute) {
                                    popUpTo(AfternoteRoute.FingerprintLoginRoute) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }
            } catch (e: Throwable) {
                Log.e(TAG_FINGERPRINT, "FingerprintLoginRoute: BiometricPrompt failed", e)
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
                BiometricManager.BIOMETRIC_SUCCESS -> biometricPrompt?.authenticate(promptInfo)
                else ->
                    android.widget.Toast
                        .makeText(context, notAvailableMessage, android.widget.Toast.LENGTH_SHORT)
                        .show()
            }
        },
        onBottomNavTabSelected = onBottomNavTabSelected
    )
}

// -- Add Song Route --

@Composable
private fun AfternoteAddSongRouteContent(
    navController: NavController,
    playlistStateHolder: MemorialPlaylistStateHolder,
    viewModel: AddSongViewModel
) {
    AddSongScreen(
        viewModel = viewModel,
        callbacks = AddSongCallbacks(
            onBackClick = { navController.popBackStack() },
            onSongsAdded = { added ->
                added.forEach { playlistStateHolder.addSong(it) }
                navController.popBackStack()
            }
        )
    )
}

// -- Nav Graph --

fun NavGraphBuilder.afternoteNavGraph(
    navController: NavController,
    params: AfternoteNavGraphParams,
    onBottomNavTabSelected: (BottomNavItem) -> Unit = {}
) {
    val afternoteProvider = params.afternoteProvider

    afternoteComposable<AfternoteRoute.AfternoteListRoute> {
        AfternoteListRouteContent(
            navController = navController,
            onBottomNavTabSelected = onBottomNavTabSelected,
            onItemsUpdated = params.onItemsUpdated,
            editStateHandling = params.editStateHandling,
            playlistStateHolder = params.playlistStateHolder,
            listRefresh = params.listRefresh
        )
    }

    val onAfternoteDeleted = params.listRefresh?.onAfternoteDeleted ?: {}

    afternoteComposable<AfternoteRoute.DetailRoute> { backStackEntry ->
        AfternoteDetailRouteContent(
            backStackEntry = backStackEntry,
            navController = navController,
            userName = params.userName,
            onAfternoteDeleted = onAfternoteDeleted
        )
    }

    afternoteComposable<AfternoteRoute.GalleryDetailRoute> { backStackEntry ->
        AfternoteGalleryDetailRouteContent(
            backStackEntry = backStackEntry,
            navController = navController,
            userName = params.userName,
            onAfternoteDeleted = onAfternoteDeleted
        )
    }

    afternoteComposable<AfternoteRoute.EditRoute> { backStackEntry ->
        val currentItems = params.afternoteItemsProvider()
        AfternoteEditRouteContent(
            backStackEntry = backStackEntry,
            navController = navController,
            afternoteItems = currentItems,
            playlistStateHolder = params.playlistStateHolder,
            afternoteProvider = afternoteProvider,
            editStateHandling = params.editStateHandling,
            onBottomNavTabSelected = onBottomNavTabSelected
        )
    }

    afternoteComposable<AfternoteRoute.MemorialGuidelineDetailRoute> { backStackEntry ->
        AfternoteMemorialGuidelineDetailContent(
            backStackEntry = backStackEntry,
            navController = navController,
            userName = params.userName,
            onAfternoteDeleted = onAfternoteDeleted
        )
    }

    afternoteComposable<AfternoteRoute.MemorialPlaylistRoute> {
        MemorialPlaylistRouteScreen(
            playlistStateHolder = params.playlistStateHolder,
            onBackClick = { navController.popBackStack() },
            onNavigateToAddSongScreen = { navController.navigate(AfternoteRoute.AddSongRoute) }
        )
    }

    afternoteComposable<AfternoteRoute.FingerprintLoginRoute> {
        AfternoteFingerprintLoginContent(
            navController = navController,
            onBottomNavTabSelected = onBottomNavTabSelected
        )
    }

    afternoteComposable<AfternoteRoute.AddSongRoute> {
        val addSongViewModel: AddSongViewModel = hiltViewModel()
        AfternoteAddSongRouteContent(
            navController = navController,
            playlistStateHolder = params.playlistStateHolder,
            viewModel = addSongViewModel
        )
    }
}
