package com.kuit.afternote.data.provider

import com.kuit.afternote.BuildConfig
import com.kuit.afternote.domain.provider.AfternoteEditDataProvider
import com.kuit.afternote.domain.provider.ReceiverDataProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataProviderModule {

    @Provides
    @Singleton
    fun provideAfternoteEditDataProvider(
        real: RealAfternoteEditDataProvider,
        fake: FakeAfternoteEditDataProvider
    ): AfternoteEditDataProvider =
        if (BuildConfig.DEBUG) fake else real

    @Provides
    @Singleton
    fun provideReceiverDataProvider(
        real: RealReceiverDataProvider,
        fake: FakeReceiverDataProvider
    ): ReceiverDataProvider =
        if (BuildConfig.DEBUG) fake else real
}
