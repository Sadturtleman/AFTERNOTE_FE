package com.kuit.afternote.feature.afternote.presentation.navgraph

import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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
import com.kuit.afternote.core.ui.screen.AfternoteDetailScreen
import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.feature.afternote.presentation.screen.AddSongCallbacks
import com.kuit.afternote.feature.afternote.presentation.screen.AddSongScreen
import com.kuit.afternote.feature.afternote.presentation.screen.AfternoteEditScreen
import com.kuit.afternote.feature.afternote.domain.model.AfternoteItem
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

private const val TAG_FINGERPRINT = "FingerprintLogin"

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

fun NavGraphBuilder.afternoteNavGraph(
    navController: NavController,
    afternoteItems: List<AfternoteItem>,
    onItemsUpdated: (List<AfternoteItem>) -> Unit,
    playlistStateHolder: MemorialPlaylistStateHolder,
    afternoteProvider: AfternoteEditDataProvider
) {
    afternoteComposable<AfternoteRoute.AfternoteListRoute> {
        val listItems = afternoteItems.ifEmpty {
            AfternoteItemMapper.toAfternoteItemsWithStableIds(afternoteProvider.getAfternoteItemsForDev())
        }
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

    afternoteComposable<AfternoteRoute.DetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<AfternoteRoute.DetailRoute>()
        val listItems = afternoteItems.ifEmpty {
            AfternoteItemMapper.toAfternoteItemsWithStableIds(afternoteProvider.getAfternoteItemsForDev())
        }
        val item = listItems.find { it.id == route.itemId }
        AfternoteDetailScreen(
            serviceName = item?.serviceName ?: "",
            userName = "서영",
            onBackClick = { navController.popBackStack() },
            onEditClick = {
                if (item != null) {
                    navController.navigate(AfternoteRoute.EditRoute(itemId = item.id))
                }
            }
        )
    }

    afternoteComposable<AfternoteRoute.GalleryDetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<AfternoteRoute.GalleryDetailRoute>()
        val listItems = afternoteItems.ifEmpty {
            AfternoteItemMapper.toAfternoteItemsWithStableIds(afternoteProvider.getAfternoteItemsForDev())
        }
        val item = listItems.find { it.id == route.itemId }
        GalleryDetailScreen(
            detailState = GalleryDetailState(
                afternoteEditReceivers = afternoteProvider.getAfternoteEditReceivers(),
                serviceName = item?.serviceName ?: "갤러리",
                userName = "서영"
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

    afternoteComposable<AfternoteRoute.EditRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<AfternoteRoute.EditRoute>()
        val listItems = afternoteItems.ifEmpty {
            AfternoteItemMapper.toAfternoteItemsWithStableIds(afternoteProvider.getAfternoteItemsForDev())
        }
        val initialItem = route.itemId?.let { id -> listItems.find { it.id == id } }

        LaunchedEffect(playlistStateHolder, afternoteProvider) {
            if (playlistStateHolder.songs.isEmpty()) {
                playlistStateHolder.initializeSongs(afternoteProvider.getSongs())
            }
        }

        AfternoteEditScreen(
            onBackClick = { navController.popBackStack() },
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
                navController.navigate(AfternoteRoute.AfternoteListRoute) {
                    popUpTo(AfternoteRoute.AfternoteListRoute) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onNavigateToAddSong = { navController.navigate(AfternoteRoute.MemorialPlaylistRoute) },
            playlistStateHolder = playlistStateHolder,
            initialItem = initialItem
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
        Log.d(TAG_FINGERPRINT, "FingerprintLoginRoute: composable entered")
        val context = LocalContext.current
        Log.d(TAG_FINGERPRINT, "FingerprintLoginRoute: context=${context::class.java.name}")
        val activity = context as? FragmentActivity
        Log.d(
            TAG_FINGERPRINT,
            "FingerprintLoginRoute: activity=${activity?.javaClass?.name ?: "null"}"
        )
        val promptTitle = stringResource(R.string.biometric_prompt_title)
        val promptSubtitle = stringResource(R.string.biometric_prompt_subtitle)
        val notAvailableMessage = stringResource(R.string.biometric_not_available)
        Log.d(TAG_FINGERPRINT, "FingerprintLoginRoute: stringResource done")
        val biometricPrompt =
            remember(activity) {
                try {
                    Log.d(
                        TAG_FINGERPRINT,
                        "FingerprintLoginRoute: building BiometricPrompt, activity=$activity"
                    )
                    activity?.let { fragActivity ->
                        val executor = ContextCompat.getMainExecutor(fragActivity)
                        BiometricPrompt(
                            fragActivity,
                            executor,
                            object : BiometricPrompt.AuthenticationCallback() {
                                override fun onAuthenticationSucceeded(
                                    result: BiometricPrompt.AuthenticationResult
                                ) {
                                    Log.d(TAG_FINGERPRINT, "FingerprintLoginRoute: auth succeeded, popping")
                                    navController.popBackStack()
                                }
                            }
                        ).also {
                            Log.d(TAG_FINGERPRINT, "FingerprintLoginRoute: BiometricPrompt created")
                        }
                    }
                } catch (e: Throwable) {
                    Log.e(TAG_FINGERPRINT, "FingerprintLoginRoute: BiometricPrompt failed", e)
                    null
                }
            }
        val promptInfo =
            remember(promptTitle, promptSubtitle) {
                try {
                    Log.d(TAG_FINGERPRINT, "FingerprintLoginRoute: building PromptInfo")
                    BiometricPrompt.PromptInfo.Builder()
                        .setTitle(promptTitle)
                        .setSubtitle(promptSubtitle)
                        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                        .build()
                        .also { Log.d(TAG_FINGERPRINT, "FingerprintLoginRoute: PromptInfo created") }
                } catch (e: Throwable) {
                    Log.e(TAG_FINGERPRINT, "FingerprintLoginRoute: PromptInfo failed", e)
                    throw e
                }
            }
        Log.d(TAG_FINGERPRINT, "FingerprintLoginRoute: showing FingerprintLoginScreen")
        FingerprintLoginScreen(
            onFingerprintAuthClick = {
                if (activity == null) return@FingerprintLoginScreen
                val biometricManager = BiometricManager.from(context)
                when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
                    BiometricManager.BIOMETRIC_SUCCESS ->
                        biometricPrompt?.authenticate(promptInfo)
                    else ->
                        android.widget.Toast
                            .makeText(context, notAvailableMessage, android.widget.Toast.LENGTH_SHORT)
                            .show()
                }
            }
        )
    }

    afternoteComposable<AfternoteRoute.AddSongRoute> {
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
}
