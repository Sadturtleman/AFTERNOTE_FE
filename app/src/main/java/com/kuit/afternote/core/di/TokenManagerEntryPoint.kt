package com.kuit.afternote.core.di
import com.kuit.afternote.core.data.service.TokenManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
fun interface TokenManagerEntryPoint {
    fun tokenManager(): TokenManager
}
