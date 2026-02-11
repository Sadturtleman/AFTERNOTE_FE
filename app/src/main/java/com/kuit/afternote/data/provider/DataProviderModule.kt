package com.kuit.afternote.data.provider

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@Suppress("UNUSED") // Used by Hilt via @InstallIn
object DataProviderModule
