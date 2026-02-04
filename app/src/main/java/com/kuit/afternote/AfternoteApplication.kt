package com.kuit.afternote

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AfternoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val kakaoNativeAppKey = BuildConfig.KAKAO_NATIVE_APP_KEY
        if (kakaoNativeAppKey.isNotBlank()) {
            KakaoSdk.init(this, kakaoNativeAppKey)
        }
    }
}
