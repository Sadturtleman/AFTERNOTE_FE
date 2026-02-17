package com.kuit.afternote.feature.receiver.data.di

import com.kuit.afternote.feature.receiver.data.api.ReceivedApiService
import com.kuit.afternote.feature.receiver.data.repository.iface.ReceivedRepository
import com.kuit.afternote.feature.receiver.data.repository.impl.ExportReceivedRepositoryImpl
import com.kuit.afternote.feature.receiver.data.repository.impl.ReceivedRepositoryImpl
import com.kuit.afternote.feature.receiver.data.repository.impl.ReceivedTimeLetterRepositoryStub
import com.kuit.afternote.feature.receiver.domain.repository.iface.ExportReceivedRepository
import com.kuit.afternote.feature.receiver.domain.repository.iface.ReceivedTimeLetterRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReceiverModule {

    @Binds
    @Singleton
    abstract fun bindReceivedRepository(impl: ReceivedRepositoryImpl): ReceivedRepository

    @Binds
    @Singleton
    abstract fun bindReceivedTimeLetterRepository(
        impl: ReceivedTimeLetterRepositoryStub
    ): ReceivedTimeLetterRepository

    @Binds
    @Singleton
    abstract fun bindExportReceivedRepository(
        impl: ExportReceivedRepositoryImpl
    ): ExportReceivedRepository

    companion object {
        @Provides
        @Singleton
        fun provideReceivedApiService(retrofit: Retrofit): ReceivedApiService =
            retrofit.create(ReceivedApiService::class.java)
    }
}
