package com.kuit.afternote.feature.timeletter.domain.usecase

import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterMediaUploadRepository
import javax.inject.Inject

/**
 * 타임레터 첨부 오디오 1개를 presigned URL로 S3에 업로드하고 fileUrl을 반환합니다.
 *
 * @param uriString content URI 문자열
 * @return 성공 시 fileUrl, 실패 시 Result.failure
 */
class UploadTimeLetterAudioUseCase
    @Inject
    constructor(
        private val repository: TimeLetterMediaUploadRepository
    ) {
        suspend operator fun invoke(uriString: String): Result<String> =
            repository.uploadAudio(uriString)
    }
