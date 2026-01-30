package com.kuit.afternote.feature.dev.di

import com.kuit.afternote.feature.dev.data.DebugLocalPropertiesManager
import com.kuit.afternote.feature.dev.data.DebugTestAccountManager
import com.kuit.afternote.feature.dev.domain.LocalPropertiesManager
import com.kuit.afternote.feature.dev.domain.TestAccountManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Debug 빌드 전용 Hilt 모듈.
 *
 * DebugTestAccountManager와 DebugLocalPropertiesManager를 각각의 인터페이스에 바인딩합니다.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DevModule {
    @Binds
    @Singleton
    abstract fun bindTestAccountManager(impl: DebugTestAccountManager): TestAccountManager

    @Binds
    @Singleton
    abstract fun bindLocalPropertiesManager(impl: DebugLocalPropertiesManager): LocalPropertiesManager
}
