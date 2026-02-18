package com.kuit.afternote.feature.timeletter.domain.repository

/**
 * 타임레터 첨부 이미지 업로드 (POST /files/presigned-url 후 S3 PUT).
 *
 * content URI 문자열을 받아 업로드 후 사용할 fileUrl을 반환합니다.
 */
fun interface TimeLetterMediaUploadRepository {

    /**
     * [uriString]에 해당하는 이미지를 S3에 업로드하고, 타임레터 mediaList에 넣을 fileUrl을 반환합니다.
     *
     * @param uriString content URI 문자열 (예: content://media/...)
     * @return 성공 시 업로드된 이미지 URL, 실패 시 Result.failure
     */
    suspend fun uploadImage(uriString: String): Result<String>
}
