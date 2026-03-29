package com.kuit.afternote.feature.afternote.data.di

import com.kuit.afternote.feature.afternote.data.api.AfternotePresignedUrlApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AfternoteNetworkModule {
    @Provides
    @Singleton
    fun provideAfternotePresignedUrlApi(retrofit: Retrofit): AfternotePresignedUrlApi =
        retrofit.create(AfternotePresignedUrlApi::class.java)
}
