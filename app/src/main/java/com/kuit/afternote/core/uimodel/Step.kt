package com.kuit.afternote.core.uimodel

interface Step {
    val value: Int

    fun previous(): Step?
}
