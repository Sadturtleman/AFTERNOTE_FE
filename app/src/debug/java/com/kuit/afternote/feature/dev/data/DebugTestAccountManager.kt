package com.kuit.afternote.feature.dev.data

import android.content.Context
import android.content.SharedPreferences
import com.kuit.afternote.BuildConfig
import com.kuit.afternote.feature.dev.domain.TestAccountManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Debug 빌드 전용: 테스트 계정 비밀번호 관리 구현체.
 *
 * SharedPreferences를 사용하여 현재 테스트 계정의 비밀번호를 저장하고 조회합니다.
 * "Cycle Password" 전략: 저장된 비밀번호를 사용하여 실제 change-password API를 호출합니다.
 */
@Singleton
class DebugTestAccountManager @Inject constructor(
    @ApplicationContext private val context: Context
) : TestAccountManager {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun getCurrentPassword(): String {
        // 저장된 비밀번호가 있으면 반환, 없으면 BuildConfig의 기본값 사용
        return prefs.getString(KEY_PASSWORD, BuildConfig.TEST_PASSWORD) ?: BuildConfig.TEST_PASSWORD
    }

    override suspend fun updateStoredPassword(newPassword: String) {
        prefs.edit().putString(KEY_PASSWORD, newPassword).apply()
    }

    companion object {
        private const val PREFS_NAME = "test_account_credentials"
        private const val KEY_PASSWORD = "current_test_password"
    }
}
