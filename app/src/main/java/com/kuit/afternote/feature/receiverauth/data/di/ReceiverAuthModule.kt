package com.kuit.afternote.feature.receiverauth.data.di

import com.kuit.afternote.feature.receiver.domain.repository.iface.GetAfternoteDetailByAuthCodeRepository
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetAfterNotesByAuthCodeRepository
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetSenderMessageRepository
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetMindRecordDetailByAuthCodeRepository
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetMindRecordsByAuthCodeRepository
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetTimeLetterDetailByAuthCodeRepository
import com.kuit.afternote.feature.receiver.domain.repository.iface.GetTimeLettersByAuthCodeRepository
import com.kuit.afternote.feature.receiverauth.data.api.ReceiverAuthApiService
import com.kuit.afternote.feature.receiverauth.data.repository.impl.GetDeliveryVerificationStatusRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthAfternoteDetailRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthAfternoteRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthMessageRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthMindRecordDetailRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthMindRecordRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthTimeLetterDetailRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverAuthTimeLetterRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.ReceiverDocumentUploadRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.impl.SubmitDeliveryVerificationRepositoryImpl
import com.kuit.afternote.feature.receiverauth.data.repository.iface.ReceiverAuthRepository
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.GetDeliveryVerificationStatusRepository
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.ReceiverAuthRepository as DomainReceiverAuthRepository
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.SubmitDeliveryVerificationRepository
import com.kuit.afternote.feature.receiverauth.domain.repository.iface.UploadReceiverDocumentRepository
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
    abstract fun bindGetTimeLetterDetailByAuthCodeRepository(
        impl: ReceiverAuthTimeLetterDetailRepositoryImpl
    ): GetTimeLetterDetailByAuthCodeRepository

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
    abstract fun bindGetMindRecordDetailByAuthCodeRepository(
        impl: ReceiverAuthMindRecordDetailRepositoryImpl
    ): GetMindRecordDetailByAuthCodeRepository

    @Binds
    @Singleton
    abstract fun bindGetAfterNotesByAuthCodeRepository(
        impl: ReceiverAuthAfternoteRepositoryImpl
    ): GetAfterNotesByAuthCodeRepository

    @Binds
    @Singleton
    abstract fun bindGetAfternoteDetailByAuthCodeRepository(
        impl: ReceiverAuthAfternoteDetailRepositoryImpl
    ): GetAfternoteDetailByAuthCodeRepository

    @Binds
    @Singleton
    abstract fun bindGetSenderMessageRepository(
        impl: ReceiverAuthMessageRepositoryImpl
    ): GetSenderMessageRepository

    @Binds
    @Singleton
    abstract fun bindUploadReceiverDocumentRepository(
        impl: ReceiverDocumentUploadRepositoryImpl
    ): UploadReceiverDocumentRepository

    @Binds
    @Singleton
    abstract fun bindSubmitDeliveryVerificationRepository(
        impl: SubmitDeliveryVerificationRepositoryImpl
    ): SubmitDeliveryVerificationRepository

    @Binds
    @Singleton
    abstract fun bindGetDeliveryVerificationStatusRepository(
        impl: GetDeliveryVerificationStatusRepositoryImpl
    ): GetDeliveryVerificationStatusRepository

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
