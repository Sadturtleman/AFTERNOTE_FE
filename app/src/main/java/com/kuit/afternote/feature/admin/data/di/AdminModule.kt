package com.kuit.afternote.feature.admin.data.di

import com.kuit.afternote.feature.admin.data.api.AdminApiService
import com.kuit.afternote.feature.admin.data.repository.iface.AdminRepository
import com.kuit.afternote.feature.admin.data.repository.impl.AdminRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AdminModule {

    @Binds
    @Singleton
    abstract fun bindAdminRepository(impl: AdminRepositoryImpl): AdminRepository

    companion object {
        @Provides
        @Singleton
        fun provideAdminApiService(retrofit: Retrofit): AdminApiService =
            retrofit.create(AdminApiService::class.java)
    }
}
