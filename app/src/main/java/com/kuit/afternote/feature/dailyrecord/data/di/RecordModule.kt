package com.kuit.afternote.feature.dailyrecord.data.di

import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepository
import com.kuit.afternote.feature.dailyrecord.data.repository.MindRecordRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecordModule {
    @Binds
    @Singleton
    abstract fun bindRecordRepository(impl: MindRecordRepositoryImpl): MindRecordRepository
}
