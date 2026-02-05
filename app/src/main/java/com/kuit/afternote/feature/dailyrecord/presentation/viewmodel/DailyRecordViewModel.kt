package com.kuit.afternote.feature.dailyrecord.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.dailyrecord.data.RecordPost
import com.kuit.afternote.feature.dailyrecord.domain.usecase.DailyRecordPostUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class DailyRecordViewModel(
    private val dailyRecordPostUseCase: DailyRecordPostUseCase
) : ViewModel() {
    // 더미 데이터
    private val dummyPosts = mutableStateListOf(
        RecordPost(
            id = 1,
            title = "Jetpack Compose 시작하기",
            content = "Jetpack Compose는 Android의 최신 UI 툴킷입니다. 선언형 UI로 더 쉽고 빠르게 UI를 만들 수 있습니다.",
            createdAt = "2025-10-05T10:00:00",
            updatedAt = "2025-10-05T10:00:00"
        ),
        RecordPost(
            id = 2,
            title = "Kotlin Coroutines 완벽 가이드",
            content = "비동기 프로그래밍을 쉽게! Coroutines를 사용하면 복잡한 비동기 코드를 간단하게 작성할 수 있습니다.",
            createdAt = "2025-10-05T11:30:00",
            updatedAt = "2025-10-05T11:30:00"
        ),
        RecordPost(
            id = 3,
            title = "Android MVVM 아키텍처",
            content = "MVVM 패턴으로 코드를 구조화하면 테스트와 유지보수가 쉬워집니다. ViewModel과 LiveData/StateFlow를 활용해봅시다.",
            createdAt = "2025-10-05T14:20:00",
            updatedAt = "2025-10-05T14:20:00"
        )
    )
    private val _postResult = MutableStateFlow<Result<RecordPost>?>(null)
    val postResult: StateFlow<Result<RecordPost>?> = _postResult

    @RequiresApi(Build.VERSION_CODES.O)
    fun createPost(title: String, content: String) {
        viewModelScope.launch {
            val result = dailyRecordPostUseCase(title, content)
            _postResult.value = result
        }
    }


}
