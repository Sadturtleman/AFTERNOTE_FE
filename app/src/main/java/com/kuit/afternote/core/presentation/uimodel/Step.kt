package com.kuit.afternote.core.presentation.uimodel

interface Step {
    val value: Int

    fun previous(): Step?
}
