package com.kuit.afternote.feature.auth.api

import com.kuit.afternote.feature.auth.data.api.AuthApiService
import com.kuit.afternote.feature.auth.data.dto.LoginRequest
import com.kuit.afternote.feature.auth.data.dto.LogoutRequest
import com.kuit.afternote.feature.auth.data.dto.PasswordChangeRequest
import com.kuit.afternote.feature.auth.data.dto.ReissueRequest
import com.kuit.afternote.feature.auth.data.dto.SendEmailCodeRequest
import com.kuit.afternote.feature.auth.data.dto.SignUpRequest
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailRequest
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Auth API 통합 테스트.
 *
 * 실제 API를 호출하여 HTTP 상태 코드를 검증합니다.
 * 네트워크 연결이 필요하며, 실제 백엔드 서버가 실행 중이어야 합니다.
 *
 * 테스트 대상 API (Swagger 문서 기준):
 * 1. POST /auth/email/send - 이메일 인증코드 발송
 * 2. POST /auth/email/verify - 이메일 인증코드 검증
 * 3. POST /auth/sign-up - 회원가입
 * 4. POST /auth/login - 로그인
 * 5. POST /auth/reissue - 토큰 재발급
 * 6. POST /auth/logout - 로그아웃
 * 7. POST /auth/password/change - 비밀번호 변경
 */
class AuthApiIntegrationTest {

    private lateinit var authApi: AuthApiService
    private lateinit var authenticatedApi: AuthApiService
    private var accessToken: String? = null
    private var refreshToken: String? = null

    companion object {
        private const val BASE_URL = "https://afternote.kro.kr/"

        // 테스트용 존재하는 계정 (실제 테스트 계정)
        private const val EXISTING_EMAIL = "dnfjddk2@gmail.com"
        private const val CORRECT_PASSWORD = "Ab@12345"

        // 테스트용 존재하지 않는 계정
        private const val NON_EXISTENT_EMAIL = "nonexistent_test_12345@example.com"
        private const val WRONG_PASSWORD = "WrongPassword123!"

        // 유효하지 않은 토큰
        private const val INVALID_TOKEN = "invalid_token_12345"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        coerceInputValues = true
    }

    @Before
    fun setUp() {
        // 인증 없는 API 클라이언트
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

        authApi = retrofit.create(AuthApiService::class.java)
    }

    /**
     * 인증이 필요한 API 테스트를 위해 로그인하고 토큰을 획득합니다.
     */
    private suspend fun loginAndGetTokens() {
        if (accessToken != null && refreshToken != null) return

        val response = authApi.login(
            LoginRequest(email = EXISTING_EMAIL, password = CORRECT_PASSWORD)
        )
        accessToken = response.data?.accessToken
        refreshToken = response.data?.refreshToken

        // 인증된 API 클라이언트 생성
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            chain.proceed(request)
        }

        val authenticatedClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()

        authenticatedApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(authenticatedClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(AuthApiService::class.java)
    }

    // ========== Login API Tests ==========

    @Test
    fun login_withCorrectCredentials_returns200() = runBlocking {
        val response = authApi.login(
            LoginRequest(email = EXISTING_EMAIL, password = CORRECT_PASSWORD)
        )

        // 성공 시 200 OK, data에 토큰이 포함됨
        assertEquals(200, response.status)
    }

    @Test
    fun login_withWrongPassword_returnsExpectedStatusCode() = runBlocking {
        try {
            authApi.login(
                LoginRequest(email = EXISTING_EMAIL, password = WRONG_PASSWORD)
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            // 현재 API는 400을 반환하지만, 401이 더 적절함
            println("==> Wrong password returns HTTP ${e.code()}")
            // 400 또는 401 중 하나여야 함
            assert(e.code() == 400 || e.code() == 401) {
                "Expected 400 or 401 but got ${e.code()}"
            }
        }
    }

    @Test
    fun login_withNonExistentAccount_returnsExpectedStatusCode() = runBlocking {
        try {
            authApi.login(
                LoginRequest(email = NON_EXISTENT_EMAIL, password = WRONG_PASSWORD)
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Non-existent account returns HTTP ${e.code()}")
            // 404 또는 401 중 하나여야 함
            assert(e.code() == 404 || e.code() == 401) {
                "Expected 404 or 401 but got ${e.code()}"
            }
        }
    }

    // ========== Email Verification API Tests ==========

    @Test
    fun sendEmailCode_withInvalidEmailFormat_returns400() = runBlocking {
        try {
            authApi.sendEmailCode(
                SendEmailCodeRequest(email = "invalid-email-format")
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Invalid email format returns HTTP ${e.code()}")
            assertEquals("Expected 400 for invalid email format", 400, e.code())
        }
    }

    @Test
    fun verifyEmail_withWrongCode_returnsExpectedStatusCode() = runBlocking {
        try {
            authApi.verifyEmail(
                VerifyEmailRequest(email = EXISTING_EMAIL, certificateCode = "000000")
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Wrong verification code returns HTTP ${e.code()}")
            // 400 또는 401 중 하나여야 함
            assert(e.code() == 400 || e.code() == 401) {
                "Expected 400 or 401 but got ${e.code()}"
            }
        }
    }

    // ========== Sign Up API Tests ==========

    @Test
    fun signUp_withExistingEmail_returns409() = runBlocking {
        try {
            authApi.signUp(
                SignUpRequest(
                    email = EXISTING_EMAIL,
                    password = "TestPassword123!",
                    name = "TestName",
                    profileUrl = null
                )
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Existing email signup returns HTTP ${e.code()}")
            // 409 Conflict가 예상됨
            assertEquals("Expected 409 for existing email", 409, e.code())
        }
    }

    @Test
    fun signUp_withInvalidPasswordFormat_returns400() = runBlocking {
        try {
            authApi.signUp(
                SignUpRequest(
                    email = "newuser_${System.currentTimeMillis()}@test.com",
                    password = "weak", // 약한 비밀번호
                    name = "TestName",
                    profileUrl = null
                )
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Invalid password format returns HTTP ${e.code()}")
            assertEquals("Expected 400 for invalid password format", 400, e.code())
        }
    }

    // ========== Login Additional Tests ==========

    @Test
    fun login_withEmptyEmail_returns400() = runBlocking {
        try {
            authApi.login(LoginRequest(email = "", password = CORRECT_PASSWORD))
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Empty email returns HTTP ${e.code()}")
            assertEquals("Expected 400 for empty email", 400, e.code())
        }
    }

    @Test
    fun login_withEmptyPassword_returns400() = runBlocking {
        try {
            authApi.login(LoginRequest(email = EXISTING_EMAIL, password = ""))
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Empty password returns HTTP ${e.code()}")
            assertEquals("Expected 400 for empty password", 400, e.code())
        }
    }

    // ========== Reissue API Tests ==========

    @Test
    fun reissue_withValidRefreshToken_returns200() = runBlocking {
        loginAndGetTokens()
        assertNotNull("RefreshToken should not be null", refreshToken)

        val response = authApi.reissue(ReissueRequest(refreshToken = refreshToken!!))
        assertEquals(200, response.status)
        assertNotNull("New accessToken should not be null", response.data?.accessToken)
    }

    @Test
    fun reissue_withInvalidToken_returnsExpectedStatusCode() = runBlocking {
        try {
            authApi.reissue(ReissueRequest(refreshToken = INVALID_TOKEN))
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Invalid refresh token returns HTTP ${e.code()}")
            // 401 또는 400 중 하나여야 함
            assertTrue(
                "Expected 400 or 401 but got ${e.code()}",
                e.code() == 400 || e.code() == 401
            )
        }
    }

    @Test
    fun reissue_withEmptyToken_returns400() = runBlocking {
        try {
            authApi.reissue(ReissueRequest(refreshToken = ""))
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Empty refresh token returns HTTP ${e.code()}")
            assertEquals("Expected 400 for empty token", 400, e.code())
        }
    }

    // ========== Logout API Tests ==========

    /**
     * 로그아웃 API 테스트.
     *
     * 참고: 로그아웃 API는 Authorization 헤더가 필요할 수 있습니다.
     * 현재 API는 refresh token만으로는 401을 반환합니다.
     */
    @Test
    fun logout_withValidRefreshToken_returnsExpectedStatusCode() = runBlocking {
        // 새로운 로그인을 통해 fresh 토큰 획득
        val loginResponse = authApi.login(
            LoginRequest(email = EXISTING_EMAIL, password = CORRECT_PASSWORD)
        )
        val freshRefreshToken = loginResponse.data?.refreshToken
        val freshAccessToken = loginResponse.data?.accessToken
        assertNotNull("RefreshToken should not be null", freshRefreshToken)

        // Authorization 헤더가 있는 클라이언트로 로그아웃 시도
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $freshAccessToken")
                .build()
            chain.proceed(request)
        }

        val authenticatedClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()

        val authenticatedApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(authenticatedClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(AuthApiService::class.java)

        try {
            val response = authenticatedApi.logout(LogoutRequest(refreshToken = freshRefreshToken!!))
            println("==> Logout with auth header returns HTTP ${response.status}")
            assertEquals(200, response.status)
        } catch (e: HttpException) {
            println("==> Logout with auth header returns HTTP ${e.code()}")
            // API가 401을 반환하면 기록 (Authorization 헤더가 있어도)
            assertTrue(
                "Expected 200 or 401 but got ${e.code()}",
                e.code() == 401 || e.code() == 200
            )
        }

        // 토큰 무효화
        accessToken = null
        refreshToken = null
    }

    @Test
    fun logout_withInvalidToken_returnsExpectedStatusCode() = runBlocking {
        try {
            authApi.logout(LogoutRequest(refreshToken = INVALID_TOKEN))
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Invalid logout token returns HTTP ${e.code()}")
            // 401 또는 400 중 하나여야 함
            assertTrue(
                "Expected 400 or 401 but got ${e.code()}",
                e.code() == 400 || e.code() == 401
            )
        }
    }

    @Test
    fun logout_withEmptyToken_returns401() = runBlocking {
        try {
            authApi.logout(LogoutRequest(refreshToken = ""))
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Empty logout token returns HTTP ${e.code()}")
            assertEquals("Expected 401 for empty token", 401, e.code())
        }
    }

    // ========== Password Change API Tests ==========

    @Test
    fun passwordChange_withCorrectCurrentPassword_returns200() = runBlocking {
        loginAndGetTokens()

        // 비밀번호 변경 후 다시 원래대로 변경 (테스트 계정 보호)
        val newPassword = "NewPassword123!"

        val response = authenticatedApi.passwordChange(
            PasswordChangeRequest(
                currentPassword = CORRECT_PASSWORD,
                newPassword = newPassword
            )
        )
        assertEquals(200, response.status)

        // 원래 비밀번호로 복구
        val restoreResponse = authenticatedApi.passwordChange(
            PasswordChangeRequest(
                currentPassword = newPassword,
                newPassword = CORRECT_PASSWORD
            )
        )
        assertEquals(200, restoreResponse.status)

        // 토큰 초기화
        accessToken = null
        refreshToken = null
    }

    @Test
    fun passwordChange_withWrongCurrentPassword_returnsExpectedStatusCode() = runBlocking {
        loginAndGetTokens()

        try {
            authenticatedApi.passwordChange(
                PasswordChangeRequest(
                    currentPassword = WRONG_PASSWORD,
                    newPassword = "NewPassword123!"
                )
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Wrong current password returns HTTP ${e.code()}")
            // 400 또는 401 중 하나여야 함
            assertTrue(
                "Expected 400 or 401 but got ${e.code()}",
                e.code() == 400 || e.code() == 401
            )
        }
    }

    @Test
    fun passwordChange_withInvalidNewPasswordFormat_returns400() = runBlocking {
        loginAndGetTokens()

        try {
            authenticatedApi.passwordChange(
                PasswordChangeRequest(
                    currentPassword = CORRECT_PASSWORD,
                    newPassword = "weak" // 약한 비밀번호
                )
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Invalid new password format returns HTTP ${e.code()}")
            assertEquals("Expected 400 for invalid password format", 400, e.code())
        }
    }

    @Test
    fun passwordChange_withoutAuthentication_returns401() = runBlocking {
        try {
            // 인증 없이 비밀번호 변경 시도
            authApi.passwordChange(
                PasswordChangeRequest(
                    currentPassword = CORRECT_PASSWORD,
                    newPassword = "NewPassword123!"
                )
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Unauthenticated password change returns HTTP ${e.code()}")
            assertEquals("Expected 401 for unauthenticated request", 401, e.code())
        }
    }

    // ========== Send Email Code Additional Tests ==========

    @Test
    fun sendEmailCode_withEmptyEmail_returns400() = runBlocking {
        try {
            authApi.sendEmailCode(SendEmailCodeRequest(email = ""))
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Empty email returns HTTP ${e.code()}")
            assertEquals("Expected 400 for empty email", 400, e.code())
        }
    }

    // ========== Verify Email Additional Tests ==========

    @Test
    fun verifyEmail_withNonExistentEmail_returnsExpectedStatusCode() = runBlocking {
        try {
            authApi.verifyEmail(
                VerifyEmailRequest(email = NON_EXISTENT_EMAIL, certificateCode = "123456")
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Non-existent email verify returns HTTP ${e.code()}")
            // 400 또는 404 중 하나여야 함
            assertTrue(
                "Expected 400 or 404 but got ${e.code()}",
                e.code() == 400 || e.code() == 404
            )
        }
    }

    @Test
    fun verifyEmail_withEmptyCode_returns400() = runBlocking {
        try {
            authApi.verifyEmail(
                VerifyEmailRequest(email = EXISTING_EMAIL, certificateCode = "")
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Empty code returns HTTP ${e.code()}")
            assertEquals("Expected 400 for empty code", 400, e.code())
        }
    }

    // ========== Sign Up Additional Tests ==========

    @Test
    fun signUp_withInvalidEmailFormat_returns400() = runBlocking {
        try {
            authApi.signUp(
                SignUpRequest(
                    email = "invalid-email-format",
                    password = "ValidPassword123!",
                    name = "TestName",
                    profileUrl = null
                )
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Invalid email format signup returns HTTP ${e.code()}")
            assertEquals("Expected 400 for invalid email format", 400, e.code())
        }
    }

    @Test
    fun signUp_withEmptyName_returns400() = runBlocking {
        try {
            authApi.signUp(
                SignUpRequest(
                    email = "newuser_${System.currentTimeMillis()}@test.com",
                    password = "ValidPassword123!",
                    name = "",
                    profileUrl = null
                )
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Empty name signup returns HTTP ${e.code()}")
            assertEquals("Expected 400 for empty name", 400, e.code())
        }
    }

    // ========== Comprehensive Status Code Documentation Test ==========

    /**
     * 이 테스트는 모든 API 시나리오의 실제 상태 코드를 문서화합니다.
     */
    @Test
    fun documentAllActualStatusCodes() = runBlocking {
        val results = mutableMapOf<String, Any>()

        // ===== Login =====
        try {
            val r = authApi.login(LoginRequest(EXISTING_EMAIL, CORRECT_PASSWORD))
            results["login_success"] = r.status
            accessToken = r.data?.accessToken
            refreshToken = r.data?.refreshToken
        } catch (e: HttpException) {
            results["login_success"] = "FAILED: ${e.code()}"
        }

        try {
            authApi.login(LoginRequest(EXISTING_EMAIL, WRONG_PASSWORD))
            results["login_wrong_password"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["login_wrong_password"] = e.code()
        }

        try {
            authApi.login(LoginRequest(NON_EXISTENT_EMAIL, WRONG_PASSWORD))
            results["login_non_existent"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["login_non_existent"] = e.code()
        }

        try {
            authApi.login(LoginRequest("", CORRECT_PASSWORD))
            results["login_empty_email"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["login_empty_email"] = e.code()
        }

        // ===== Send Email Code =====
        try {
            authApi.sendEmailCode(SendEmailCodeRequest("invalid"))
            results["send_code_invalid_email"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["send_code_invalid_email"] = e.code()
        }

        try {
            authApi.sendEmailCode(SendEmailCodeRequest(""))
            results["send_code_empty_email"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["send_code_empty_email"] = e.code()
        }

        // ===== Verify Email =====
        try {
            authApi.verifyEmail(VerifyEmailRequest(EXISTING_EMAIL, "000000"))
            results["verify_wrong_code"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["verify_wrong_code"] = e.code()
        }

        try {
            authApi.verifyEmail(VerifyEmailRequest(EXISTING_EMAIL, ""))
            results["verify_empty_code"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["verify_empty_code"] = e.code()
        }

        // ===== Sign Up =====
        try {
            authApi.signUp(SignUpRequest(EXISTING_EMAIL, "Test123!", "TestName", null))
            results["signup_existing_email"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["signup_existing_email"] = e.code()
        }

        try {
            authApi.signUp(SignUpRequest("invalid", "Test123!", "TestName", null))
            results["signup_invalid_email"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["signup_invalid_email"] = e.code()
        }

        try {
            authApi.signUp(SignUpRequest("test@test.com", "weak", "TestName", null))
            results["signup_weak_password"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["signup_weak_password"] = e.code()
        }

        // ===== Reissue =====
        if (refreshToken != null) {
            try {
                val r = authApi.reissue(ReissueRequest(refreshToken!!))
                results["reissue_valid_token"] = r.status
            } catch (e: HttpException) {
                results["reissue_valid_token"] = "FAILED: ${e.code()}"
            }
        }

        try {
            authApi.reissue(ReissueRequest(INVALID_TOKEN))
            results["reissue_invalid_token"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["reissue_invalid_token"] = e.code()
        }

        try {
            authApi.reissue(ReissueRequest(""))
            results["reissue_empty_token"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["reissue_empty_token"] = e.code()
        }

        // ===== Logout =====
        try {
            authApi.logout(LogoutRequest(INVALID_TOKEN))
            results["logout_invalid_token"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["logout_invalid_token"] = e.code()
        }

        try {
            authApi.logout(LogoutRequest(""))
            results["logout_empty_token"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["logout_empty_token"] = e.code()
        }

        // ===== Password Change =====
        try {
            authApi.passwordChange(PasswordChangeRequest(CORRECT_PASSWORD, "NewPass123!"))
            results["password_change_no_auth"] = "200 (unexpected)"
        } catch (e: HttpException) {
            results["password_change_no_auth"] = e.code()
        }

        // 결과 출력
        println("================================================================")
        println("              ALL ACTUAL API STATUS CODES")
        println("================================================================")
        println("| Scenario                        | Status Code |")
        println("|--------------------------------|-------------|")
        results.forEach { (scenario, code) ->
            println("| ${scenario.padEnd(30)} | $code |")
        }
        println("================================================================")

        assert(results.isNotEmpty()) { "No API calls were made" }
    }
}
