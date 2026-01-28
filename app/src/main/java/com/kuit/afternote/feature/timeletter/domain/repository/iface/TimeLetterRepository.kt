package com.kuit.afternote.feature.timeletter.domain.repository.iface

import com.kuit.afternote.feature.timeletter.data.dto.request.TimeLetterCreateRequest
import com.kuit.afternote.feature.timeletter.data.dto.request.TimeLetterDeleteRequest
import com.kuit.afternote.feature.timeletter.data.dto.request.TimeLetterUpdateRequest
import com.kuit.afternote.feature.timeletter.data.dto.response.TimeLetterBaseResponse
import com.kuit.afternote.feature.timeletter.data.dto.response.TimeLetterListResponse
import com.kuit.afternote.feature.timeletter.data.dto.response.TimeLetterResponse


interface TimeLetterRepository {
    // 타임레터 전체 조회
    suspend fun getEntireTimeLetter(): List<TimeLetterResponse>

    // 타임레터 등록
    suspend fun createTimeLetter(request: TimeLetterCreateRequest): TimeLetterResponse

    // 타임레터 삭제
    suspend fun deleteTimeLetters(request: TimeLetterDeleteRequest): String

    // 타임레터 단일 조회
    suspend fun getTimeLetter(id: Long): TimeLetterResponse

    // 타임레터 수정
    suspend fun updateTimeLetter(id: Long, request: TimeLetterUpdateRequest): TimeLetterResponse

    //임시저장 전체 조회
    suspend fun getDraftTimeLetter(): TimeLetterListResponse

    //임시저장 전체 삭제
    suspend fun deleteDraftTimeLetter(): TimeLetterBaseResponse<Any>

}
