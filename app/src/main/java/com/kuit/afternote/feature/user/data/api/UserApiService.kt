package com.kuit.afternote.feature.user.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.user.data.dto.UserPushSettingResponse
import com.kuit.afternote.feature.user.data.dto.UserResponse
import com.kuit.afternote.feature.user.data.dto.UserUpdateProfileRequest
import com.kuit.afternote.feature.user.data.dto.UserUpdatePushSettingRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

/**
 * User API 서비스. (스웨거 기준)
 *
 * - GET /users/me, PATCH /users/me
 * - GET /users/push-settings, PATCH /users/push-settings
 */
interface UserApiService {

    @GET("users/me")
    suspend fun getMyProfile(@Query("userId") userId: Long): ApiResponse<UserResponse?>

    @PATCH("users/me")
    suspend fun updateMyProfile(
        @Query("userId") userId: Long,
        @Body body: UserUpdateProfileRequest
    ): ApiResponse<UserResponse?>

    @GET("users/push-settings")
    suspend fun getMyPushSettings(@Query("userId") userId: Long): ApiResponse<UserPushSettingResponse?>

    @PATCH("users/push-settings")
    suspend fun updateMyPushSettings(
        @Query("userId") userId: Long,
        @Body body: UserUpdatePushSettingRequest
    ): ApiResponse<UserPushSettingResponse?>
}
