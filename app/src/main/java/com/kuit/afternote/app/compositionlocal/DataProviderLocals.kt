package com.kuit.afternote.app.compositionlocal

import androidx.compose.runtime.compositionLocalOf
import com.kuit.afternote.data.provider.DataProviderSwitch
import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.domain.provider.ReceiverDataProvider

object DataProviderLocals {
    val LocalDataProviderSwitch = compositionLocalOf<DataProviderSwitch?> { null }

    val LocalAfternoteEditDataProvider = compositionLocalOf<AfternoteEditDataProvider> {
        error("No AfternoteEditDataProvider provided")
    }

    val LocalReceiverDataProvider = compositionLocalOf<ReceiverDataProvider> {
        error("No ReceiverDataProvider provided")
    }
}
