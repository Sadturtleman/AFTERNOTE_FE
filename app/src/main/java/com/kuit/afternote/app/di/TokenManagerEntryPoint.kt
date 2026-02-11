package com.kuit.afternote.app.di

import com.kuit.afternote.data.local.TokenManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
fun interface TokenManagerEntryPoint {
    fun tokenManager(): TokenManager
}
