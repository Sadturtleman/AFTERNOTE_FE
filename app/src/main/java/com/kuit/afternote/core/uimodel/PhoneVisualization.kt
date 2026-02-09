package com.kuit.afternote.core.uimodel

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert
class PhoneNumberVisualTransformation : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        // 숫자만 남긴 상태라고 가정하고 처리
        val rawText = this.asCharSequence()
        if (rawText.length <= 3) return

        // 010-1234-5678 (11자리) 또는 010-123-4567 (10자리) 대응
        if (rawText.length <= 7) {
            insert(3, "-")
        } else {
            insert(3, "-")
            // 11자리일 때는 7번째 다음에, 10자리일 때는 6번째 다음에 대시
            val dashIndex = if (rawText.length <= 10) 7 else 8
            insert(dashIndex, "-")
        }
    }
}
