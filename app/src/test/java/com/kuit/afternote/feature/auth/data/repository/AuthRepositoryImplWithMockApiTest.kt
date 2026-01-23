package com.kuit.afternote.feature.auth.data.repository

import com.kuit.afternote.BuildConfig
import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.data.remote.MockApiInterceptor
import com.kuit.afternote.feature.auth.data.api.AuthApiService
import com.kuit.afternote.feature.auth.data.dto.LoginData
import com.kuit.afternote.feature.auth.data.dto.LoginRequest
import com.kuit.afternote.feature.auth.data.dto.ReissueData
import com.kuit.afternote.feature.auth.data.dto.ReissueRequest
import com.kuit.afternote.feature.auth.data.dto.SignUpData
import com.kuit.afternote.feature.auth.data.dto.SignUpRequest
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailData
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailRequest
import com.kuit.afternote.feature.auth.domain.model.EmailVerifyResult
import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.model.ReissueResult
import com.kuit.afternote.feature.auth.domain.model.SignUpResult
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

/**
 * [AuthRepositoryImpl] 통합 테스트 (Mock API 사용).
 *
 * Mock API Interceptor를 사용하여 실제 네트워크 호출 없이 테스트합니다.
 * BuildConfig.USE_MOCK_API가 true일 때만 동작합니다.
 */
class AuthRepositoryImplWithMockApiTest {

    private lateinit var json: Json
    private lateinit var api: AuthApiService
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            isLenient = true
            coerceInputValues = true
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .apply {
                // Mock API 모드일 때만 Mock Interceptor 추가
                if (BuildConfig.USE_MOCK_API) {
                    addInterceptor(MockApiInterceptor(json))
                }
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://afternote.kro.kr/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        api = retrofit.create()
        repository = AuthRepositoryImpl(api)
    }

    @Test
    fun sendEmailCode_whenMockApiEnabled_returnsSuccess() = runBlocking {
        if (!BuildConfig.USE_MOCK_API) {
            return@runBlocking // Mock API가 비활성화되어 있으면 테스트 스킵
        }

        val result = repository.sendEmailCode("test@example.com")

        assertTrue(result.isSuccess)
    }

    @Test
    fun verifyEmail_whenMockApiEnabled_returnsEmailVerifyResult() = runBlocking {
        if (!BuildConfig.USE_MOCK_API) {
            return@runBlocking
        }

        val result = repository.verifyEmail("test@example.com", "123456")

        assertTrue(result.isSuccess)
        assertEquals(EmailVerifyResult(isVerified = true), result.getOrNull())
    }

    @Test
    fun signUp_whenMockApiEnabled_returnsSignUpResult() = runBlocking {
        if (!BuildConfig.USE_MOCK_API) {
            return@runBlocking
        }

        val result = repository.signUp("test@example.com", "password123!", "홍길동", null)

        assertTrue(result.isSuccess)
        val signUpResult = result.getOrNull()
        assertNotNull(signUpResult)
        assertEquals(1L, signUpResult?.userId)
        assertEquals("test@example.com", signUpResult?.email)
    }

    @Test
    fun login_whenMockApiEnabled_returnsLoginResult() = runBlocking {
        if (!BuildConfig.USE_MOCK_API) {
            return@runBlocking
        }

        val result = repository.login("test@example.com", "password123!")

        assertTrue(result.isSuccess)
        val loginResult = result.getOrNull()
        assertNotNull(loginResult)
        assertEquals("mock_access_token_12345", loginResult?.accessToken)
        assertEquals("mock_refresh_token_12345", loginResult?.refreshToken)
    }

    @Test
    fun reissue_whenMockApiEnabled_returnsReissueResult() = runBlocking {
        if (!BuildConfig.USE_MOCK_API) {
            return@runBlocking
        }

        val result = repository.reissue("old_refresh_token")

        assertTrue(result.isSuccess)
        val reissueResult = result.getOrNull()
        assertNotNull(reissueResult)
        assertEquals("mock_new_access_token_12345", reissueResult?.accessToken)
        assertEquals("mock_new_refresh_token_12345", reissueResult?.refreshToken)
    }

    @Test
    fun logout_whenMockApiEnabled_returnsSuccess() = runBlocking {
        if (!BuildConfig.USE_MOCK_API) {
            return@runBlocking
        }

        val result = repository.logout("refresh_token")

        assertTrue(result.isSuccess)
    }

    @Test
    fun passwordChange_whenMockApiEnabled_returnsSuccess() = runBlocking {
        if (!BuildConfig.USE_MOCK_API) {
            return@runBlocking
        }

        val result = repository.passwordChange("oldPwd", "newPwd")

        assertTrue(result.isSuccess)
    }
}
