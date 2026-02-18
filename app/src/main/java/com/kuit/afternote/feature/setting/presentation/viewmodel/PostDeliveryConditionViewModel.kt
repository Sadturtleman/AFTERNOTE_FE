package com.kuit.afternote.feature.setting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.data.local.PostDeliveryConditionPreferences
import com.kuit.afternote.feature.setting.presentation.model.DeliveryMethodOption
import com.kuit.afternote.feature.setting.presentation.model.TriggerConditionOption
import com.kuit.afternote.feature.user.domain.model.DeliveryConditionType
import com.kuit.afternote.feature.user.domain.usecase.GetDeliveryConditionUseCase
import com.kuit.afternote.feature.user.domain.usecase.UpdateDeliveryConditionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val CODE_AUTOMATIC_TRANSFER = "AUTOMATIC_TRANSFER"
private const val CODE_RECEIVER_APPROVAL = "RECEIVER_APPROVAL_TRANSFER"
private const val CODE_APP_INACTIVITY = "APP_INACTIVITY"
private const val CODE_SPECIFIC_DATE = "SPECIFIC_DATE"
private const val CODE_RECEIVER_REQUEST = "RECEIVER_REQUEST"
private const val DEFAULT_INACTIVITY_DAYS = 365

data class PostDeliveryConditionState(
    val selectedDeliveryMethod: DeliveryMethodOption = DeliveryMethodOption.AutomaticTransfer,
    val selectedTriggerCondition: TriggerConditionOption = TriggerConditionOption.AppInactivity,
    val selectedDate: LocalDate? = null,
    val inactivityPeriodDays: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
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
 * 사후 전달 조건 화면. API로 전달 조건을 조회/수정하고, 전달 방식은 로컬에만 저장합니다.
 */
@HiltViewModel
class PostDeliveryConditionViewModel
@Inject
constructor(
    private val getDeliveryConditionUseCase: GetDeliveryConditionUseCase,
    private val updateDeliveryConditionUseCase: UpdateDeliveryConditionUseCase,
    private val preferences: PostDeliveryConditionPreferences
) : ViewModel(), PostDeliveryConditionViewModelContract {

    private val _state = MutableStateFlow(PostDeliveryConditionState())
    override val state: StateFlow<PostDeliveryConditionState> = _state.asStateFlow()

    init {
        viewModelScope.launch { loadDeliveryCondition() }
    }

    private suspend fun loadDeliveryCondition() {
        _state.update { it.copy(isLoading = true, errorMessage = null) }
                getDeliveryConditionUseCase()
            .onSuccess { condition ->
                val deliveryCode = preferences.getDeliveryMethod() ?: CODE_AUTOMATIC_TRANSFER
                _state.update {
                    it.copy(
                        selectedDeliveryMethod = deliveryCodeToOption(deliveryCode),
                        selectedTriggerCondition = conditionTypeToTriggerOption(condition.conditionType),
                        selectedDate = condition.specificDate?.let { iso -> LocalDate.parse(iso) },
                        inactivityPeriodDays = condition.inactivityPeriodDays,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
            .onFailure { e ->
                _state.update { state -> state.copy(isLoading = false, errorMessage = e.message) }
                loadFromPreferencesAsFallback()
            }
    }

    private suspend fun loadFromPreferencesAsFallback() {
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
    }

    override fun onTriggerConditionSelected(option: TriggerConditionOption) {
        _state.update { it.copy(selectedTriggerCondition = option, errorMessage = null) }
        viewModelScope.launch {
            updateDeliveryConditionApi()
            persistDeliveryMethodOnly()
        }
    }

    override fun onDateSelected(date: LocalDate?) {
        _state.update { it.copy(selectedDate = date, errorMessage = null) }
        viewModelScope.launch { updateDeliveryConditionApi() }
    }

    private suspend fun updateDeliveryConditionApi() {
        val s = _state.value
        val conditionType = triggerOptionToConditionType(s.selectedTriggerCondition)
        val inactivityPeriodDays = when (conditionType) {
            DeliveryConditionType.INACTIVITY -> s.inactivityPeriodDays ?: DEFAULT_INACTIVITY_DAYS
            else -> null
        }
        val specificDate = when (conditionType) {
            DeliveryConditionType.SPECIFIC_DATE -> s.selectedDate?.toString()
            else -> null
        }
        updateDeliveryConditionUseCase(
            conditionType = conditionType,
            inactivityPeriodDays = inactivityPeriodDays,
            specificDate = specificDate
        )
            .onSuccess { condition ->
                _state.update {
                    it.copy(
                        inactivityPeriodDays = condition.inactivityPeriodDays,
                        errorMessage = null
                    )
                }
                persistTriggerToPreferences()
            }
            .onFailure { e ->
                _state.update { state ->
                    state.copy(errorMessage = e.message ?: "전달 조건 저장에 실패했습니다.")
                }
            }
    }

    private suspend fun persistDeliveryMethodOnly() {
        val s = _state.value
        preferences.save(
            deliveryMethod = optionToDeliveryCode(s.selectedDeliveryMethod),
            triggerCondition = optionToTriggerCode(s.selectedTriggerCondition),
            triggerDateIso = s.selectedDate?.toString()
        )
    }

    private suspend fun persistTriggerToPreferences() {
        val s = _state.value
        preferences.save(
            deliveryMethod = optionToDeliveryCode(s.selectedDeliveryMethod),
            triggerCondition = optionToTriggerCode(s.selectedTriggerCondition),
            triggerDateIso = s.selectedDate?.toString()
        )
    }

    private fun conditionTypeToTriggerOption(type: DeliveryConditionType): TriggerConditionOption =
        when (type) {
            DeliveryConditionType.NONE -> TriggerConditionOption.AppInactivity
            DeliveryConditionType.DEATH_CERTIFICATE -> TriggerConditionOption.ReceiverRequest
            DeliveryConditionType.INACTIVITY -> TriggerConditionOption.AppInactivity
            DeliveryConditionType.SPECIFIC_DATE -> TriggerConditionOption.SpecificDate
        }

    private fun triggerOptionToConditionType(option: TriggerConditionOption): DeliveryConditionType =
        when (option) {
            TriggerConditionOption.AppInactivity -> DeliveryConditionType.INACTIVITY
            TriggerConditionOption.SpecificDate -> DeliveryConditionType.SPECIFIC_DATE
            TriggerConditionOption.ReceiverRequest -> DeliveryConditionType.DEATH_CERTIFICATE
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
