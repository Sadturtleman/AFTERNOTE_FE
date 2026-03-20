package com.kuit.afternote.feature.afternote.presentation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem

/**
 * Holds processing method list state and actions for the afternote edit screen.
 * Extracted from [com.kuit.afternote.feature.afternote.presentation.edit.AfternoteEditState] to keep function count under the detekt threshold.
 */
@Stable
class ProcessingMethodManager {
    var processingMethods by mutableStateOf<List<com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem>>(
        emptyList(),
    )
        private set
    var galleryProcessingMethods by mutableStateOf<List<com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem>>(
        emptyList(),
    )
        private set

    fun replaceProcessingMethods(list: List<com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem>) {
        processingMethods = list
    }

    fun replaceGalleryProcessingMethods(list: List<com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem>) {
        galleryProcessingMethods = list
    }

    fun editProcessingMethod(
        itemId: String,
        newText: String,
    ) {
        processingMethods =
            processingMethods.map { item ->
                if (item.id == itemId) item.copy(text = newText) else item
            }
    }

    fun deleteProcessingMethod(itemId: String) {
        processingMethods = processingMethods.filter { it.id != itemId }
    }

    fun addProcessingMethod(text: String) {
        val newItem =
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem(
                id = (processingMethods.size + 1).toString(),
                text = text,
            )
        processingMethods = processingMethods + newItem
    }

    fun editGalleryProcessingMethod(
        itemId: String,
        newText: String,
    ) {
        galleryProcessingMethods =
            galleryProcessingMethods.map { item ->
                if (item.id == itemId) item.copy(text = newText) else item
            }
    }

    fun deleteGalleryProcessingMethod(itemId: String) {
        galleryProcessingMethods = galleryProcessingMethods.filter { it.id != itemId }
    }

    fun addGalleryProcessingMethod(text: String) {
        val newItem =
            _root_ide_package_.com.kuit.afternote.feature.afternote.presentation.edit.model.ProcessingMethodItem(
                id = (galleryProcessingMethods.size + 1).toString(),
                text = text,
            )
        galleryProcessingMethods = galleryProcessingMethods + newItem
    }
}
