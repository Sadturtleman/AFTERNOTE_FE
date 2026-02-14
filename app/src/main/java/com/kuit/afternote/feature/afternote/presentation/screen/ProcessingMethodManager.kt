package com.kuit.afternote.feature.afternote.presentation.screen

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodItem

/**
 * Holds processing method list state and actions for the afternote edit screen.
 * Extracted from [AfternoteEditState] to keep function count under the detekt threshold.
 */
@Stable
class ProcessingMethodManager {

    var processingMethods by mutableStateOf<List<ProcessingMethodItem>>(emptyList())
        private set
    var galleryProcessingMethods by mutableStateOf<List<ProcessingMethodItem>>(emptyList())
        private set

    fun replaceProcessingMethods(list: List<ProcessingMethodItem>) {
        processingMethods = list
    }

    fun replaceGalleryProcessingMethods(list: List<ProcessingMethodItem>) {
        galleryProcessingMethods = list
    }

    fun deleteProcessingMethod(itemId: String) {
        processingMethods = processingMethods.filter { it.id != itemId }
    }

    fun addProcessingMethod(text: String) {
        val newItem = ProcessingMethodItem(
            id = (processingMethods.size + 1).toString(),
            text = text
        )
        processingMethods = processingMethods + newItem
    }

    fun deleteGalleryProcessingMethod(itemId: String) {
        galleryProcessingMethods = galleryProcessingMethods.filter { it.id != itemId }
    }

    fun addGalleryProcessingMethod(text: String) {
        val newItem = ProcessingMethodItem(
            id = (galleryProcessingMethods.size + 1).toString(),
            text = text
        )
        galleryProcessingMethods = galleryProcessingMethods + newItem
    }
}
