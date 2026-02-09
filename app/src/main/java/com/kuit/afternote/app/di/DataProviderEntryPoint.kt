package com.kuit.afternote.app.di

import com.kuit.afternote.data.provider.DataProviderSwitch
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
fun interface DataProviderEntryPoint {
    fun dataProviderSwitch(): DataProviderSwitch
}
