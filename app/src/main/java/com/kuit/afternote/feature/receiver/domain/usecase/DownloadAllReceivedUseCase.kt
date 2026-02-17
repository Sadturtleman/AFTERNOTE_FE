package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.entity.DownloadAllResult
import javax.inject.Inject

/**
 * 수신자 인증번호(authCode)로 타임레터·마인드레코드·애프터노트를 한 번에 조회하는 UseCase.
 *
 * 마스터키(인증번호)로 세 API를 순차 호출하여 결과를 [DownloadAllResult]로 묶어 반환합니다.
 * 하나라도 실패하면 [Result.failure]를 반환합니다.
 */
class DownloadAllReceivedUseCase
    @Inject
    constructor(
        private val getTimeLettersByAuthCodeUseCase: GetTimeLettersByAuthCodeUseCase,
        private val getMindRecordsByAuthCodeUseCase: GetMindRecordsByAuthCodeUseCase,
        private val getAfterNotesByAuthCodeUseCase: GetAfterNotesByAuthCodeUseCase
    ) {

    suspend operator fun invoke(authCode: String): Result<DownloadAllResult> {
        val timeLettersResult = getTimeLettersByAuthCodeUseCase(authCode)
        if (timeLettersResult.isFailure) return Result.failure(timeLettersResult.exceptionOrNull()!!)

        val mindRecordsResult = getMindRecordsByAuthCodeUseCase(authCode)
        if (mindRecordsResult.isFailure) return Result.failure(mindRecordsResult.exceptionOrNull()!!)

        val afternotesResult = getAfterNotesByAuthCodeUseCase(authCode)
        if (afternotesResult.isFailure) return Result.failure(afternotesResult.exceptionOrNull()!!)

        return Result.success(
            DownloadAllResult(
                timeLetters = timeLettersResult.getOrThrow().items,
                mindRecords = mindRecordsResult.getOrThrow().items,
                afternotes = afternotesResult.getOrThrow().items
            )
        )
    }
}
