package com.kuit.afternote.feature.dev.di

import com.kuit.afternote.feature.dev.data.ReleaseLocalPropertiesManager
import com.kuit.afternote.feature.dev.data.ReleaseTestAccountManager
import com.kuit.afternote.feature.dev.domain.LocalPropertiesManager
import com.kuit.afternote.feature.dev.domain.TestAccountManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Release 빌드 전용 Hilt 모듈.
 *
 * ReleaseTestAccountManager와 ReleaseLocalPropertiesManager (No-op)를 각각의 인터페이스에 바인딩합니다.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ReleaseModule {

    @Binds
    @Singleton
    abstract fun bindTestAccountManager(impl: ReleaseTestAccountManager): TestAccountManager

    @Binds
    @Singleton
    abstract fun bindLocalPropertiesManager(impl: ReleaseLocalPropertiesManager): LocalPropertiesManager
}
