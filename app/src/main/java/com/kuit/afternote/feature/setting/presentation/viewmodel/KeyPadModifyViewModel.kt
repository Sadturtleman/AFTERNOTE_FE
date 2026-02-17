package com.kuit.afternote.feature.setting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.setting.data.repository.iface.SecurityRepository
import com.kuit.afternote.feature.setting.presentation.uimodel.KeyAction
import com.kuit.afternote.feature.setting.presentation.uimodel.PasswordStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class KeyPadModifyViewModel
@Inject
constructor(
    private val securityRepository: SecurityRepository
) : ViewModel() {
    private val _currentStep = MutableStateFlow<PasswordStep>(PasswordStep.Setup)
    val currentStep = _currentStep.asStateFlow()

    private var firstPassword = ""
    private var secondPassword = ""

    private val _inputCode = MutableStateFlow("")
    val inputCode: StateFlow<String> = _inputCode.asStateFlow()

    fun handleKeyAction(action: KeyAction) {
        when (action) {
            is KeyAction.Number -> {
                if (_inputCode.value.length < 4) {
                    _inputCode.value += action.value
                }
            }

            is KeyAction.Confirm -> {
                validateStep()
            }

            is KeyAction.Delete -> {
                _inputCode.value = _inputCode.value.dropLast(1)
            }
        }
    }

    private fun validateStep() {
        when (_currentStep.value) {
            is PasswordStep.Setup -> {
                firstPassword = inputCode.value
                _inputCode.value = ""
                _currentStep.value = PasswordStep.Confirm
            }

            is PasswordStep.Confirm -> {
                secondPassword = _inputCode.value
                if (secondPassword == firstPassword) {
                    _currentStep.value = PasswordStep.Success
                    savePasswordToLocal(secondPassword)
                } else {
                    _inputCode.value = ""
                }
            }

            else -> {}
        }
    }

    private fun savePasswordToLocal(password: String) {
        viewModelScope.launch {
            securityRepository.savePassword(password)
                .onSuccess {
                    _currentStep.value = PasswordStep.Success
                    clearMemory() // 저장 완료 후 메모리상의 평문 비밀번호 파기
                }
                .onFailure {
                    // TODO: 저장 실패 처리 (예: 여유 공간 부족, 키스토어 오류 등)
                    _inputCode.value = ""
                }
        }
    }

    private fun clearMemory() {
        firstPassword = ""
        secondPassword = ""
        _inputCode.value = ""
    }

    override fun onCleared() {
        clearMemory()
        super.onCleared()
    }
}
