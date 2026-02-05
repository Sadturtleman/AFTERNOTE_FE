package com.kuit.afternote.data.provider

import android.content.Context
import androidx.core.content.edit
import com.kuit.afternote.BuildConfig
import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.domain.provider.ReceiverDataProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val PREFS_NAME = "data_provider_prefs"
private const val KEY_USE_FAKE = "use_fake_data"

/**
 * Holds the runtime choice between Fake (mock) and Real data providers.
 * Persists the choice so it survives process death. Defaults to [BuildConfig.DEBUG] on first run.
 */
@Singleton
class DataProviderSwitch @Inject constructor(
    private val fakeAfternote: FakeAfternoteEditDataProvider,
    private val realAfternote: RealAfternoteEditDataProvider,
    private val fakeReceiver: FakeReceiverDataProvider,
    private val realReceiver: RealReceiverDataProvider,
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _useFakeState = MutableStateFlow(
        prefs.getBoolean(KEY_USE_FAKE, BuildConfig.DEBUG)
    )
    val useFakeState: StateFlow<Boolean> = _useFakeState.asStateFlow()

    /**
     * Initial value for useFake (from persisted prefs). Use this for Compose initialValue
     * instead of reading [useFakeState].value inside composition.
     */
    fun getInitialUseFake(): Boolean = prefs.getBoolean(KEY_USE_FAKE, BuildConfig.DEBUG)

    /**
     * Sets whether to use Fake (mock) or Real data. Persists the value.
     */
    fun setUseFake(useFake: Boolean) {
        prefs.edit { putBoolean(KEY_USE_FAKE, useFake) }
        _useFakeState.value = useFake
    }

    fun getCurrentAfternoteEditDataProvider(): AfternoteEditDataProvider =
        if (_useFakeState.value) fakeAfternote else realAfternote

    fun getCurrentReceiverDataProvider(): ReceiverDataProvider =
        if (_useFakeState.value) fakeReceiver else realReceiver
}
