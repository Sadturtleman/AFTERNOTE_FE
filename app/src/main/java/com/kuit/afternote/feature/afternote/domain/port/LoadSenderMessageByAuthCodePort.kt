package com.kuit.afternote.feature.afternote.domain.port

fun interface LoadSenderMessageByAuthCodePort {
    suspend operator fun invoke(authCode: String): Result<String?>
}
