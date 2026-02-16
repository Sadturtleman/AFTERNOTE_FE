package com.kuit.afternote.feature.afternote.data.di

import com.kuit.afternote.feature.afternote.data.api.AfternoteApiService
import com.kuit.afternote.feature.afternote.data.api.MusicApiService
import com.kuit.afternote.feature.afternote.data.repository.AfternoteRepositoryImpl
import com.kuit.afternote.feature.afternote.data.repository.MusicSearchRepositoryImpl
import com.kuit.afternote.feature.afternote.domain.repository.iface.AfternoteRepository
import com.kuit.afternote.feature.afternote.domain.repository.iface.MusicSearchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
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
    abstract fun bindAfternoteRepository(
        impl: AfternoteRepositoryImpl
    ): AfternoteRepository

    @Suppress("unused")
    @Binds
    @Singleton
    abstract fun bindMusicSearchRepository(
        impl: MusicSearchRepositoryImpl
    ): MusicSearchRepository

    companion object {
        @Provides
        @Singleton
        fun provideAfternoteApiService(retrofit: Retrofit): AfternoteApiService =
            retrofit.create(AfternoteApiService::class.java)

        @Provides
        @Singleton
        fun provideMusicApiService(retrofit: Retrofit): MusicApiService =
            retrofit.create(MusicApiService::class.java)
    }
}

