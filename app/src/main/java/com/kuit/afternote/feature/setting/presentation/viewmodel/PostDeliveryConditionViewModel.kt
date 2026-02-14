package com.kuit.afternote.feature.setting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.data.local.PostDeliveryConditionPreferences
import com.kuit.afternote.feature.setting.presentation.model.DeliveryMethodOption
import com.kuit.afternote.feature.setting.presentation.model.TriggerConditionOption
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val CODE_AUTOMATIC_TRANSFER = "AUTOMATIC_TRANSFER"
private const val CODE_RECEIVER_APPROVAL = "RECEIVER_APPROVAL_TRANSFER"
private const val CODE_APP_INACTIVITY = "APP_INACTIVITY"
private const val CODE_SPECIFIC_DATE = "SPECIFIC_DATE"
private const val CODE_RECEIVER_REQUEST = "RECEIVER_REQUEST"

data class PostDeliveryConditionState(
    val selectedDeliveryMethod: DeliveryMethodOption = DeliveryMethodOption.AutomaticTransfer,
    val selectedTriggerCondition: TriggerConditionOption = TriggerConditionOption.AppInactivity,
    val selectedDate: LocalDate? = null
)

/**
 * Contract for PostDeliveryConditionScreen so Preview can use a Fake without Hilt.
 */
interface PostDeliveryConditionViewModelContract {
    val state: StateFlow<PostDeliveryConditionState>
    fun onDeliveryMethodSelected(option: DeliveryMethodOption)
    fun onTriggerConditionSelected(option: TriggerConditionOption)
    fun onDateSelected(date: LocalDate?)
}

/**
 * 사후 전달 조건 화면 상태. 로컬에 저장된 값을 로드하고 변경 시 저장합니다.
 */
@HiltViewModel
class PostDeliveryConditionViewModel
@Inject
constructor(
    private val preferences: PostDeliveryConditionPreferences
) : ViewModel(), PostDeliveryConditionViewModelContract {

    private val _state = MutableStateFlow(PostDeliveryConditionState())
    override val state: StateFlow<PostDeliveryConditionState> = _state.asStateFlow()

    init {
        viewModelScope.launch { loadFromPreferences() }
    }

    private suspend fun loadFromPreferences() {
        val deliveryCode = preferences.getDeliveryMethod() ?: CODE_AUTOMATIC_TRANSFER
        val triggerCode = preferences.getTriggerCondition() ?: CODE_APP_INACTIVITY
        val dateIso = preferences.getTriggerDateIso()
        _state.update {
            it.copy(
                selectedDeliveryMethod = deliveryCodeToOption(deliveryCode),
                selectedTriggerCondition = triggerCodeToOption(triggerCode),
                selectedDate = dateIso?.let { iso -> LocalDate.parse(iso) }
            )
        }
    }

    override fun onDeliveryMethodSelected(option: DeliveryMethodOption) {
        _state.update { it.copy(selectedDeliveryMethod = option) }
        viewModelScope.launch { persistState() }
    }

    override fun onTriggerConditionSelected(option: TriggerConditionOption) {
        _state.update { it.copy(selectedTriggerCondition = option) }
        viewModelScope.launch { persistState() }
    }

    override fun onDateSelected(date: LocalDate?) {
        _state.update { it.copy(selectedDate = date) }
        viewModelScope.launch { persistState() }
    }

    private suspend fun persistState() {
        val s = _state.value
        preferences.save(
            deliveryMethod = optionToDeliveryCode(s.selectedDeliveryMethod),
            triggerCondition = optionToTriggerCode(s.selectedTriggerCondition),
            triggerDateIso = s.selectedDate?.toString()
        )
    }

    private fun optionToDeliveryCode(option: DeliveryMethodOption): String =
        when (option) {
            is DeliveryMethodOption.AutomaticTransfer -> CODE_AUTOMATIC_TRANSFER
            is DeliveryMethodOption.ReceiverApprovalTransfer -> CODE_RECEIVER_APPROVAL
        }

    private fun deliveryCodeToOption(code: String): DeliveryMethodOption =
        when (code) {
            CODE_RECEIVER_APPROVAL -> DeliveryMethodOption.ReceiverApprovalTransfer
            else -> DeliveryMethodOption.AutomaticTransfer
        }

    private fun optionToTriggerCode(option: TriggerConditionOption): String =
        when (option) {
            is TriggerConditionOption.AppInactivity -> CODE_APP_INACTIVITY
            is TriggerConditionOption.SpecificDate -> CODE_SPECIFIC_DATE
            is TriggerConditionOption.ReceiverRequest -> CODE_RECEIVER_REQUEST
        }

    private fun triggerCodeToOption(code: String): TriggerConditionOption =
        when (code) {
            CODE_SPECIFIC_DATE -> TriggerConditionOption.SpecificDate
            CODE_RECEIVER_REQUEST -> TriggerConditionOption.ReceiverRequest
            else -> TriggerConditionOption.AppInactivity
        }
}
