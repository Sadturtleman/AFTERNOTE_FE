package com.kuit.afternote.feature.afternote.presentation.component.edit.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.Label
import com.kuit.afternote.core.ui.component.LabelStyle
import com.kuit.afternote.feature.afternote.presentation.component.edit.afternoteeditreceiver.AfternoteEditReceiverList
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiverCallbacks
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiverSection
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 수신자 지정 섹션 (Figma 수신자 지정 컴포넌트).
 * 라벨 "수신자 지정"과 수신자 리스트를 표시합니다.
 */
@Composable
fun RecipientDesignationSection(
    modifier: Modifier = Modifier,
    section: AfternoteEditReceiverSection
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Label(
            text = "수신자 지정",
            isRequired = true,
            style = LabelStyle(requiredDotOffsetY = 3.dp)
        )
        Spacer(modifier = Modifier.height(9.dp))
        AfternoteEditReceiverList(
            afternoteEditReceivers = section.afternoteEditReceivers,
            events = section.callbacks
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipientDesignationSectionPreview() {
    AfternoteTheme {
        RecipientDesignationSection(
            section = AfternoteEditReceiverSection(
                afternoteEditReceivers = emptyList(),
                callbacks = AfternoteEditReceiverCallbacks()
            )
        )
    }
}
