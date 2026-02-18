package com.kuit.afternote.feature.user.domain.usecase

import com.kuit.afternote.data.upload.PhotoUploadRepository
import javax.inject.Inject

private const val DIRECTORY_PROFILES = "profiles"

/**
 * 갤러리에서 선택한 프로필 이미지를 업로드하고 사용할 imageUrl을 반환합니다.
 */
class UploadProfileImageUseCase
    @Inject
    constructor(
        private val photoUploadRepository: PhotoUploadRepository
    ) {
    suspend operator fun invoke(uriString: String): Result<String> =
        photoUploadRepository.upload(uriString, DIRECTORY_PROFILES)
}
