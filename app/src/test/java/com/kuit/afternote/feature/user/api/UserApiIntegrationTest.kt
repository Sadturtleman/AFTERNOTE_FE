package com.kuit.afternote.feature.user.api

import com.kuit.afternote.feature.auth.data.api.AuthApiService
import com.kuit.afternote.feature.auth.data.dto.LoginRequest
import com.kuit.afternote.feature.user.data.api.UserApiService
import com.kuit.afternote.feature.user.data.dto.UserUpdateProfileRequest
import com.kuit.afternote.feature.user.data.dto.UserUpdatePushSettingRequest
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
 * User API 통합 테스트.
 *
 * 실제 API를 호출하여 HTTP 상태 코드를 검증합니다.
 * 네트워크 연결이 필요하며, 실제 백엔드 서버가 실행 중이어야 합니다.
 *
 * 테스트 대상 API (Swagger 문서 기준):
 * 1. GET /users/me - 내 프로필 조회
 * 2. PATCH /users/me - 프로필 수정
 * 3. GET /users/push-settings - 푸시 알림 설정 조회
 * 4. PATCH /users/push-settings - 푸시 알림 설정 수정
 */
class UserApiIntegrationTest {

    private lateinit var userApi: UserApiService
    private lateinit var authApi: AuthApiService
    private var accessToken: String? = null
    private var userId: Long = 1L // 테스트용 기본 userId (실제 테스트 시 조정 필요)

    companion object {
        private const val BASE_URL = "https://afternote.kro.kr/"

        // 테스트용 계정
        // local.properties의 TEST_EMAIL, TEST_PASSWORD를 우선 사용
        // 설정되지 않은 경우 하드코딩된 기본값 사용 (fallback)
        private val TEST_EMAIL = System.getProperty("TEST_EMAIL")
            ?: "dnfjddk2@gmail.com"
        private val TEST_PASSWORD = System.getProperty("TEST_PASSWORD")
            ?: "Qwerty12@"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        coerceInputValues = true
    }

    @Before
    fun setUp() {
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
        if (accessToken != null) return

        val response = authApi.login(
            LoginRequest(email = TEST_EMAIL, password = TEST_PASSWORD)
        )
        accessToken = response.data?.accessToken
        
        // userId는 프로필 조회로 얻거나 테스트 계정의 실제 userId를 사용
        // 여기서는 기본값 1L을 사용 (실제 테스트 시 조정 필요)

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

        userApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(authenticatedClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(UserApiService::class.java)
    }

    // ========== Get My Profile API Tests ==========

    @Test
    fun getMyProfile_withValidToken_returns200() = runBlocking {
        loginAndGetTokens()

        val response = userApi.getMyProfile(userId = userId)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        assertNotNull(response.data?.name)
        assertNotNull(response.data?.email)
    }

    @Test
    fun getMyProfile_withInvalidToken_returns401() = runBlocking {
        // 인증 없이 API 호출 시도
        val unauthenticatedClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()

        val unauthenticatedApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(unauthenticatedClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(UserApiService::class.java)

        try {
            unauthenticatedApi.getMyProfile(userId = 1L)
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Unauthenticated request returns HTTP ${e.code()}")
            assertEquals("Expected 401 for unauthenticated request", 401, e.code())
        }
    }

    @Test
    fun getMyProfile_withInvalidUserId_returnsExpectedStatusCode() = runBlocking {
        loginAndGetTokens()

        try {
            userApi.getMyProfile(userId = 999999L) // 존재하지 않는 userId
            // 성공할 수도 있고, 404를 반환할 수도 있음
        } catch (e: HttpException) {
            println("==> Invalid userId returns HTTP ${e.code()}")
            assertTrue("Expected 400, 401, or 404", e.code() in listOf(400, 401, 404))
        }
        Unit
    }

    // ========== Update My Profile API Tests ==========

    @Test
    fun updateMyProfile_withValidData_returns200() = runBlocking {
        loginAndGetTokens()

        val response = userApi.updateMyProfile(
            userId = userId,
            body = UserUpdateProfileRequest(
                name = "Updated Name",
                phone = "01012345678",
                profileImageUrl = null
            )
        )

        assertEquals(200, response.status)
        assertNotNull(response.data)
    }

    @Test
    fun updateMyProfile_withInvalidToken_returns401() = runBlocking {
        val unauthenticatedClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()

        val unauthenticatedApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(unauthenticatedClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(UserApiService::class.java)

        try {
            unauthenticatedApi.updateMyProfile(
                userId = 1L,
                body = UserUpdateProfileRequest(name = "Test")
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Unauthenticated update returns HTTP ${e.code()}")
            assertEquals("Expected 401 for unauthenticated request", 401, e.code())
        }
    }

    // ========== Get Push Settings API Tests ==========

    @Test
    fun getMyPushSettings_withValidToken_returns200() = runBlocking {
        loginAndGetTokens()

        val response = userApi.getMyPushSettings(userId = userId)

        assertEquals(200, response.status)
        assertNotNull(response.data)
        // Boolean 값들이 포함되어 있는지 확인
        assertNotNull(response.data?.timeLetter)
        assertNotNull(response.data?.mindRecord)
        assertNotNull(response.data?.afterNote)
    }

    @Test
    fun getMyPushSettings_withInvalidToken_returns401() = runBlocking {
        val unauthenticatedClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()

        val unauthenticatedApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(unauthenticatedClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(UserApiService::class.java)

        try {
            unauthenticatedApi.getMyPushSettings(userId = 1L)
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Unauthenticated push settings request returns HTTP ${e.code()}")
            assertEquals("Expected 401 for unauthenticated request", 401, e.code())
        }
    }

    // ========== Update Push Settings API Tests ==========

    @Test
    fun updateMyPushSettings_withValidData_returns200() = runBlocking {
        loginAndGetTokens()

        val response = userApi.updateMyPushSettings(
            userId = userId,
            body = UserUpdatePushSettingRequest(
                timeLetter = true,
                mindRecord = false,
                afterNote = true
            )
        )

        assertEquals(200, response.status)
        assertNotNull(response.data)
    }

    @Test
    fun updateMyPushSettings_withInvalidToken_returns401() = runBlocking {
        val unauthenticatedClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            .build()

        val unauthenticatedApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(unauthenticatedClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(UserApiService::class.java)

        try {
            unauthenticatedApi.updateMyPushSettings(
                userId = 1L,
                body = UserUpdatePushSettingRequest(timeLetter = true)
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Unauthenticated push settings update returns HTTP ${e.code()}")
            assertEquals("Expected 401 for unauthenticated request", 401, e.code())
        }
    }
}
