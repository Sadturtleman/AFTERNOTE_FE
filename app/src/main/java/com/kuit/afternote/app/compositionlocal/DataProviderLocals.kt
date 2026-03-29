package com.kuit.afternote.app.compositionlocal

import androidx.compose.runtime.compositionLocalOf
import com.kuit.afternote.feature.afternote.presentation.provider.AfternoteEditDataProvider
import com.kuit.afternote.feature.afternote.presentation.provider.DataProviderSwitch
import com.kuit.afternote.core.domain.provider.ReceiverDataProvider

object DataProviderLocals {
    val LocalDataProviderSwitch = compositionLocalOf<DataProviderSwitch?> { null }

    val LocalAfternoteEditDataProvider =
        compositionLocalOf<AfternoteEditDataProvider> {
            error("No AfternoteEditDataProvider provided")
        }

    val LocalReceiverDataProvider =
        compositionLocalOf<ReceiverDataProvider> {
            error("No ReceiverDataProvider provided")
        }
}
