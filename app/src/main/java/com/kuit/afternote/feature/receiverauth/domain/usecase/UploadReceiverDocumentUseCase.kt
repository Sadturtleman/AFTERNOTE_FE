package com.kuit.afternote.feature.receiverauth.domain.usecase

import com.kuit.afternote.feature.receiverauth.domain.repository.iface.UploadReceiverDocumentRepository
import javax.inject.Inject

/**
 * 수신자 증빙 서류를 S3에 업로드하는 UseCase.
 *
 * Presigned URL 발급 후 S3 PUT으로 업로드하고, 업로드된 fileUrl을 반환합니다.
 */
class UploadReceiverDocumentUseCase
    @Inject
    constructor(
        private val uploadReceiverDocumentRepository: UploadReceiverDocumentRepository
    ) {

    /**
     * 증빙 서류 파일을 업로드합니다.
     *
     * @param authCode 수신자 인증번호 (X-Auth-Code)
     * @param uriString 파일 content URI 문자열
     * @return 성공 시 fileUrl, 실패 시 [Result.failure]
     */
    suspend operator fun invoke(authCode: String, uriString: String): Result<String> =
        uploadReceiverDocumentRepository.uploadDocument(authCode, uriString)
}
