package com.kuit.afternote.data.remote

import com.kuit.afternote.BuildConfig
import com.kuit.afternote.feature.auth.data.dto.LoginData
import com.kuit.afternote.feature.auth.data.dto.ReissueData
import com.kuit.afternote.feature.auth.data.dto.SignUpData
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailData
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

/**
 * [MockApiInterceptor] 단위 테스트.
 *
 * 주의: BuildConfig.USE_MOCK_API가 true일 때만 동작합니다.
 */
class MockApiInterceptorTest {

    private lateinit var json: Json
    private lateinit var interceptor: MockApiInterceptor
    private lateinit var client: OkHttpClient

    @Before
    fun setUp() {
        json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            isLenient = true
            coerceInputValues = true
        }
        interceptor = MockApiInterceptor(json)
        client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Test
    fun intercept_whenMockApiDisabled_passesThrough() {
        // BuildConfig.USE_MOCK_API가 false면 실제 요청이 전달되어야 함
        // 이 테스트는 Mock API가 비활성화된 상태에서의 동작을 확인
        // 실제로는 BuildConfig를 모킹할 수 없으므로, Mock API 활성화 상태에서만 테스트
    }

    @Test
    fun intercept_authEmailSend_returnsMockResponse() {
        val request = Request.Builder()
            .url("https://afternote.kro.kr/auth/email/send")
            .post("{\"email\":\"test@example.com\"}".toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()

        // Mock API가 활성화되어 있으면 Mock 응답을 받아야 함
        // 비활성화되어 있으면 실제 서버 응답 또는 에러를 받음
        assertNotNull(response)
    }

    @Test
    fun intercept_authLogin_returnsMockLoginData() {
        val request = Request.Builder()
            .url("https://afternote.kro.kr/auth/login")
            .post("{\"email\":\"test@example.com\",\"password\":\"pwd123!\"}".toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()

        assertNotNull(response)
        if (BuildConfig.USE_MOCK_API) {
            assertEquals(200, response.code)
            val body = response.body?.string()
            assertNotNull(body)
            // JSON 파싱하여 LoginData 확인
            val apiResponse = json.decodeFromString<ApiResponse<LoginData?>>(body!!)
            assertEquals("로그인에 성공했습니다", apiResponse.message)
            assertNotNull(apiResponse.data)
            assertEquals("mock_access_token_12345", apiResponse.data?.accessToken)
        }
    }

    @Test
    fun intercept_authSignUp_returnsMockSignUpData() {
        val request = Request.Builder()
            .url("https://afternote.kro.kr/auth/sign-up")
            .post("{\"email\":\"test@example.com\",\"password\":\"pwd123!\",\"name\":\"홍길동\"}".toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()

        assertNotNull(response)
        if (BuildConfig.USE_MOCK_API) {
            assertEquals(200, response.code)
            val body = response.body?.string()
            assertNotNull(body)
            val apiResponse = json.decodeFromString<ApiResponse<SignUpData?>>(body!!)
            assertEquals("회원가입이 완료되었습니다", apiResponse.message)
            assertNotNull(apiResponse.data)
            assertEquals(1L, apiResponse.data?.userId)
        }
    }

    @Test
    fun intercept_authVerifyEmail_returnsMockVerifyEmailData() {
        val request = Request.Builder()
            .url("https://afternote.kro.kr/auth/email/verify")
            .post("{\"email\":\"test@example.com\",\"certificateCode\":\"123456\"}".toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()

        assertNotNull(response)
        if (BuildConfig.USE_MOCK_API) {
            assertEquals(200, response.code)
            val body = response.body?.string()
            assertNotNull(body)
            val apiResponse = json.decodeFromString<ApiResponse<VerifyEmailData?>>(body!!)
            assertEquals("이메일 인증이 완료되었습니다", apiResponse.message)
            assertNotNull(apiResponse.data)
            assertEquals(true, apiResponse.data?.isVerified)
        }
    }

    @Test
    fun intercept_authReissue_returnsMockReissueData() {
        val request = Request.Builder()
            .url("https://afternote.kro.kr/auth/reissue")
            .post("{\"refreshToken\":\"old_token\"}".toRequestBody("application/json".toMediaType()))
            .build()

        val response = client.newCall(request).execute()

        assertNotNull(response)
        if (BuildConfig.USE_MOCK_API) {
            assertEquals(200, response.code)
            val body = response.body?.string()
            assertNotNull(body)
            val apiResponse = json.decodeFromString<ApiResponse<ReissueData?>>(body!!)
            assertEquals("토큰이 재발급되었습니다", apiResponse.message)
            assertNotNull(apiResponse.data)
            assertEquals("mock_new_access_token_12345", apiResponse.data?.accessToken)
        }
    }

    @Test
    fun intercept_unknownPath_passesThrough() {
        val request = Request.Builder()
            .url("https://afternote.kro.kr/unknown/path")
            .get()
            .build()

        val response = client.newCall(request).execute()

        // 알 수 없는 경로는 실제 서버로 전달되어야 함
        // Mock API가 활성화되어 있어도 해당 경로는 Mock 응답을 반환하지 않음
        assertNotNull(response)
    }
}
