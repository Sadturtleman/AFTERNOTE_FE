package com.kuit.afternote.feature.timeletter.data.di

import com.kuit.afternote.feature.timeletter.data.api.TimeLetterApiService
import com.kuit.afternote.feature.timeletter.data.repository.TimeLetterMediaUploadRepositoryImpl
import com.kuit.afternote.feature.timeletter.data.repository.TimeLetterRepositoryImpl
import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterMediaUploadRepository
import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimeLetterRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindTimeLetterRepository(impl: TimeLetterRepositoryImpl): TimeLetterRepository

    @Binds
    @Singleton
    abstract fun bindTimeLetterMediaUploadRepository(
        impl: TimeLetterMediaUploadRepositoryImpl
    ): TimeLetterMediaUploadRepository

    companion object {
        @Provides
        @Singleton
        fun provideTimeLetterApiService(retrofit: Retrofit): TimeLetterApiService =
            retrofit.create(TimeLetterApiService::class.java)
    }
}
