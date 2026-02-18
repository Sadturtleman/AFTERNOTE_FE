package com.kuit.afternote.feature.receiverauth.domain.repository.iface

/**
 * 수신자 증빙 서류(사망진단서, 가족관계증명서) S3 업로드용 Domain Repository.
 *
 * Presigned URL 발급 후 S3에 PUT하여 업로드하고, 업로드 완료된 fileUrl을 반환합니다.
 */
interface UploadReceiverDocumentRepository {

    /**
     * 수신자 인증번호와 파일 URI로 증빙 서류를 S3에 업로드합니다.
     *
     * @param authCode 수신자 인증번호 (X-Auth-Code)
     * @param uriString 파일 content URI 문자열
     * @return 성공 시 업로드된 파일의 fileUrl, 실패 시 [Result.failure]
     */
    suspend fun uploadDocument(authCode: String, uriString: String): Result<String>
}
