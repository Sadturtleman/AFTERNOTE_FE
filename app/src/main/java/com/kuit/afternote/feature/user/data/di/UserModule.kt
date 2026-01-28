package com.kuit.afternote.feature.user.data.di

import com.kuit.afternote.feature.user.data.api.UserApiService
import com.kuit.afternote.feature.user.data.repository.UserRepositoryImpl
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {
    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    companion object {
        @Provides
        @Singleton
        fun provideUserApiService(retrofit: Retrofit): UserApiService = retrofit.create(UserApiService::class.java)
    }
}
