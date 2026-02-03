package com.kuit.afternote.feature.setting.presentation.screen.postdelivery

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.core.ui.component.Label
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.ProcessingMethodOption
import com.kuit.afternote.feature.mainpage.presentation.component.edit.processingmethod.ProcessingMethodRadioButton
import com.kuit.afternote.feature.setting.presentation.component.DatePickerDialog
import com.kuit.afternote.feature.setting.presentation.component.SelectedDateText
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import java.time.LocalDate

/**
 * 정보 전달 방법 옵션 (첫 번째 섹션)
 */
sealed interface DeliveryMethodOption {
    val title: String
    val description: String

    data object AutomaticTransfer : DeliveryMethodOption {
        override val title: String = "자동 전달"
        override val description: String = "설정된 조건 충족 시, 별도의 요청 없이 지정한 수신자에게 자동으로 전달됩니다."
    }

    data object ReceiverApprovalTransfer : DeliveryMethodOption {
        override val title: String = "수신자 승인 후 전달"
        override val description: String = "수신자의 요청과 확인 절차를 거친 후, 안전하게 정보가 전달됩니다."
    }
}

/**
 * 정보 처리 방법 옵션 (두 번째 섹션 - 트리거 조건)
 */
sealed interface TriggerConditionOption {
    val title: String
    val description: String

    data object AppInactivity : TriggerConditionOption {
        override val title: String = "일정 기간 앱 미사용 시"
        override val description: String = "1년 이상의 기간 동안 앱 사용이 없을 경우 전달 조건이 자동으로 충족됩니다."
    }

    data object SpecificDate : TriggerConditionOption {
        override val title: String = "특정 날짜에 전달"
        override val description: String = "미리 정한 날짜에 맞추어 정보가 자동으로 전달됩니다."
    }

    data object ReceiverRequest : TriggerConditionOption {
        override val title: String = "수신자 요청 이후"
        override val description: String = "수신자의 요청이 접수된 후, 확인 절차를 거쳐 전달이 진행됩니다."
    }
}

@Composable
fun PostDeliveryConditionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit = {}
) {
    var selectedDeliveryMethod by remember {
        mutableStateOf<DeliveryMethodOption>(DeliveryMethodOption.AutomaticTransfer)
    }
    var selectedTriggerCondition by remember {
        mutableStateOf<TriggerConditionOption>(TriggerConditionOption.AppInactivity)
    }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val deliveryMethods = listOf(
        DeliveryMethodOption.AutomaticTransfer,
        DeliveryMethodOption.ReceiverApprovalTransfer
    )

    val triggerConditions = listOf(
        TriggerConditionOption.AppInactivity,
        TriggerConditionOption.SpecificDate,
        TriggerConditionOption.ReceiverRequest
    )

    BackHandler(onBack = onBackClick)
    Scaffold(
        containerColor = Gray1,
        topBar = {
            TopBar(
                title = "사후 전달 조건",
                onBackClick = onBackClick,
                onActionClick = onRegisterClick,
                actionText = "등록"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 첫 번째 섹션: 정보 처리 방법 (전달 방식)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Label(text = "정보 처리 방법")

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    deliveryMethods.forEach { method ->
                        ProcessingMethodRadioButton(
                            modifier = Modifier.fillMaxWidth(),
                            option = object : ProcessingMethodOption {
                                override val title: String = method.title
                                override val description: String = method.description
                            },
                            selected = selectedDeliveryMethod == method,
                            onClick = { selectedDeliveryMethod = method }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 두 번째 섹션: 정보 처리 방법 (트리거 조건)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Label(text = "정보 처리 방법")

                Spacer(modifier = Modifier.height(16.dp))

                // SelectedDateText - 특정 날짜가 선택되었을 때만 표시
                if (selectedTriggerCondition == TriggerConditionOption.SpecificDate && selectedDate != null) {
                    SelectedDateText(date = selectedDate!!)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    triggerConditions.forEach { condition ->
                        ProcessingMethodRadioButton(
                            modifier = Modifier.fillMaxWidth(),
                            option = object : ProcessingMethodOption {
                                override val title: String = condition.title
                                override val description: String = condition.description
                            },
                            selected = selectedTriggerCondition == condition,
                            onClick = {
                                selectedTriggerCondition = condition
                                // 특정 날짜에 전달 선택 시 다이얼로그 표시
                                if (condition == TriggerConditionOption.SpecificDate) {
                                    showDatePickerDialog = true
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 구분선
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                thickness = 1.dp,
                color = Gray3
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 하단 안내 문구
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                BulletItem(text = "해당 정보는 지정된 조건이 충족 시에만 전달됩니다.")
                BulletItem(text = "예기치 않은 상황에서도 설정된 방식에 따라 안전하게 전달됩니다.")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // DatePickerDialog
    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismiss = {
                showDatePickerDialog = false
            },
            onDateChanged = { date ->
                selectedDate = date
                showDatePickerDialog = false
            },
            initialDate = selectedDate ?: LocalDate.now()
        )
    }
}

@Composable
private fun BulletItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .size(4.dp)
                .background(color = Gray9, shape = CircleShape)
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                color = Gray9
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PostDeliveryConditionScreenPreview() {
    AfternoteTheme {
        PostDeliveryConditionScreen(
            onBackClick = {},
            onRegisterClick = {}
        )
    }
}
