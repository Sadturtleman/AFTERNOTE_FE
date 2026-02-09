package com.kuit.afternote.feature.afternote.data.di

import com.kuit.afternote.feature.afternote.data.api.AfternoteApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AfternoteModule {

    @Provides
    @Singleton
    fun provideAfternoteApiService(retrofit: Retrofit): AfternoteApiService =
        retrofit.create(AfternoteApiService::class.java)
}
