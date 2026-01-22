package com.kuit.afternote.feature.timeletter.data.di

import com.kuit.afternote.feature.timeletter.data.repository.DraftLetterRepositoryImpl
import com.kuit.afternote.feature.timeletter.domain.repository.iface.DraftLetterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TimeLetterRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDraftLetterRepository(
        impl: DraftLetterRepositoryImpl
    ): DraftLetterRepository
}
