package com.kuit.afternote.app.di

import com.kuit.afternote.feature.receiverauth.session.ReceiverAuthSessionHolder
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
fun interface ReceiverAuthSessionEntryPoint {
    fun receiverAuthSessionHolder(): ReceiverAuthSessionHolder
}
