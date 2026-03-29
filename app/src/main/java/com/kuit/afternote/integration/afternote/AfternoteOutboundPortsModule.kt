package com.kuit.afternote.integration.afternote

import com.kuit.afternote.feature.afternote.domain.port.AuthorReceiversDirectoryPort
import com.kuit.afternote.feature.afternote.domain.port.CurrentAuthorUserIdPort
import com.kuit.afternote.feature.afternote.domain.port.LoadMindRecordsByAuthCodePort
import com.kuit.afternote.feature.afternote.domain.port.LoadSenderMessageByAuthCodePort
import com.kuit.afternote.feature.afternote.domain.port.LoadTimeLettersByAuthCodePort
import com.kuit.afternote.feature.afternote.domain.port.ReceiverAuthCodeProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AfternoteOutboundPortsModule {
    @Binds
    @Singleton
    abstract fun bindCurrentAuthorUserIdPort(impl: CurrentAuthorUserIdAdapter): CurrentAuthorUserIdPort

    @Binds
    @Singleton
    abstract fun bindAuthorReceiversDirectoryPort(impl: AuthorReceiversDirectoryAdapter): AuthorReceiversDirectoryPort

    @Binds
    @Singleton
    abstract fun bindLoadTimeLettersByAuthCodePort(impl: LoadTimeLettersByAuthCodeAdapter): LoadTimeLettersByAuthCodePort

    @Binds
    @Singleton
    abstract fun bindLoadMindRecordsByAuthCodePort(impl: LoadMindRecordsByAuthCodeAdapter): LoadMindRecordsByAuthCodePort

    @Binds
    @Singleton
    abstract fun bindLoadSenderMessageByAuthCodePort(impl: LoadSenderMessageByAuthCodeAdapter): LoadSenderMessageByAuthCodePort

    @Binds
    @Singleton
    abstract fun bindReceiverAuthCodeProvider(impl: ReceiverAuthCodeFromSessionAdapter): ReceiverAuthCodeProvider
}
