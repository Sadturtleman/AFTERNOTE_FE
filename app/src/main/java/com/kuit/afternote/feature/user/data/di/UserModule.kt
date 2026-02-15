package com.kuit.afternote.feature.user.data.di

import com.kuit.afternote.feature.user.data.api.ImageApiService
import com.kuit.afternote.feature.user.data.api.UserApiService
import com.kuit.afternote.feature.user.data.repository.ProfileImageUploadRepositoryImpl
import com.kuit.afternote.feature.user.data.repository.UserRepositoryImpl
import com.kuit.afternote.feature.user.domain.repository.ProfileImageUploadRepository
import com.kuit.afternote.feature.user.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {
    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindProfileImageUploadRepository(
        impl: ProfileImageUploadRepositoryImpl
    ): ProfileImageUploadRepository

    companion object {
        @Provides
        @Singleton
        @Named("IoDispatcher")
        fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Singleton
        fun provideUserApiService(retrofit: Retrofit): UserApiService =
            retrofit.create(UserApiService::class.java)

        @Provides
        @Singleton
        fun provideImageApiService(retrofit: Retrofit): ImageApiService =
            retrofit.create(ImageApiService::class.java)
    }
}
