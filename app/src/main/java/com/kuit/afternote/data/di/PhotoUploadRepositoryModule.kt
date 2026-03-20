package com.kuit.afternote.data.di

import com.kuit.afternote.data.repositoryimpl.PhotoUploadRepositoryImpl
import com.kuit.afternote.domain.repository.PhotoUploadRepository
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
