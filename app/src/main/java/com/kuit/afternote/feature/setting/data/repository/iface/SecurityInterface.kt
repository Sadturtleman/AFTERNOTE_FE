package com.kuit.afternote.feature.setting.data.repository.iface

interface SecurityRepository {
    suspend fun savePassword(password: String): Result<Unit>
}
