package com.kuit.afternote.feature.afternote.domain.port

import com.kuit.afternote.feature.afternote.domain.model.AuthorReceiverDirectoryEntry

/** 작성자 기준 수신자 목록 조회 (GET /users/receivers). */
fun interface AuthorReceiversDirectoryPort {
    suspend operator fun invoke(userId: Long): Result<List<AuthorReceiverDirectoryEntry>>
}
