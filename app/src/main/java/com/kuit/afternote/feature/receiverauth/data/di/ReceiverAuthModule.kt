package com.kuit.afternote.feature.receiverauth.data.di

import com.kuit.afternote.feature.receiver.domain.repository.iface.GetAfterNotesByAuthCodeRepository
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetMindRecordsByAuthCodeRepository
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetTimeLettersByAuthCodeRepository
import com.kuit.afternote.feature.receiverauth.data.api.ReceiverAuthApiService
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthAfternoteRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthMindRecordRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthTimeLetterRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.ReceiverAuthRepository as DomainReceiverAuthRepository
import com.kuit.afternote.feature.receiverauth.session.ReceiverAuthSessionHolder
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
    abstract fun bindDomainReceiverAuthRepository(
        impl: ReceiverAuthRepositoryImpl
    ): DomainReceiverAuthRepository

    @Binds
    @Singleton
    abstract fun bindReceiverAuthRepository(
        impl: ReceiverAuthRepositoryImpl
    ): ReceiverAuthRepository

    @Binds
    @Singleton
    abstract fun bindGetTimeLettersByAuthCodeRepository(
        impl: ReceiverAuthTimeLetterRepositoryImpl
    ): GetTimeLettersByAuthCodeRepository

    @Binds
    @Singleton
    abstract fun bindGetMindRecordsByAuthCodeRepository(
        impl: ReceiverAuthMindRecordRepositoryImpl
    ): GetMindRecordsByAuthCodeRepository

    @Binds
    @Singleton
    abstract fun bindGetAfterNotesByAuthCodeRepository(
        impl: ReceiverAuthAfternoteRepositoryImpl
    ): GetAfterNotesByAuthCodeRepository

    companion object {
        @Provides
        @Singleton
        fun provideReceiverAuthApiService(retrofit: Retrofit): ReceiverAuthApiService =
            retrofit.create(ReceiverAuthApiService::class.java)

        @Provides
        @Singleton
        fun provideReceiverAuthSessionHolder(): ReceiverAuthSessionHolder =
            ReceiverAuthSessionHolder()
    }
}
