// K2 false positive: state assigned in DatePickerDialog lambdas is read at recomposition (KT-78881).
@file:Suppress("AssignedValueIsNeverRead")

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.core.ui.component.Label
import com.kuit.afternote.core.ui.component.SelectableRadioCard
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodOption
import com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod.OptionRadioCardContent
import com.kuit.afternote.feature.setting.presentation.component.DatePickerDialog
import com.kuit.afternote.feature.setting.presentation.component.SelectedDateText
import com.kuit.afternote.feature.setting.presentation.model.DeliveryMethodOption
import com.kuit.afternote.feature.setting.presentation.model.TriggerConditionOption
import com.kuit.afternote.feature.setting.presentation.viewmodel.PostDeliveryConditionState
import com.kuit.afternote.feature.setting.presentation.viewmodel.PostDeliveryConditionViewModel
import com.kuit.afternote.feature.setting.presentation.viewmodel.PostDeliveryConditionViewModelContract
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray3
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

@Composable
fun PostDeliveryConditionScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: PostDeliveryConditionViewModelContract = hiltViewModel<PostDeliveryConditionViewModel>()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val selectedDeliveryMethod = state.selectedDeliveryMethod
    val selectedTriggerCondition = state.selectedTriggerCondition
    val selectedDate = state.selectedDate
    var showDatePickerDialog by remember { mutableStateOf(false) }

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
                onBackClick = onBackClick
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
                        val option = object : ProcessingMethodOption {
                            override val title: String = method.title
                            override val description: String = method.description
                        }
                        val isSelected = selectedDeliveryMethod == method
                        SelectableRadioCard(
                            selected = isSelected,
                            onClick = { viewModel.onDeliveryMethodSelected(method) },
                            modifier = Modifier.fillMaxWidth(),
                            content = {
                                OptionRadioCardContent(
                                    option = option,
                                    selected = isSelected
                                )
                            }
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
                    SelectedDateText(date = selectedDate)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    triggerConditions.forEach { condition ->
                        val option = object : ProcessingMethodOption {
                            override val title: String = condition.title
                            override val description: String = condition.description
                        }
                        val isSelected = selectedTriggerCondition == condition
                        SelectableRadioCard(
                            selected = isSelected,
                            onClick = {
                                viewModel.onTriggerConditionSelected(condition)
                                if (condition == TriggerConditionOption.SpecificDate) {
                                    showDatePickerDialog = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            content = {
                                OptionRadioCardContent(
                                    option = option,
                                    selected = isSelected
                                )
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
                viewModel.onDateSelected(date)
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

private class FakePostDeliveryConditionViewModel : PostDeliveryConditionViewModelContract {
    override val state: StateFlow<PostDeliveryConditionState> =
        MutableStateFlow(
            PostDeliveryConditionState(
                selectedDeliveryMethod = DeliveryMethodOption.AutomaticTransfer,
                selectedTriggerCondition = TriggerConditionOption.AppInactivity,
                selectedDate = null
            )
        )

    override fun onDeliveryMethodSelected(option: DeliveryMethodOption) {
        // No-op: Fake for Preview only; no persistence.
    }

    override fun onTriggerConditionSelected(option: TriggerConditionOption) {
        // No-op: Fake for Preview only; no persistence.
    }

    override fun onDateSelected(date: LocalDate?) {
        // No-op: Fake for Preview only; no persistence.
    }
}

@Preview(showBackground = true)
@Composable
private fun PostDeliveryConditionScreenPreview() {
    AfternoteTheme {
        PostDeliveryConditionScreen(
            onBackClick = {},
            viewModel = remember { FakePostDeliveryConditionViewModel() }
        )
    }
}
