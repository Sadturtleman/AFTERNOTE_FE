package com.kuit.afternote.feature.receiverauth.session

import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 수신자 인증번호(authCode)를 수신자 플로우 동안 보관하는 세션 홀더.
 *
 * 마스터키 검증 완료 후 receiver_main 진입 시 설정하고,
 * 수신자 메인을 벗어날 때 clear합니다.
 * receiver-auth API 호출 시 X-Auth-Code로 사용합니다.
 */
@Singleton
class ReceiverAuthSessionHolder
    @Inject
    constructor() {

    @Volatile
    private var authCode: String? = null

    @Volatile
    private var senderName: String? = null

    @Volatile
    private var selectedTimeLetter: ReceivedTimeLetter? = null

    fun setAuthCode(code: String) {
        authCode = code
    }

    fun getAuthCode(): String? = authCode

    /** 마스터키 검증 성공 시 수신한 발신자 이름. 수신자 홈 "故 OO님이 남기신 기록" 표시용. */
    fun setSenderName(name: String?) {
        senderName = name
    }

    fun getSenderName(): String? = senderName

    fun clearAuthCode() {
        authCode = null
        senderName = null
        selectedTimeLetter = null
    }

    /** 타임레터 상세로 진입 시 목록에서 선택한 항목. 상세 화면에서 API 없이 표시용으로 사용. */
    fun setSelectedTimeLetter(letter: ReceivedTimeLetter?) {
        selectedTimeLetter = letter
    }

    fun getSelectedTimeLetter(): ReceivedTimeLetter? = selectedTimeLetter
}
