package com.kuit.afternote.data.remote

import com.kuit.afternote.BuildConfig
import com.kuit.afternote.feature.auth.data.dto.LoginData
import com.kuit.afternote.feature.auth.data.dto.ReissueData
import com.kuit.afternote.feature.auth.data.dto.SignUpData
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailData
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
