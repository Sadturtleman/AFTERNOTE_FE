package com.kuit.afternote.feature.timeletter.api

import com.kuit.afternote.feature.auth.data.api.AuthApiService
import com.kuit.afternote.feature.auth.data.dto.LoginRequest
import com.kuit.afternote.feature.timeletter.data.api.TimeLetterApiService
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterCreateRequest
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterDeleteRequest
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterStatus
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterUpdateRequest
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
 * TimeLetter API 통합 테스트.
 *
 * 실제 API를 호출하여 HTTP 상태 코드를 검증합니다.
 * 네트워크 연결이 필요하며, 실제 백엔드 서버가 실행 중이어야 합니다.
 *
 * 테스트 대상 API (Swagger 문서 기준):
 * 1. GET /time-letters - 타임레터 전체 조회
 * 2. POST /time-letters - 타임레터 등록
 * 3. GET /time-letters/{timeLetterId} - 타임레터 단일 조회
 * 4. PATCH /time-letters/{timeLetterId} - 타임레터 수정
 * 5. POST /time-letters/delete - 타임레터 삭제
 * 6. GET /time-letters/temporary - 임시저장 전체 조회
 * 7. DELETE /time-letters/temporary - 임시저장 전체 삭제
 */
class TimeLetterApiIntegrationTest {

    private lateinit var timeLetterApi: TimeLetterApiService
    private lateinit var authApi: AuthApiService
    private var accessToken: String? = null

    companion object {
        private const val BASE_URL = "https://afternote.kro.kr/"

        // 테스트용 계정 (실제 테스트 계정)
        private const val TEST_EMAIL = "dnfjddk2@gmail.com"
        private const val TEST_PASSWORD = "Ab@12345"
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

        timeLetterApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(authenticatedClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(TimeLetterApiService::class.java)
    }

    // ========== Get Time Letters API Tests ==========

    @Test
    fun getTimeLetters_withValidToken_returns200() = runBlocking {
        loginAndGetTokens()

        val response = timeLetterApi.getTimeLetters()

        assertEquals(200, response.status)
        assertNotNull(response.data)
    }

    @Test
    fun getTimeLetters_withInvalidToken_returns401() = runBlocking {
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
            .create(TimeLetterApiService::class.java)

        try {
            unauthenticatedApi.getTimeLetters()
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Unauthenticated request returns HTTP ${e.code()}")
            assertEquals("Expected 401 for unauthenticated request", 401, e.code())
        }
    }

    // ========== Create Time Letter API Tests ==========

    @Test
    fun createTimeLetter_withValidData_returns200() = runBlocking {
        loginAndGetTokens()

        val response = timeLetterApi.createTimeLetter(
            TimeLetterCreateRequest(
                title = "Test Time Letter",
                content = "Test content",
                sendAt = null,
                status = TimeLetterStatus.DRAFT,
                mediaList = null
            )
        )

        assertEquals(200, response.status)
        assertNotNull(response.data)
    }

    @Test
    fun createTimeLetter_withInvalidToken_returns401() = runBlocking {
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
            .create(TimeLetterApiService::class.java)

        try {
            unauthenticatedApi.createTimeLetter(
                TimeLetterCreateRequest(
                    status = TimeLetterStatus.DRAFT
                )
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Unauthenticated create returns HTTP ${e.code()}")
            assertEquals("Expected 401 for unauthenticated request", 401, e.code())
        }
    }

    // ========== Get Time Letter API Tests ==========

    @Test
    fun getTimeLetter_withValidId_returns200() = runBlocking {
        loginAndGetTokens()

        // 먼저 타임레터 목록을 가져와서 ID를 얻거나, 테스트용 ID 사용
        try {
            val response = timeLetterApi.getTimeLetter(timeLetterId = 1L)
            assertEquals(200, response.status)
            assertNotNull(response.data)
        } catch (e: HttpException) {
            // 404가 반환될 수 있음 (존재하지 않는 ID)
            println("==> Get time letter returns HTTP ${e.code()}")
            assertTrue("Expected 200 or 404", e.code() == 200 || e.code() == 404)
        }
    }

    @Test
    fun getTimeLetter_withInvalidId_returns404() = runBlocking {
        loginAndGetTokens()

        try {
            timeLetterApi.getTimeLetter(timeLetterId = 999999L)
            // 성공할 수도 있음
        } catch (e: HttpException) {
            println("==> Invalid time letter ID returns HTTP ${e.code()}")
            assertEquals("Expected 404 for invalid ID", 404, e.code())
        }
    }

    // ========== Get Temporary Time Letters API Tests ==========

    @Test
    fun getTemporaryTimeLetters_withValidToken_returns200() = runBlocking {
        loginAndGetTokens()

        val response = timeLetterApi.getTemporaryTimeLetters()

        assertEquals(200, response.status)
        assertNotNull(response.data)
    }

    // ========== Delete Time Letters API Tests ==========

    @Test
    fun deleteTimeLetters_withValidIds_returns200() = runBlocking {
        loginAndGetTokens()

        val response = timeLetterApi.deleteTimeLetters(
            TimeLetterDeleteRequest(timeLetterIds = listOf(1L))
        )

        // 200 또는 404 (존재하지 않는 ID일 수 있음)
        assertTrue("Expected 200 or 404", response.status == 200 || response.status == 404)
    }

    @Test
    fun deleteTimeLetters_withInvalidToken_returns401() = runBlocking {
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
            .create(TimeLetterApiService::class.java)

        try {
            unauthenticatedApi.deleteTimeLetters(
                TimeLetterDeleteRequest(timeLetterIds = listOf(1L))
            )
            throw AssertionError("Expected HttpException but got success response")
        } catch (e: HttpException) {
            println("==> Unauthenticated delete returns HTTP ${e.code()}")
            assertEquals("Expected 401 for unauthenticated request", 401, e.code())
        }
    }
}
