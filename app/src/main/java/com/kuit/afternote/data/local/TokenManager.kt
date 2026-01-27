package com.kuit.afternote.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kuit.afternote.util.JwtDecoder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_tokens")

/**
 * 인증 토큰을 관리하는 클래스.
 *
 * DataStore를 사용하여 accessToken과 refreshToken을 저장/조회/삭제합니다.
 */
@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    private val refreshTokenKey = stringPreferencesKey("refresh_token")
    private val userEmailKey = stringPreferencesKey("user_email")

    /**
     * Access Token Flow.
     */
    val accessTokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[accessTokenKey]
    }

    /**
     * Refresh Token Flow.
     */
    val refreshTokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[refreshTokenKey]
    }

    /**
     * User Email Flow.
     */
    val userEmailFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[userEmailKey]
    }

    /**
     * 로그인 상태 Flow.
     */
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        !preferences[accessTokenKey].isNullOrEmpty()
    }

    /**
     * Access Token을 동기적으로 가져옵니다.
     */
    suspend fun getAccessToken(): String? {
        return context.dataStore.data.first()[accessTokenKey]
    }

    /**
     * Refresh Token을 동기적으로 가져옵니다.
     */
    suspend fun getRefreshToken(): String? {
        return context.dataStore.data.first()[refreshTokenKey]
    }

    /**
     * 토큰을 저장합니다.
     *
     * @param accessToken Access Token
     * @param refreshToken Refresh Token
     * @param email 사용자 이메일 (선택)
     */
    suspend fun saveTokens(accessToken: String, refreshToken: String, email: String? = null) {
        context.dataStore.edit { preferences ->
            preferences[accessTokenKey] = accessToken
            preferences[refreshTokenKey] = refreshToken
            email?.let { preferences[userEmailKey] = it }
        }
    }

    /**
     * 토큰을 업데이트합니다 (재발급 시 사용).
     *
     * @param accessToken 새로운 Access Token
     * @param refreshToken 새로운 Refresh Token
     */
    suspend fun updateTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[accessTokenKey] = accessToken
            preferences[refreshTokenKey] = refreshToken
        }
    }

    /**
     * 모든 토큰을 삭제합니다 (로그아웃 시 사용).
     */
    suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences.remove(accessTokenKey)
            preferences.remove(refreshTokenKey)
            preferences.remove(userEmailKey)
        }
    }

    /**
     * 로그인 상태를 동기적으로 확인합니다.
     */
    suspend fun isLoggedIn(): Boolean {
        return !getAccessToken().isNullOrEmpty()
    }

    /**
     * JWT 토큰에서 userId를 추출합니다.
     *
     * @return userId (Long) 또는 null (토큰이 없거나 userId를 찾을 수 없는 경우)
     */
    suspend fun getUserId(): Long? {
        val accessToken = getAccessToken()
        return if (!accessToken.isNullOrEmpty()) {
            JwtDecoder.getUserId(accessToken)
        } else {
            null
        }
    }
}
