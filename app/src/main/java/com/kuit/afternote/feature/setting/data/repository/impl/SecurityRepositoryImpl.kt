package com.kuit.afternote.feature.setting.data.repository.impl

import android.content.SharedPreferences
import com.kuit.afternote.feature.setting.data.repository.iface.SecurityRepository
import androidx.core.content.edit

class SecurityRepositoryImpl(
    private val encryptedPrefs: SharedPreferences
) : SecurityRepository {
    override suspend fun savePassword(password: String): Result<Unit> {
        return runCatching {
            encryptedPrefs.edit { putString("APP_LOCK_PASSWORD", password) }
        }
    }
}
