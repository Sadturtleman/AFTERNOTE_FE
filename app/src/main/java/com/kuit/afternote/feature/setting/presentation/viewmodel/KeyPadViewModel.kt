package com.kuit.afternote.feature.setting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.kuit.afternote.feature.setting.presentation.uimodel.KeyAction
import com.kuit.afternote.feature.setting.presentation.uimodel.PasswordStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class KeyPadViewModel
    @Inject
    constructor() : ViewModel() {
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
                    } else {
                        _inputCode.value = ""
                    }
                }
                else -> {}
            }
        }
    }
