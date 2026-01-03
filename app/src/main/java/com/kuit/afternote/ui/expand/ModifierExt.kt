package com.kuit.afternote.ui.expand

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput

/**
 * 포커스를 해제하는 Modifier 확장 함수 (강력한 버전)
 *
 * TextField 외부 클릭 시 포커스를 자동으로 해제합니다.
 * PointerEventPass.Initial을 사용하여 자식 뷰(clickable 요소)가 이벤트를 받기 전에
 * 먼저 포커스를 해제하므로, 버튼이나 리스트 아이템 등 clickable 요소를 클릭해도
 * 포커스가 해제됩니다.
 *
 * 이벤트를 소비하지 않으므로 하위 컴포넌트의 클릭 이벤트를 방해하지 않습니다.
 *
 * @param focusManager 포커스 매니저 (LocalFocusManager.current)
 */
fun Modifier.addFocusCleaner(focusManager: FocusManager): Modifier =
    this.pointerInput(Unit) {
        awaitEachGesture {
            // Initial 패스에서 터치 다운을 감지 (자식 뷰가 이벤트를 받기 전)
            awaitFirstDown(pass = PointerEventPass.Initial)

            // 터치가 발생하면 포커스 해제
            focusManager.clearFocus()

            // 이벤트를 소비하지 않으므로 자식 뷰의 클릭도 정상 동작
            waitForUpOrCancellation(pass = PointerEventPass.Initial)
        }
    }
