package com.kuit.afternote.feature.dailyrecord.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RecordModule {
    @Binds
    abstract fun bindRecordRepository(impl: RecordRepositoryImpl): RecordRepository
}
