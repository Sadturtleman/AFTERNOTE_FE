package com.kuit.afternote.core.presentation.shared.model
interface Step {
    val value: Int

    fun previous(): Step?
}
