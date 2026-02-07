package com.kuit.afternote.feature.afternote.presentation.navgraph

import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.core.ui.screen.SocialNetworkDetailContent
import com.kuit.afternote.core.ui.screen.SocialNetworkDetailScreen
import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.feature.afternote.presentation.screen.AddSongCallbacks
import com.kuit.afternote.feature.afternote.presentation.screen.AddSongScreen
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditScreen
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditState
import com.kuit.afternote.feature.afternote.presentation.screen.rememberAfternoteEditState
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
import com.kuit.afternote.feature.afternote.domain.model.ServiceType
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteItemMapper
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteListRoute
import com.kuit.afternote.feature.afternote.presentation.screen.RegisterAfternotePayload
import com.kuit.afternote.feature.afternote.presentation.screen.FingerprintLoginScreen
import com.kuit.afternote.feature.afternote.presentation.screen.GalleryDetailCallbacks
import com.kuit.afternote.feature.afternote.presentation.screen.GalleryDetailScreen
import com.kuit.afternote.feature.afternote.presentation.screen.GalleryDetailState
import com.kuit.afternote.feature.afternote.presentation.screen.MemorialPlaylistRouteScreen
import com.kuit.afternote.feature.afternote.presentation.screen.MemorialPlaylistStateHolder
import com.kuit.afternote.ui.theme.AfternoteTheme

private const val TAG_AFTERNOTE_EDIT = "AfternoteEdit"
private const val TAG_FINGERPRINT = "FingerprintLogin"

/**
 * Holder and clear callback for hoisted edit state (keeps param count ≤7).
 */
data class AfternoteEditStateHandling(
    val holder: MutableState<AfternoteEditState?>,
    val onClear: () -> Unit
)

/**
 * afternote feature 전용 라이트 모드 테마 래퍼
 * Preview 함수에서 사용하여 다크 모드를 강제로 비활성화합니다.
 *
 * 사용 예시:
 * ```
 * @Preview(showBackground = true)
 * @Composable
 * private fun MyScreenPreview() {
 *     AfternoteLightTheme {
 *         MyScreen()
 *     }
 * }
 * ```
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
        AfternoteItemMapper.toAfternoteItemsWithStableIds(afternoteProvider.getAfternoteItemsForDev())
    }

@Composable
private fun AfternoteListRouteContent(
    navController: NavController,
    listItems: List<AfternoteItem>
) {
    AfternoteListRoute(
        onNavigateToDetail = { itemId ->
            navController.navigate(AfternoteRoute.DetailRoute(itemId = itemId))
        },
        onNavigateToGalleryDetail = { itemId ->
            navController.navigate(AfternoteRoute.GalleryDetailRoute(itemId = itemId))
        },
        onNavigateToAdd = { navController.navigate(AfternoteRoute.EditRoute()) },
        initialItems = listItems
    )
}

/** Detail categories that have a designed screen: 갤러리 및 파일 (own route), 소셜 네트워크 (this route). */
private val DESIGNED_DETAIL_TYPES = setOf(ServiceType.SOCIAL_NETWORK, ServiceType.OTHER)

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

@Composable
private fun AfternoteDetailRouteContent(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    listItems: List<AfternoteItem>,
    userName: String
) {
    val route = backStackEntry.toRoute<AfternoteRoute.DetailRoute>()
    val item = listItems.find { it.id == route.itemId }
    val showDesignPending = item == null || item.type !in DESIGNED_DETAIL_TYPES

    if (showDesignPending) {
        DesignPendingDetailContent(onBackClick = { navController.popBackStack() })
    } else {
        SocialNetworkDetailScreen(
            content = SocialNetworkDetailContent(
                serviceName = item.serviceName,
                userName = userName,
                accountId = item.accountId,
                password = item.password,
                accountProcessingMethod = item.accountProcessingMethod,
                processingMethods = item.processingMethods.map { it.text },
                message = item.message,
                finalWriteDate = item.date
            ),
            onBackClick = { navController.popBackStack() },
            onEditClick = { navController.navigate(AfternoteRoute.EditRoute(itemId = item.id)) }
        )
    }
}

@Composable
private fun AfternoteGalleryDetailRouteContent(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    listItems: List<AfternoteItem>,
    afternoteProvider: AfternoteEditDataProvider,
    userName: String
) {
    val route = backStackEntry.toRoute<AfternoteRoute.GalleryDetailRoute>()
    val item = listItems.find { it.id == route.itemId }
    GalleryDetailScreen(
        detailState = GalleryDetailState(
            afternoteEditReceivers = afternoteProvider.getAfternoteEditReceivers(),
            serviceName = item?.serviceName ?: "갤러리",
            userName = userName,
            finalWriteDate = item?.date ?: "2025.11.26.",
            informationProcessingMethod = item?.informationProcessingMethod ?: "",
            processingMethods = item?.galleryProcessingMethods?.map { it.text } ?: emptyList(),
            message = item?.message ?: ""
        ),
        callbacks = GalleryDetailCallbacks(
            onBackClick = { navController.popBackStack() },
            onEditClick = {
                if (item != null) {
                    navController.navigate(AfternoteRoute.EditRoute(itemId = item.id))
                }
            }
        )
    )
}

@Composable
private fun AfternoteEditRouteContent(
    backStackEntry: NavBackStackEntry,
    navController: NavController,
    afternoteItems: List<AfternoteItem>,
    onItemsUpdated: (List<AfternoteItem>) -> Unit,
    playlistStateHolder: MemorialPlaylistStateHolder,
    afternoteProvider: AfternoteEditDataProvider,
    editStateHandling: AfternoteEditStateHandling
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
                "listSize=${listItems.size}, " +
                "ids=${listItems.take(3).map { it.id }}"
        )
    }

    val newState = rememberAfternoteEditState()
    val state = editStateHandling.holder.value ?: newState
    LaunchedEffect(Unit) {
        if (editStateHandling.holder.value == null) {
            Log.d(TAG_AFTERNOTE_EDIT, "Initialising afternoteEditStateHolder with newState")
            editStateHandling.holder.value = newState
        }
    }

    LaunchedEffect(playlistStateHolder, afternoteProvider) {
        if (playlistStateHolder.songs.isEmpty()) {
            playlistStateHolder.initializeSongs(afternoteProvider.getSongs())
        }
    }

    AfternoteEditScreen(
        onBackClick = {
            editStateHandling.onClear()
            navController.popBackStack()
        },
        onRegisterClick = { payload: RegisterAfternotePayload ->
            if (initialItem != null) {
                val updatedItems = afternoteItems.map {
                    if (it.id == initialItem.id) AfternoteItemMapper.fromPayload(payload)
                    else it
                }
                onItemsUpdated(updatedItems)
            } else {
                val newItem = AfternoteItemMapper.fromPayload(payload)
                onItemsUpdated(afternoteItems + newItem)
            }
            editStateHandling.onClear()
            navController.navigate(AfternoteRoute.AfternoteListRoute) {
                popUpTo(AfternoteRoute.AfternoteListRoute) { inclusive = true }
                launchSingleTop = true
            }
        },
        onNavigateToAddSong = { navController.navigate(AfternoteRoute.MemorialPlaylistRoute) },
        playlistStateHolder = playlistStateHolder,
        initialItem = initialItem,
        state = state
    )
}

@Composable
private fun AfternoteFingerprintLoginContent(navController: NavController) {
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
                                navController.popBackStack()
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
        }
    )
}

@Composable
private fun AfternoteAddSongRouteContent(
    navController: NavController,
    playlistStateHolder: MemorialPlaylistStateHolder,
    afternoteProvider: AfternoteEditDataProvider
) {
    AddSongScreen(
        songs = afternoteProvider.getAddSongSearchResults(),
        callbacks = AddSongCallbacks(
            onBackClick = { navController.popBackStack() },
            onSongsAdded = { added ->
                added.forEach { playlistStateHolder.addSong(it) }
                navController.popBackStack()
            }
        )
    )
}

fun NavGraphBuilder.afternoteNavGraph(
    navController: NavController,
    afternoteItems: List<AfternoteItem>,
    onItemsUpdated: (List<AfternoteItem>) -> Unit,
    playlistStateHolder: MemorialPlaylistStateHolder,
    afternoteProvider: AfternoteEditDataProvider,
    userName: String,
    editStateHandling: AfternoteEditStateHandling
) {
    afternoteComposable<AfternoteRoute.AfternoteListRoute> {
        val listItems = resolveListItems(afternoteItems, afternoteProvider)
        AfternoteListRouteContent(navController = navController, listItems = listItems)
    }

    afternoteComposable<AfternoteRoute.DetailRoute> { backStackEntry ->
        val listItems = resolveListItems(afternoteItems, afternoteProvider)
        AfternoteDetailRouteContent(
            backStackEntry = backStackEntry,
            navController = navController,
            listItems = listItems,
            userName = userName
        )
    }

    afternoteComposable<AfternoteRoute.GalleryDetailRoute> { backStackEntry ->
        val listItems = resolveListItems(afternoteItems, afternoteProvider)
        AfternoteGalleryDetailRouteContent(
            backStackEntry = backStackEntry,
            navController = navController,
            listItems = listItems,
            afternoteProvider = afternoteProvider,
            userName = userName
        )
    }

    afternoteComposable<AfternoteRoute.EditRoute> { backStackEntry ->
        AfternoteEditRouteContent(
            backStackEntry = backStackEntry,
            navController = navController,
            afternoteItems = afternoteItems,
            onItemsUpdated = onItemsUpdated,
            playlistStateHolder = playlistStateHolder,
            afternoteProvider = afternoteProvider,
            editStateHandling = editStateHandling
        )
    }

    afternoteComposable<AfternoteRoute.MemorialPlaylistRoute> {
        MemorialPlaylistRouteScreen(
            playlistStateHolder = playlistStateHolder,
            onBackClick = { navController.popBackStack() },
            onNavigateToAddSongScreen = { navController.navigate(AfternoteRoute.AddSongRoute) }
        )
    }

    afternoteComposable<AfternoteRoute.FingerprintLoginRoute> {
        AfternoteFingerprintLoginContent(navController = navController)
    }

    afternoteComposable<AfternoteRoute.AddSongRoute> {
        AfternoteAddSongRouteContent(
            navController = navController,
            playlistStateHolder = playlistStateHolder,
            afternoteProvider = afternoteProvider
        )
    }
}
