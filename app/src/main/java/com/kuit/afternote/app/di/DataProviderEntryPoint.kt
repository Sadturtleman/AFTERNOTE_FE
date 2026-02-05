package com.kuit.afternote.app.di

import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.domain.provider.ReceiverDataProvider
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DataProviderEntryPoint {
    fun afternoteEditDataProvider(): AfternoteEditDataProvider
    fun receiverDataProvider(): ReceiverDataProvider
}
