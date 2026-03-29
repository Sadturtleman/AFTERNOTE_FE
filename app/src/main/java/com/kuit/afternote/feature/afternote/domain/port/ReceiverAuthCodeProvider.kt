package com.kuit.afternote.feature.afternote.domain.port

/** 수신자 플로우에서 사용 중인 인증번호(X-Auth-Code). 없으면 null. */
fun interface ReceiverAuthCodeProvider {
    fun currentAuthCode(): String?
}
