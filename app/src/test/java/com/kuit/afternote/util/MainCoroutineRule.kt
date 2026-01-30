package com.kuit.afternote.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit Rule: ViewModel 단위 테스트에서 [Dispatchers.Main]을 [Dispatchers.Unconfined]로 교체.
 *
 * Unconfined는 작업을 즉시 실행하므로, runTest { ... advanceUntilIdle() } 로
 * viewModelScope 코루틴이 완료된 뒤 검증할 수 있다.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineRule : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
