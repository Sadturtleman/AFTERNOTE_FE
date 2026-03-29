package com.kuit.afternote.feature.afternote.domain.model.received

/** 수신자 인증번호로 조회한 마인드레코드 한 건 (export·다운로드 묶음용). */
data class InboxMindRecord(
    val mindRecordId: Long,
    val sourceType: String?,
    val content: String?,
    val recordDate: String?,
)
