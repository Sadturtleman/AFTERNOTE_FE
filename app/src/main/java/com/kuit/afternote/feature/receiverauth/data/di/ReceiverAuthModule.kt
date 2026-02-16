package com.kuit.afternote.feature.receiverauth.data.di

import com.kuit.afternote.feature.receiverauth.data.api.ReceiverAuthApiService
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReceiverAuthModule {

    @Binds
    @Singleton
    abstract fun bindReceiverAuthRepository(
        impl: ReceiverAuthRepositoryImpl
    ): ReceiverAuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideReceiverAuthApiService(retrofit: Retrofit): ReceiverAuthApiService =
            retrofit.create(ReceiverAuthApiService::class.java)
    }
}
