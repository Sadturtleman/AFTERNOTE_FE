package com.kuit.afternote.feature.setting.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.mainpage.presentation.component.edit.processingmethod.ProcessingMethodRadioButton
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B2

/**
 * 사후 전달 조건 옵션
 */
sealed interface PostDeliveryConditionOption {
    val title: String
    val description: String

    data object ImmediateTransfer : PostDeliveryConditionOption {
        override val title: String = "즉시 전달"
        override val description: String = "사망 확인 후 즉시 수신자에게 정보를 전달합니다."
    }

    data object DelayedTransfer : PostDeliveryConditionOption {
        override val title: String = "지연 전달"
        override val description: String = "사망 확인 후 일정 기간이 지난 후 수신자에게 정보를 전달합니다."
    }

    data object ConditionalTransfer : PostDeliveryConditionOption {
        override val title: String = "조건부 전달"
        override val description: String = "특정 조건이 충족된 후 수신자에게 정보를 전달합니다."
    }
}

@Composable
fun PostDeliveryConditionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    var selectedCondition by remember {
        mutableStateOf<PostDeliveryConditionOption>(PostDeliveryConditionOption.ImmediateTransfer)
    }

    val conditions = listOf(
        PostDeliveryConditionOption.ImmediateTransfer,
        PostDeliveryConditionOption.DelayedTransfer,
        PostDeliveryConditionOption.ConditionalTransfer
    )

    Scaffold(
        topBar = {
            TopBar(
                title = "사후 전달 조건",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                conditions.forEach { condition ->
                    ProcessingMethodRadioButton(
                        modifier = Modifier.fillMaxWidth(),
                        option = object : com.kuit.afternote.feature.mainpage.presentation.component.edit.model.ProcessingMethodOption {
                            override val title: String = condition.title
                            override val description: String = condition.description
                        },
                        selected = selectedCondition == condition,
                        onClick = { selectedCondition = condition }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            ClickButton(
                color = B2,
                title = "저장",
                onButtonClick = {
                    // TODO: 사후 전달 조건 저장 API 호출
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PostDeliveryConditionScreenPreview() {
    AfternoteTheme {
        PostDeliveryConditionScreen(
            onBackClick = {}
        )
    }
}
