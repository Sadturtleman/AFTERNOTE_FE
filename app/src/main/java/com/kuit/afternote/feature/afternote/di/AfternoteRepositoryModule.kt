package com.kuit.afternote.feature.afternote.di

import com.kuit.afternote.data.repositoryimpl.MemorialThumbnailUploadRepositoryImpl
import com.kuit.afternote.data.repositoryimpl.MemorialVideoUploadRepositoryImpl
import com.kuit.afternote.domain.repository.MemorialThumbnailUploadRepository
import com.kuit.afternote.domain.repository.MemorialVideoUploadRepository
import com.kuit.afternote.feature.afternote.data.repositoryimpl.AfternoteRepositoryImpl
import com.kuit.afternote.feature.afternote.data.repositoryimpl.MusicSearchRepositoryImpl
import com.kuit.afternote.feature.afternote.domain.repository.AfternoteRepository
import com.kuit.afternote.feature.afternote.domain.repository.MusicSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Afternote Repository + ApiService DI 설정.
 *
 * - AfternoteRepositoryImpl ↔ AfternoteRepository
 * - MusicSearchRepositoryImpl ↔ MusicSearchRepository
 * - Retrofit → AfternoteApiService, MusicApiService
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AfternoteRepositoryModule {
    // 사용 위치가 Hilt가 생성하는 코드 안에만 있기 때문에 IDE에서 'never used'로 표시됩니다.
    @Suppress("unused")
    @Binds
    @Singleton
    abstract fun bindAfternoteRepository(impl: AfternoteRepositoryImpl): AfternoteRepository

    @Suppress("unused")
    @Binds
    @Singleton
    abstract fun bindMusicSearchRepository(impl: MusicSearchRepositoryImpl): MusicSearchRepository

    @Suppress("unused")
    @Binds
    @Singleton
    abstract fun bindMemorialThumbnailUploadRepository(impl: MemorialThumbnailUploadRepositoryImpl): MemorialThumbnailUploadRepository

    @Suppress("unused")
    @Binds
    @Singleton
    abstract fun bindMemorialVideoUploadRepository(impl: MemorialVideoUploadRepositoryImpl): MemorialVideoUploadRepository
}
