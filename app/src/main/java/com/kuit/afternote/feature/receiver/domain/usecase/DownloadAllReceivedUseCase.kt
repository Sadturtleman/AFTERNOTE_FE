package com.kuit.afternote.feature.receiver.domain.usecase

import com.kuit.afternote.feature.receiver.domain.entity.DownloadAllResult
import javax.inject.Inject

/**
 * 수신자에게 전달된 타임레터·마인드레코드·애프터노트를 한 번에 조회하는 UseCase.
 *
 * receiverId로 세 API를 순차 호출하여 결과를 [DownloadAllResult]로 묶어 반환합니다.
 * 하나라도 실패하면 [Result.failure]를 반환합니다.
 */
class DownloadAllReceivedUseCase
    @Inject
    constructor(
        private val getReceivedTimeLettersUseCase: GetReceivedTimeLettersUseCase,
        private val getReceivedMindRecordsUseCase: GetReceivedMindRecordsUseCase,
        private val getReceivedAfterNotesUseCase: GetReceivedAfterNotesUseCase
    ) {

    suspend operator fun invoke(receiverId: Long): Result<DownloadAllResult> {
        val timeLettersResult = getReceivedTimeLettersUseCase(receiverId)
        if (timeLettersResult.isFailure) return Result.failure(timeLettersResult.exceptionOrNull()!!)

        val mindRecordsResult = getReceivedMindRecordsUseCase(receiverId)
        if (mindRecordsResult.isFailure) return Result.failure(mindRecordsResult.exceptionOrNull()!!)

        val afternotesResult = getReceivedAfterNotesUseCase(receiverId)
        if (afternotesResult.isFailure) return Result.failure(afternotesResult.exceptionOrNull()!!)

        return Result.success(
            DownloadAllResult(
                timeLetters = timeLettersResult.getOrThrow().items,
                mindRecords = mindRecordsResult.getOrThrow(),
                afternotes = afternotesResult.getOrThrow().items
            )
        )
    }
}
