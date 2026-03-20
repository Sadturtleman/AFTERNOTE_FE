package com.kuit.afternote.presentation.uimodel

interface Step {
    val value: Int

    fun previous(): Step?
}
