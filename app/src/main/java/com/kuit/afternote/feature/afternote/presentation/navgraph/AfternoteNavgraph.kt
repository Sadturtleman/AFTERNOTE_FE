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
import com.kuit.afternote.core.domain.model.AfternoteServiceType
import com.kuit.afternote.core.navigation.SELECTED_RECEIVER_ID_KEY
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
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.screen.AddSongCallbacks
import com.kuit.afternote.feature.afternote.presentation.screen.AddSongScreen
import com.kuit.afternote.feature.afternote.presentation.screen.AddSongViewModel
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteDetailViewModel
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditSaveError
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditScreen
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditScreenCallbacks
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditState
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditViewModel
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteItemMapper
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteListRoute
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteListRouteCallbacks
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteSaveState
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteValidationError
import com.kuit.afternote.feature.afternote.presentation.screen.FingerprintLoginScreen
import com.kuit.afternote.feature.afternote.presentation.screen.MemorialPlaylistRouteScreen
import com.kuit.afternote.feature.afternote.presentation.screen.MemorialPlaylistStateHolder
import com.kuit.afternote.feature.afternote.presentation.screen.RegisterAfternotePayload
import com.kuit.afternote.feature.afternote.presentation.screen.SaveAfternoteMemorialMedia
import com.kuit.afternote.feature.afternote.presentation.screen.rememberAfternoteEditState
import com.kuit.afternote.ui.theme.AfternoteTheme

private const val TAG_AFTERNOTE_EDIT = "AfternoteEdit"
private const val TAG_FINGERPRINT = "FingerprintLogin"
private const val TAG_AFTERNOTE_DETAIL = "AfternoteDetail"

private const val CATEGORY_MEMORIAL_GUIDELINE = "추모 가이드라인"

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
    val listRefresh: AfternoteListRefreshParams? = null,
    val onNavigateToSelectReceiver: () -> Unit = {}
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
private val DESIGNED_DETAIL_TYPES = setOf(AfternoteServiceType.SOCIAL_NETWORK)

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
                finalWriteDate = detail.updatedAt.ifEmpty { detail.createdAt },
                afternoteEditReceivers = detail.receivers.map { r ->
                    AfternoteEditReceiver(
                        id = "",
                        name = r.name,
                        label = r.relation
                    )
                }
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
                finalWriteDate = detail.updatedAt.ifEmpty { detail.createdAt },
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
                finalWriteDate = detail.updatedAt.ifEmpty { detail.createdAt },
                profileImageUri = detail.playlist?.memorialPhotoUrl ?: detail.playlist?.profilePhoto,
                afternoteEditReceivers = detail.receivers.map { r ->
                    AfternoteEditReceiver(
                        id = "",
                        name = r.name,
                        label = r.relation
                    )
                },
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

/** Result of save error resolution: either a validation string resource id or a raw message. */
private sealed class EditSaveErrorResult {
    data class Validation(val messageResId: Int) : EditSaveErrorResult()
    data class Raw(val message: String) : EditSaveErrorResult()
}

/**
 * Returns the save error to show, or null. Suppresses PLAYLIST_SONGS_REQUIRED when playlist has songs.
 * Returns Validation(resId) or Raw(message) so the Composable can call stringResource(resId) in Composable context.
 */
private fun editSaveErrorFromState(
    saveState: AfternoteSaveState,
    playlistSongCount: Int
): EditSaveErrorResult? {
    if (saveState.validationError == AfternoteValidationError.PLAYLIST_SONGS_REQUIRED &&
        playlistSongCount > 0
    ) return null
    saveState.validationError?.let { return EditSaveErrorResult.Validation(it.messageResId) }
    saveState.error?.let { return EditSaveErrorResult.Raw(it) }
    return null
}

private data class EditScreenCallbacksParams(
    val navController: NavController,
    val editViewModel: AfternoteEditViewModel,
    val editStateHandling: AfternoteEditStateHandling,
    val state: AfternoteEditState,
    val route: AfternoteRoute.EditRoute,
    val initialItem: AfternoteItem?,
    val playlistStateHolder: MemorialPlaylistStateHolder,
    val onNavigateToSelectReceiver: () -> Unit,
    val onBottomNavTabSelected: (BottomNavItem) -> Unit
)

private data class AfternoteEditRouteContentParams(
    val backStackEntry: NavBackStackEntry,
    val navController: NavController,
    val afternoteItems: List<AfternoteItem>,
    val playlistStateHolder: MemorialPlaylistStateHolder,
    val afternoteProvider: AfternoteEditDataProvider,
    val editStateHandling: AfternoteEditStateHandling,
    val onNavigateToSelectReceiver: () -> Unit = {},
    val onBottomNavTabSelected: (BottomNavItem) -> Unit = {}
)

private fun navigateToAfternoteListOnSaveSuccess(
    editStateHandling: AfternoteEditStateHandling,
    navController: NavController
) {
    editStateHandling.onClear()
    navController.navigate(AfternoteRoute.AfternoteListRoute) {
        popUpTo(AfternoteRoute.AfternoteListRoute) { inclusive = true }
        launchSingleTop = true
    }
}

private fun applyUploadedThumbnailAndClear(
    url: String,
    state: AfternoteEditState,
    viewModel: AfternoteEditViewModel
) {
    runCatching { state.onFuneralThumbnailDataUrlReady(url) }
        .onFailure { e -> Log.e(TAG_AFTERNOTE_EDIT, "apply uploadedThumbnailUrl failed", e) }
    viewModel.clearUploadedThumbnailUrl()
}

/**
 * If the back stack entry has a selected receiver id from the receiver list screen, applies it to
 * [state] and removes the key. No-op if key is absent.
 */
private fun tryApplyReceiverSelectionFromSavedState(
    backStackEntry: NavBackStackEntry,
    viewModel: AfternoteEditViewModel,
    state: AfternoteEditState
) {
    val id = backStackEntry.savedStateHandle[SELECTED_RECEIVER_ID_KEY] as? Long ?: return
    backStackEntry.savedStateHandle.remove<Long>(SELECTED_RECEIVER_ID_KEY)
    val receiver = viewModel.getReceiverById(id) ?: return
    state.addReceiverFromSelection(receiver.receiverId, receiver.name, receiver.relation)
}

private fun buildEditScreenCallbacks(params: EditScreenCallbacksParams): AfternoteEditScreenCallbacks =
    AfternoteEditScreenCallbacks(
        onBackClick = {
            params.editStateHandling.onClear()
            params.navController.popBackStack()
        },
        onRegisterClick = { payload: RegisterAfternotePayload ->
            params.editViewModel.saveAfternote(
                editingId = params.route.itemId?.toLongOrNull() ?: params.initialItem?.id?.toLongOrNull(),
                category = params.state.selectedCategory,
                payload = payload,
                selectedReceiverIds = params.state.afternoteEditReceivers.mapNotNull { it.id.toLongOrNull() },
                playlistStateHolder = params.playlistStateHolder,
                memorialMedia = SaveAfternoteMemorialMedia(
                    funeralVideoUrl = params.state.funeralVideoUrl,
                    funeralThumbnailUrl = params.state.funeralThumbnailUrl,
                    memorialPhotoUrl = params.state.memorialPhotoUrl,
                    pickedMemorialPhotoUri = params.state.pickedMemorialPhotoUri
                )
            )
        },
        onNavigateToAddSong = { params.navController.navigate(AfternoteRoute.MemorialPlaylistRoute) },
        onNavigateToSelectReceiver = params.onNavigateToSelectReceiver,
        onBottomNavTabSelected = params.onBottomNavTabSelected,
        onThumbnailBytesReady = { bytes ->
            if (bytes != null) params.editViewModel.uploadMemorialThumbnail(bytes)
        }
    )

@Composable
private fun AfternoteEditRouteContent(params: AfternoteEditRouteContentParams) {
    val route = params.backStackEntry.toRoute<AfternoteRoute.EditRoute>()
    val listItems = remember(params.afternoteItems, params.afternoteProvider) {
        resolveListItems(params.afternoteItems, params.afternoteProvider)
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
    val existingState = params.editStateHandling.holder.value
    val state = existingState ?: newState

    LaunchedEffect(Unit) {
        if (params.editStateHandling.holder.value == null) {
            params.editStateHandling.holder.value = state
        }
    }
    LaunchedEffect(Unit) { editViewModel.loadReceivers() }

    val isEditCurrentDestination = params.navController.currentBackStackEntry == params.backStackEntry
    LaunchedEffect(isEditCurrentDestination) {
        if (isEditCurrentDestination) {
            tryApplyReceiverSelectionFromSavedState(
                params.backStackEntry,
                editViewModel,
                state
            )
        }
    }

    LaunchedEffect(route.initialCategory, route.itemId) {
        if (route.itemId == null && route.initialCategory != null) {
            state.onCategorySelected(route.initialCategory)
        }
    }

    LaunchedEffect(route.itemId) {
        val id = route.itemId?.toLongOrNull() ?: return@LaunchedEffect
        if (state.loadedItemId != route.itemId) {
            editViewModel.loadForEdit(
                afternoteId = id,
                state = state,
                playlistStateHolder = params.playlistStateHolder
            )
        }
    }

    LaunchedEffect(saveState.saveSuccess) {
        if (saveState.saveSuccess) {
            navigateToAfternoteListOnSaveSuccess(params.editStateHandling, params.navController)
        }
    }

    val uploadedThumbnailUrl by editViewModel.uploadedThumbnailUrl.collectAsStateWithLifecycle(initialValue = null)
    LaunchedEffect(uploadedThumbnailUrl) {
        uploadedThumbnailUrl?.let { url ->
            applyUploadedThumbnailAndClear(url, state, editViewModel)
        }
    }

    val errorResult = remember(
        saveState.validationError,
        saveState.error,
        params.playlistStateHolder.songs.size
    ) { editSaveErrorFromState(saveState, params.playlistStateHolder.songs.size) }
    val saveError = when (errorResult) {
        is EditSaveErrorResult.Validation -> AfternoteEditSaveError(stringResource(errorResult.messageResId))
        is EditSaveErrorResult.Raw -> AfternoteEditSaveError(errorResult.message)
        null -> null
    }

    AfternoteEditScreen(
        callbacks = buildEditScreenCallbacks(
            EditScreenCallbacksParams(
                navController = params.navController,
                editViewModel = editViewModel,
                editStateHandling = params.editStateHandling,
                state = state,
                route = route,
                initialItem = initialItem,
                playlistStateHolder = params.playlistStateHolder,
                onNavigateToSelectReceiver = params.onNavigateToSelectReceiver,
                onBottomNavTabSelected = params.onBottomNavTabSelected
            )
        ),
        playlistStateHolder = params.playlistStateHolder,
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
            AfternoteEditRouteContentParams(
                backStackEntry = backStackEntry,
                navController = navController,
                afternoteItems = currentItems,
                playlistStateHolder = params.playlistStateHolder,
                afternoteProvider = afternoteProvider,
                editStateHandling = params.editStateHandling,
                onNavigateToSelectReceiver = params.onNavigateToSelectReceiver,
                onBottomNavTabSelected = onBottomNavTabSelected
            )
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
