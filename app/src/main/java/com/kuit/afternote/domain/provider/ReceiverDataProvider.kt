package com.kuit.afternote.domain.provider

import com.kuit.afternote.core.dummy.receiver.AfternoteListItemSeed
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem

/**
 * Provides data for receiver-related screens (list, afternote seeds, time letters for preview).
 * Implementation (real vs dummy-backed) is decided at DI; consumers use this interface only.
 */
interface ReceiverDataProvider {
    fun getReceiverList(): List<AfternoteEditReceiver>
    fun getDefaultReceiverTitleForDev(): String
    fun getAfternoteListSeedsForReceiverList(): List<AfternoteListItemSeed>
    fun getAfternoteListSeedsForReceiverDetail(): List<AfternoteListItemSeed>
    fun getTimeLetterItemsForPreview(): List<TimeLetterItem>
}
