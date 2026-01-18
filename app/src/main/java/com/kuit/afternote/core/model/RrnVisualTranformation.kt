package com.kuit.afternote.core.model

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class RrnVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text
        var out = ""

        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 5) out += " - " // 6번째 자리 뒤에 하이픈 추가
        }

        // 뒷자리 첫 번째 숫자 이후를 마스킹 처리 (이미지 컨셉 유지)
        val maskAfter = if (trimmed.length > 6) 1 else 0
        if (trimmed.length > 6) {
            out += "●".repeat(6) // 뒷자리 나머지 6개는 고정 마스킹
        }

        // 커서 위치 계산 (OffsetMapping)
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 6) return offset
                return offset + 3 // " - " 3글자 추가분 반영
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 6) return offset
                if (offset <= 10) return 7 // 하이픈 및 뒷자리 첫자 범위
                return 7
            }
        }

        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}
