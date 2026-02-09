package com.kuit.afternote.feature.dailyrecord.data.di

import com.kuit.afternote.feature.dailyrecord.data.repository.RecordRepository
import com.kuit.afternote.feature.dailyrecord.data.repository.RecordRepositoryImpl
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
