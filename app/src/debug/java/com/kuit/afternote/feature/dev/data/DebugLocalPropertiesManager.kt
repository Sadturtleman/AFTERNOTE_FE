package com.kuit.afternote.feature.dev.data

import android.content.Context
import android.util.Log
import com.kuit.afternote.feature.dev.domain.LocalPropertiesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Properties
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Debug 빌드 전용: local.properties 파일 업데이트 구현체.
 *
 * 비밀번호 변경 시 local.properties의 TEST_PASSWORD를 자동으로 업데이트합니다.
 */
@Singleton
class DebugLocalPropertiesManager
    @Inject
    constructor(
        @ApplicationContext private val context: Context
    ) : LocalPropertiesManager {
        override suspend fun updateTestPassword(newPassword: String): Boolean {
            return withContext(Dispatchers.IO) {
                try {
                    // 프로젝트 루트 찾기: app의 filesDir에서 상위로 올라가기
                    // /data/user/0/com.kuit.afternote/files -> ... -> /Users/zach/StudioProjects/AFTERNOTE_FE
                    var currentDir = context.filesDir
                    var projectRoot: File? = null

                    // 최대 10단계까지 상위 디렉토리 탐색
                    repeat(10) {
                        currentDir = currentDir.parentFile ?: return@repeat
                        val localProps = File(currentDir, "local.properties")
                        if (localProps.exists()) {
                            projectRoot = currentDir
                            return@repeat
                        }
                    }

                    val localPropertiesFile = projectRoot?.let { File(it, "local.properties") }
                        ?: run {
                            Log.w(TAG, "Could not find project root directory")
                            return@withContext false
                        }

                    if (!localPropertiesFile.exists()) {
                        Log.w(TAG, "local.properties file not found at: ${localPropertiesFile.absolutePath}")
                        return@withContext false
                    }

                    // 기존 properties 읽기
                    val properties = Properties().apply {
                        localPropertiesFile.inputStream().use { load(it) }
                    }

                    // TEST_PASSWORD 업데이트
                    properties.setProperty("TEST_PASSWORD", newPassword)

                    // 파일에 쓰기
                    localPropertiesFile.outputStream().use { output ->
                        properties.store(
                            output,
                            "## This file must *NOT* be checked into Version Control Systems,\n" +
                                "# as it contains information specific to your local configuration.\n" +
                                "#\n" +
                                "# Location of the SDK. This is only used by Gradle.\n" +
                                "# For customization when using a Version Control System, please read the\n" +
                                "# header note."
                        )
                    }

                    Log.d(TAG, "Successfully updated TEST_PASSWORD in local.properties")
                    true
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to update local.properties", e)
                    false
                }
            }
        }

        companion object {
            private const val TAG = "LocalPropsManager"
        }
    }
