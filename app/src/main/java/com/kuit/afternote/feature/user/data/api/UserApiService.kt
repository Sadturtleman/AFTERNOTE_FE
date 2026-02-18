package com.kuit.afternote.feature.user.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.user.data.dto.DeliveryConditionRequestDto
import com.kuit.afternote.feature.user.data.dto.DeliveryConditionResponseDto
import com.kuit.afternote.feature.user.data.dto.ReceiverDailyQuestionsResponseDto
import com.kuit.afternote.feature.user.data.dto.ReceiverDetailResponseDto
import com.kuit.afternote.feature.user.data.dto.ReceiverItemDto
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
 * - GET /users/receivers/{receiverId}/daily-questions
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

    /**
     * 푸시 알림 설정 조회.
     * GET /users/push-settings — 로그인한 사용자의 푸시 알림 수신 설정을 불러옵니다.
     *
     * @param userId 사용자 ID (query, required)
     * @return data: timeLetter, mindRecord, afterNote
     */
    @GET("users/push-settings")
    suspend fun getMyPushSettings(
        @Query("userId") userId: Long
    ): ApiResponse<UserPushSettingResponse?>

    @PATCH("users/push-settings")
    suspend fun updateMyPushSettings(
        @Query("userId") userId: Long,
        @Body body: UserUpdatePushSettingRequest
    ): ApiResponse<UserPushSettingResponse?>

    /**
     * 수신인 목록 조회.
     * GET /users/receivers — 로그인한 사용자가 등록한 수신인 목록을 조회합니다.
     *
     * @param userId 사용자 ID (query, required)
     * @return data: array of { receiverId, name, relation }
     */
    @GET("users/receivers")
    suspend fun getReceivers(
        @Query("userId") userId: Long
    ): ApiResponse<List<ReceiverItemDto>?>

    @POST("users/receivers")
    suspend fun registerReceiver(
        @Body body: RegisterReceiverRequestDto
    ): ApiResponse<RegisterReceiverResponseDto?>

    /**
     * 수신인 상세 조회.
     * GET /users/receivers/{receiverId} — 특정 수신인의 상세 정보를 조회합니다.
     *
     * @param userId 사용자 ID (query, required)
     * @param receiverId 수신인 식별자 (path, required)
     * @return data: receiverId, name, relation, phone, email, dailyQuestionCount, timeLetterCount, afterNoteCount
     */
    @GET("users/receivers/{receiverId}")
    suspend fun getReceiverDetail(
        @Query("userId") userId: Long,
        @Path("receiverId") receiverId: Long
    ): ApiResponse<ReceiverDetailResponseDto?>

    /**
     * 수신인별 데일리 질문 답변 목록 조회 (페이지네이션).
     * GET /users/receivers/{receiverId}/daily-questions
     *
     * @param receiverId 수신인 식별자 (path, required)
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지 당 조회 개수
     * @return data: items, hasNext
     */
    @GET("users/receivers/{receiverId}/daily-questions")
    suspend fun getReceiverDailyQuestions(
        @Path("receiverId") receiverId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ApiResponse<ReceiverDailyQuestionsResponseDto?>

    /**
     * 전달 조건 조회.
     * GET /users/delivery-condition — 로그인한 사용자의 전달 조건 설정을 조회합니다.
     *
     * @return data: conditionType, inactivityPeriodDays, specificDate, conditionFulfilled, conditionMet
     */
    @GET("users/delivery-condition")
    suspend fun getDeliveryCondition(): ApiResponse<DeliveryConditionResponseDto?>

    /**
     * 전달 조건 수정.
     * PATCH /users/delivery-condition — 로그인한 사용자의 전달 조건을 설정하거나 변경합니다.
     *
     * @param body conditionType, inactivityPeriodDays, specificDate
     * @return data: 동일한 DeliveryConditionResponseDto
     */
    @PATCH("users/delivery-condition")
    suspend fun updateDeliveryCondition(
        @Body body: DeliveryConditionRequestDto
    ): ApiResponse<DeliveryConditionResponseDto?>
}
