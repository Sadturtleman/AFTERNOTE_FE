package com.kuit.afternote.core.uimodel

/**
 * Display model for a single row in the shared 애프터노트 list (writer main and receiver list).
 * Both features map their domain/UI models to this for a unified look.
 *
 * @param id unique id
 * @param serviceName label (e.g. "인스타그램", "갤러리")
 * @param date date string (e.g. "2025.11.26")
 * @param iconResId drawable resource id for the row icon
 */
data class AfternoteListDisplayItem(
    val id: String,
    val serviceName: String,
    val date: String,
    val iconResId: Int
)
