package com.kuit.afternote.feature.timeletter.data.api

import com.kuit.afternote.core.data.dto.response.BaseResponse
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterCreateRequest
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterDeleteRequest
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterListResponse
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterResponse
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterUpdateRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * TimeLetter API 서비스. (스웨거 기준)
 *
 * - GET /time-letters, POST /time-letters
 * - GET /time-letters/{timeLetterId}, PATCH /time-letters/{timeLetterId}
 * - POST /time-letters/delete
 * - GET /time-letters/temporary, DELETE /time-letters/temporary
 */
interface TimeLetterApiService {
    @GET("time-letters")
    suspend fun getTimeLetters(): BaseResponse<TimeLetterListResponse?>

    @POST("time-letters")
    suspend fun createTimeLetter(
        @Body body: TimeLetterCreateRequest,
    ): BaseResponse<TimeLetterResponse?>

    @GET("time-letters/{timeLetterId}")
    suspend fun getTimeLetter(
        @Path("timeLetterId") timeLetterId: Long,
    ): BaseResponse<TimeLetterResponse?>

    @PATCH("time-letters/{timeLetterId}")
    suspend fun updateTimeLetter(
        @Path("timeLetterId") timeLetterId: Long,
        @Body body: TimeLetterUpdateRequest,
    ): BaseResponse<TimeLetterResponse?>

    @POST("time-letters/delete")
    suspend fun deleteTimeLetters(
        @Body body: TimeLetterDeleteRequest,
    ): BaseResponse<Unit?>

    @GET("time-letters/temporary")
    suspend fun getTemporaryTimeLetters(): BaseResponse<TimeLetterListResponse?>

    @DELETE("time-letters/temporary")
    suspend fun deleteAllTemporary(): BaseResponse<Unit?>
}
