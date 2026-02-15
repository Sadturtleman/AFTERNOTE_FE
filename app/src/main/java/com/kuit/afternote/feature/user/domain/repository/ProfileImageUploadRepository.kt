package com.kuit.afternote.feature.user.domain.repository

/**
 * 프로필 이미지 업로드 (Presigned URL 획득 후 S3 PUT).
 * 갤러리에서 선택한 이미지 URI 문자열을 받아 업로드 후 사용할 imageUrl을 반환합니다.
 */
fun interface ProfileImageUploadRepository {
    /**
     * [uriString]에 해당하는 이미지를 업로드하고, PATCH /users/me 등에서 사용할 imageUrl을 반환합니다.
     *
     * @param uriString content URI 문자열 (예: content://media/...)
     * @return 성공 시 업로드된 이미지 URL, 실패 시 Result.failure
     */
    suspend fun uploadProfileImage(uriString: String): Result<String>
}
