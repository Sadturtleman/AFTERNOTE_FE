package com.kuit.afternote.core.domain.provider

import com.kuit.afternote.core.dummy.receiver.AfternoteListItemSeed
import com.kuit.afternote.feature.afternote.presentation.author.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem
import javax.inject.Inject

/**
 * Real implementation. Returns empty/placeholder until API is available.
 */
class RealReceiverDataProvider
    @Inject
    constructor() : ReceiverDataProvider {
        override fun getReceiverList(): List<AfternoteEditReceiver> = emptyList()

        override fun getDefaultReceiverTitle(): String = ""

        override fun getAfternoteListSeedsForReceiverList(): List<AfternoteListItemSeed> = emptyList()

        override fun getAfternoteListSeedsForReceiverDetail(): List<AfternoteListItemSeed> = emptyList()

        override fun getTimeLetterItemsForPreview(): List<TimeLetterItem> = emptyList()
    }
