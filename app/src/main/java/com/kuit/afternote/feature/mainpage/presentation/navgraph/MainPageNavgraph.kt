package com.kuit.afternote.feature.mainpage.presentation.navgraph

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
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.screen.AfternoteDetailScreen
import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.feature.mainpage.presentation.screen.AddSongCallbacks
import com.kuit.afternote.feature.mainpage.presentation.screen.AddSongScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteEditScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteItemMapper
import com.kuit.afternote.feature.mainpage.presentation.screen.AfternoteMainRoute
import com.kuit.afternote.feature.mainpage.presentation.screen.FingerprintLoginScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.GalleryDetailCallbacks
import com.kuit.afternote.feature.mainpage.presentation.screen.GalleryDetailScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.GalleryDetailState
import com.kuit.afternote.feature.mainpage.presentation.screen.MemorialPlaylistRouteScreen
import com.kuit.afternote.feature.mainpage.presentation.screen.MemorialPlaylistStateHolder
import com.kuit.afternote.ui.theme.AfternoteTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val TAG_FINGERPRINT = "FingerprintLogin"

/**
 * mainpage feature 전용 라이트 모드 테마 래퍼
 * Preview 함수에서 사용하여 다크 모드를 강제로 비활성화합니다.
 *
 * 사용 예시:
 * ```
 * @Preview(showBackground = true)
 * @Composable
 * private fun MyScreenPreview() {
 *     MainPageLightTheme {
 *         MyScreen()
 *     }
 * }
 * ```
 */
@Composable
fun MainPageLightTheme(content: @Composable () -> Unit) {
    AfternoteTheme(darkTheme = false, content = content)
}

/**
 * mainpage feature 전용 composable 래퍼
 * 내부적으로 라이트 모드를 강제 적용하여 다크모드를 비활성화합니다.
 */
inline fun <reified T : Any> NavGraphBuilder.mainPageComposable(noinline content: @Composable (NavBackStackEntry) -> Unit) {
    composable<T> { backStackEntry ->
        AfternoteTheme(darkTheme = false) {
            content(backStackEntry)
        }
    }
}

fun NavGraphBuilder.mainPageNavGraph(
    navController: NavController,
    afternoteItems: List<Pair<String, String>>,
    onItemsUpdated: (List<Pair<String, String>>) -> Unit,
    playlistStateHolder: MemorialPlaylistStateHolder,
    afternoteProvider: AfternoteEditDataProvider
) {
    mainPageComposable<MainPageRoute.MainRoute> {
        val mainListItems =
            afternoteItems.ifEmpty { afternoteProvider.getMainPageItemsForDev() }
        AfternoteMainRoute(
            onNavigateToDetail = { navController.navigate(MainPageRoute.DetailRoute) },
            onNavigateToGalleryDetail = { navController.navigate(MainPageRoute.GalleryDetailRoute) },
            onNavigateToAdd = { navController.navigate(MainPageRoute.EditRoute) },
            initialItems = AfternoteItemMapper.toAfternoteItems(mainListItems)
        )
    }

    mainPageComposable<MainPageRoute.DetailRoute> {
        AfternoteDetailScreen(
            onBackClick = { navController.popBackStack() },
            onEditClick = { navController.navigate(MainPageRoute.EditRoute) }
        )
    }

    mainPageComposable<MainPageRoute.GalleryDetailRoute> {
        GalleryDetailScreen(
            detailState = GalleryDetailState(
                mainPageEditReceivers = afternoteProvider.getMainPageEditReceivers()
            ),
            callbacks = GalleryDetailCallbacks(
                onBackClick = { navController.popBackStack() },
                onEditClick = { navController.navigate(MainPageRoute.EditRoute) }
            )
        )
    }

    mainPageComposable<MainPageRoute.EditRoute> {
        LaunchedEffect(playlistStateHolder, afternoteProvider) {
            if (playlistStateHolder.songs.isEmpty()) {
                playlistStateHolder.initializeSongs(afternoteProvider.getSongs())
            }
        }

        AfternoteEditScreen(
            onBackClick = { navController.popBackStack() },
            onRegisterClick = { selectedService ->
                // 현재 날짜를 포맷팅
                val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                // 목록에 추가 (레거시 형식 유지)
                val newItem = selectedService to currentDate
                val updatedItems = afternoteItems + newItem
                onItemsUpdated(updatedItems)

                // 메인 화면으로 이동
                navController.navigate(MainPageRoute.MainRoute) {
                    popUpTo(MainPageRoute.MainRoute) { inclusive = true }
                    launchSingleTop = true
                }
            },
            onNavigateToAddSong = { navController.navigate(MainPageRoute.MemorialPlaylistRoute) },
            playlistStateHolder = playlistStateHolder
        )
    }

    mainPageComposable<MainPageRoute.MemorialPlaylistRoute> {
        MemorialPlaylistRouteScreen(
            playlistStateHolder = playlistStateHolder,
            onBackClick = { navController.popBackStack() },
            onNavigateToAddSongScreen = { navController.navigate(MainPageRoute.AddSongRoute) }
        )
    }

    mainPageComposable<MainPageRoute.FingerprintLoginRoute> {
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

    mainPageComposable<MainPageRoute.AddSongRoute> {
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
