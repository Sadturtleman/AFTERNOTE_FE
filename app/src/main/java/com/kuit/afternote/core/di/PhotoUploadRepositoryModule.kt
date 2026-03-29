package com.kuit.afternote.core.di

import com.kuit.afternote.core.data.repository.PhotoUploadRepositoryImpl
import com.kuit.afternote.core.domain.upload.repository.PhotoUploadRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PhotoUploadRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPhotoUploadRepository(impl: PhotoUploadRepositoryImpl): PhotoUploadRepository
}
