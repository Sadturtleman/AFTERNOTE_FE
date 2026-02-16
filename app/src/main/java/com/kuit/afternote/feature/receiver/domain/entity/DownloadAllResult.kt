package com.kuit.afternote.feature.receiver.domain.entity

/**
 * 수신자에게 전달된 타임레터·마인드레코드·애프터노트 전체 조회 결과.
 *
 * DownloadAllReceivedUseCase의 반환 타입으로 사용됩니다.
 */
data class DownloadAllResult(
    val timeLetters: List<ReceivedTimeLetter>,
    val mindRecords: List<ReceivedMindRecord>,
    val afternotes: List<ReceivedAfternote>
)
