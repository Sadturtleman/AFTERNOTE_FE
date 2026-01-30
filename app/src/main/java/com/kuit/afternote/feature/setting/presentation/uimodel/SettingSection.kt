package com.kuit.afternote.feature.setting.presentation.uimodel

data class SettingItemData(
    val title: String,
    val status: String? = null,
    val onClick: () -> Unit = {}
)

data class SettingSection(
    val header: String?,
    val items: List<SettingItemData>
)
