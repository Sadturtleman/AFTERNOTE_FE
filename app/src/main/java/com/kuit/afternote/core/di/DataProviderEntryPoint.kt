package com.kuit.afternote.core.di

import com.kuit.afternote.feature.afternote.presentation.author.edit.ui.provider.DataProviderSwitch
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
fun interface DataProviderEntryPoint {
    fun dataProviderSwitch(): DataProviderSwitch
}
