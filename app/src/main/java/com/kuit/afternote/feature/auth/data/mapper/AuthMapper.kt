package com.kuit.afternote.feature.auth.data.mapper

import com.kuit.afternote.feature.auth.data.dto.LoginData
import com.kuit.afternote.feature.auth.data.dto.ReissueData
import com.kuit.afternote.feature.auth.data.dto.SignUpData
import com.kuit.afternote.feature.auth.data.dto.VerifyEmailData
import com.kuit.afternote.feature.auth.domain.model.EmailVerifyResult
import com.kuit.afternote.feature.auth.domain.model.LoginResult
import com.kuit.afternote.feature.auth.domain.model.ReissueResult
import com.kuit.afternote.feature.auth.domain.model.SignUpResult

/**
 * Auth DTO를 Domain 모델로 변환. (스웨거 기준)
 */
object AuthMapper {

    fun toEmailVerifyResult(dto: VerifyEmailData): EmailVerifyResult =
        EmailVerifyResult(isVerified = dto.isVerified ?: true)

    fun toSignUpResult(dto: SignUpData): SignUpResult =
        SignUpResult(userId = dto.userId, email = dto.email)

    fun toLoginResult(dto: LoginData): LoginResult =
        LoginResult(accessToken = dto.accessToken, refreshToken = dto.refreshToken)

    fun toReissueResult(dto: ReissueData): ReissueResult =
        ReissueResult(accessToken = dto.accessToken, refreshToken = dto.refreshToken)
}
