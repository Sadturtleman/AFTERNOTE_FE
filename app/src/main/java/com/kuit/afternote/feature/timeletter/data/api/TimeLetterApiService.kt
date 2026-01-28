package com.kuit.afternote.feature.timeletter.data.api

import com.kuit.afternote.feature.timeletter.data.dto.request.TimeLetterCreateRequest
import com.kuit.afternote.feature.timeletter.data.dto.request.TimeLetterDeleteRequest
import com.kuit.afternote.feature.timeletter.data.dto.request.TimeLetterUpdateRequest
import com.kuit.afternote.feature.timeletter.data.dto.response.TimeLetterBaseResponse
import com.kuit.afternote.feature.timeletter.data.dto.response.TimeLetterListResponse
import com.kuit.afternote.feature.timeletter.data.dto.response.TimeLetterResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TimeLetterApiService {

    //타임레터 전체 조회
    @GET("time-letters")
    suspend fun getEntireTimeLetter(): TimeLetterBaseResponse<TimeLetterListResponse>

    //타임레터 등록
    @POST("time-letters")
    suspend fun createTimeLetter(
        @Body request: TimeLetterCreateRequest
    ): TimeLetterBaseResponse<TimeLetterResponse>

    //타임레터 삭제
    @POST("time-letters/delete")
    suspend fun deleteTimeLetters(
        @Body request: TimeLetterDeleteRequest
    ): TimeLetterBaseResponse<String>

    //타임레터 단일 조회
    @GET("time-letters/{timeLetterId}")
    suspend fun getTimeLetter(
        @Path("timeLetterId") id: Long
    ): TimeLetterBaseResponse<TimeLetterResponse>

    //타임레터 수정
    @PATCH("time-letters/{timeLetterId}")
    suspend fun updateTimeLetter(
        @Path("timeLetterId") id: Long,
        @Body request: TimeLetterUpdateRequest
    ): TimeLetterBaseResponse<TimeLetterResponse>

    //임시저장 전체 조회
    @GET("time-letters/temporary")
    suspend fun getDraftTimeLetter(): TimeLetterBaseResponse<TimeLetterListResponse>

    //임시저장 삭제
    @DELETE("time-letters/temporary")
    suspend fun deleteDraftTimeLetter(): TimeLetterBaseResponse<Any>
}
