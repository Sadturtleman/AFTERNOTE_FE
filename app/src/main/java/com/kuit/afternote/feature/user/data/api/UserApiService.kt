package com.kuit.afternote.feature.user.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.user.data.dto.ReceiverAfterNotesResponseDto
import com.kuit.afternote.feature.user.data.dto.ReceiverDailyQuestionsResponseDto
import com.kuit.afternote.feature.user.data.dto.ReceiverDetailResponseDto
import com.kuit.afternote.feature.user.data.dto.ReceiverTimeLettersResponseDto
import com.kuit.afternote.feature.user.data.dto.ReceiversListResponseDto
import com.kuit.afternote.feature.user.data.dto.RegisterReceiverRequestDto
import com.kuit.afternote.feature.user.data.dto.RegisterReceiverResponseDto
import com.kuit.afternote.feature.user.data.dto.UserPushSettingResponse
import com.kuit.afternote.feature.user.data.dto.UserResponse
import com.kuit.afternote.feature.user.data.dto.UserUpdateProfileRequest
import com.kuit.afternote.feature.user.data.dto.UserUpdatePushSettingRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * User API 서비스. (스웨거 기준)
 *
 * - GET /users/me, PATCH /users/me
 * - GET /users/push-settings, PATCH /users/push-settings
 * - GET /users/receivers, POST /users/receivers, GET /users/receivers/{receiverId}
 * - GET /users/receivers/{receiverId}/daily-questions, time-letters, after-notes
 */
interface UserApiService {
    @GET("users/me")
    suspend fun getMyProfile(
        @Query("userId") userId: Long
    ): ApiResponse<UserResponse?>

    @PATCH("users/me")
    suspend fun updateMyProfile(
        @Query("userId") userId: Long,
        @Body body: UserUpdateProfileRequest
    ): ApiResponse<UserResponse?>

    @GET("users/push-settings")
    suspend fun getMyPushSettings(
        @Query("userId") userId: Long
    ): ApiResponse<UserPushSettingResponse?>

    @PATCH("users/push-settings")
    suspend fun updateMyPushSettings(
        @Query("userId") userId: Long,
        @Body body: UserUpdatePushSettingRequest
    ): ApiResponse<UserPushSettingResponse?>

    @GET("users/receivers")
    suspend fun getReceivers(): ApiResponse<ReceiversListResponseDto?>

    @POST("users/receivers")
    suspend fun registerReceiver(
        @Body body: RegisterReceiverRequestDto
    ): ApiResponse<RegisterReceiverResponseDto?>

    @GET("users/receivers/{receiverId}")
    suspend fun getReceiverDetail(
        @Path("receiverId") receiverId: Long
    ): ApiResponse<ReceiverDetailResponseDto?>

    @GET("users/receivers/{receiverId}/daily-questions")
    suspend fun getReceiverDailyQuestions(
        @Path("receiverId") receiverId: Long
    ): ApiResponse<ReceiverDailyQuestionsResponseDto?>

    @GET("users/receivers/{receiverId}/time-letters")
    suspend fun getReceiverTimeLetters(
        @Path("receiverId") receiverId: Long
    ): ApiResponse<ReceiverTimeLettersResponseDto?>

    @GET("users/receivers/{receiverId}/after-notes")
    suspend fun getReceiverAfterNotes(
        @Path("receiverId") receiverId: Long
    ): ApiResponse<ReceiverAfterNotesResponseDto?>
}
