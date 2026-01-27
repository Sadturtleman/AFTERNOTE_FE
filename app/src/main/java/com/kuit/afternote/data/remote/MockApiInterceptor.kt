package com.kuit.afternote.data.remote

import com.kuit.afternote.BuildConfig
import com.kuit.afternote.feature.auth.data.dto.LoginData
import com.kuit.afternote.feature.auth.data.dto.ReissueData
import com.kuit.afternote.feature.auth.data.dto.SignUpData
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailData
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterListResponse
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterMediaResponse
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterMediaType
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterResponse
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterStatus
import com.kuit.afternote.feature.user.data.dto.UserPushSettingResponse
import com.kuit.afternote.feature.user.data.dto.UserResponse
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Mock API Interceptor.
 * 명세서 업데이트 전 테스트를 위해 Mock 응답을 반환합니다.
 *
 * BuildConfig.USE_MOCK_API가 true일 때만 동작합니다.
 */
class MockApiInterceptor(
    private val json: Json
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!BuildConfig.USE_MOCK_API) {
            return chain.proceed(chain.request())
        }

        val request = chain.request()
        val url = request.url.toString()
        val path = request.url.encodedPath
        val method = request.method

        val mockResponse = when {
            // Auth API Mock 응답
            path == "/auth/email/send" && method == "POST" -> createSuccessResponse<Unit?>(
                request = request,
                data = null,
                message = "이메일 인증번호가 발송되었습니다"
            )

            path == "/auth/email/verify" && method == "POST" -> createSuccessResponse(
                request = request,
                data = VerifyEmailData(isVerified = true),
                message = "이메일 인증이 완료되었습니다"
            )

            path == "/auth/sign-up" && method == "POST" -> createSuccessResponse(
                request = request,
                data = SignUpData(userId = 1L, email = "test@example.com"),
                message = "회원가입이 완료되었습니다"
            )

            path == "/auth/login" && method == "POST" -> createSuccessResponse(
                request = request,
                data = LoginData(
                    accessToken = "mock_access_token_12345",
                    refreshToken = "mock_refresh_token_12345"
                ),
                message = "로그인에 성공했습니다"
            )

            path == "/auth/reissue" && method == "POST" -> createSuccessResponse(
                request = request,
                data = ReissueData(
                    accessToken = "mock_new_access_token_12345",
                    refreshToken = "mock_new_refresh_token_12345"
                ),
                message = "토큰이 재발급되었습니다"
            )

            path == "/auth/logout" && method == "POST" -> createSuccessResponse<Unit?>(
                request = request,
                data = null,
                message = "로그아웃되었습니다"
            )

            path == "/auth/password/change" && method == "POST" -> createSuccessResponse<Unit?>(
                request = request,
                data = null,
                message = "비밀번호가 변경되었습니다"
            )

            // User API Mock 응답
            path == "/users/me" && method == "GET" -> createSuccessResponse(
                request = request,
                data = UserResponse(
                    name = "테스트 사용자",
                    email = "test@example.com",
                    phone = "01012345678",
                    profileImageUrl = "https://example.com/profile.jpg"
                ),
                message = "프로필 조회 성공"
            )

            path == "/users/me" && method == "PATCH" -> createSuccessResponse(
                request = request,
                data = UserResponse(
                    name = "수정된 이름",
                    email = "test@example.com",
                    phone = "01012345678",
                    profileImageUrl = "https://example.com/profile.jpg"
                ),
                message = "프로필 수정 성공"
            )

            path == "/users/push-settings" && method == "GET" -> createSuccessResponse(
                request = request,
                data = UserPushSettingResponse(
                    timeLetter = true,
                    mindRecord = false,
                    afterNote = true
                ),
                message = "푸시 알림 설정 조회 성공"
            )

            path == "/users/push-settings" && method == "PATCH" -> createSuccessResponse(
                request = request,
                data = UserPushSettingResponse(
                    timeLetter = true,
                    mindRecord = true,
                    afterNote = false
                ),
                message = "푸시 알림 설정 수정 성공"
            )

            // TimeLetter API Mock 응답
            path == "/time-letters" && method == "GET" -> createSuccessResponse(
                request = request,
                data = TimeLetterListResponse(
                    timeLetters = listOf(
                        TimeLetterResponse(
                            id = 1L,
                            title = "미래의 나에게",
                            content = "1년 후의 나에게 보내는 편지...",
                            sendAt = "2025-12-31T00:00:00",
                            status = TimeLetterStatus.SCHEDULED,
                            mediaList = emptyList(),
                            createdAt = "2025-01-26T00:00:00",
                            updatedAt = "2025-01-26T00:00:00"
                        )
                    ),
                    totalCount = 1
                ),
                message = "타임레터 목록 조회 성공"
            )

            path == "/time-letters" && method == "POST" -> createSuccessResponse(
                request = request,
                data = TimeLetterResponse(
                    id = 1L,
                    title = "새 타임레터",
                    content = "테스트 내용",
                    sendAt = "2025-12-31T00:00:00",
                    status = TimeLetterStatus.DRAFT,
                    mediaList = emptyList(),
                    createdAt = "2025-01-26T00:00:00",
                    updatedAt = "2025-01-26T00:00:00"
                ),
                message = "타임레터 생성 성공"
            )

            path.matches(Regex("/time-letters/\\d+")) && method == "GET" -> createSuccessResponse(
                request = request,
                data = TimeLetterResponse(
                    id = 1L,
                    title = "타임레터 제목",
                    content = "타임레터 내용",
                    sendAt = "2025-12-31T00:00:00",
                    status = TimeLetterStatus.SCHEDULED,
                    mediaList = listOf(
                        TimeLetterMediaResponse(
                            id = 1L,
                            mediaType = TimeLetterMediaType.IMAGE,
                            mediaUrl = "https://example.com/image.jpg"
                        )
                    ),
                    createdAt = "2025-01-26T00:00:00",
                    updatedAt = "2025-01-26T00:00:00"
                ),
                message = "타임레터 조회 성공"
            )

            path.matches(Regex("/time-letters/\\d+")) && method == "PATCH" -> createSuccessResponse(
                request = request,
                data = TimeLetterResponse(
                    id = 1L,
                    title = "수정된 제목",
                    content = "수정된 내용",
                    sendAt = "2025-12-31T00:00:00",
                    status = TimeLetterStatus.SCHEDULED,
                    mediaList = emptyList(),
                    createdAt = "2025-01-26T00:00:00",
                    updatedAt = "2025-01-26T00:00:00"
                ),
                message = "타임레터 수정 성공"
            )

            path == "/time-letters/delete" && method == "POST" -> createSuccessResponse<Unit?>(
                request = request,
                data = null,
                message = "타임레터 삭제 성공"
            )

            path == "/time-letters/temporary" && method == "GET" -> createSuccessResponse(
                request = request,
                data = TimeLetterListResponse(
                    timeLetters = listOf(
                        TimeLetterResponse(
                            id = 2L,
                            title = "임시저장 타임레터",
                            content = "임시저장 내용",
                            sendAt = null,
                            status = TimeLetterStatus.DRAFT,
                            mediaList = emptyList(),
                            createdAt = "2025-01-26T00:00:00",
                            updatedAt = "2025-01-26T00:00:00"
                        )
                    ),
                    totalCount = 1
                ),
                message = "임시저장 타임레터 목록 조회 성공"
            )

            path == "/time-letters/temporary" && method == "DELETE" -> createSuccessResponse<Unit?>(
                request = request,
                data = null,
                message = "임시저장 타임레터 전체 삭제 성공"
            )

            else -> null
        }

        return mockResponse ?: chain.proceed(request)
    }

    private inline fun <reified T> createSuccessResponse(
        request: okhttp3.Request,
        data: T?,
        message: String = "Success"
    ): Response {
        val apiResponse = ApiResponse(
            status = 200,
            code = 200,
            message = message,
            data = data
        )

        // ApiResponse는 @Serializable이므로 직접 직렬화 가능
        val jsonString = json.encodeToString(
            kotlinx.serialization.serializer<ApiResponse<T>>(),
            apiResponse
        )
        val responseBody = jsonString.toResponseBody("application/json".toMediaType())

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(responseBody)
            .addHeader("Content-Type", "application/json")
            .build()
    }
}
