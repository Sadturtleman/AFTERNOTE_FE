package com.kuit.afternote.feature.afternote.presentation.author.edit.ui.provider

import com.kuit.afternote.core.domain.receiver.provider.ReceiverDataProvider
import com.kuit.afternote.core.dummy.receiver.ReceiverDummies
import com.kuit.afternote.feature.afternote.presentation.shared.model.dummy.AfternoteEditReceiverDummies
import com.kuit.afternote.feature.timeletter.presentation.dummy.TimeLetterPreviewDummies
import javax.inject.Inject

/**
 * Fake [ReceiverDataProvider] backed by in-app dummy data. Lives in the afternote feature so
 * core stays free of feature UI models.
 */
class FakeReceiverDataProvider
    @Inject
    constructor() : ReceiverDataProvider {
        override fun getReceiverList() = AfternoteEditReceiverDummies.defaultList

        override fun getDefaultReceiverTitle(): String = ReceiverDummies.defaultReceiverTitle()

        override fun getAfternoteListSeedsForReceiverList() =
            ReceiverDummies.defaultAfternoteListSeedsForReceiverList()

        override fun getAfternoteListSeedsForReceiverDetail() =
            ReceiverDummies.defaultAfternoteListSeedsForReceiverDetail()

        override fun getTimeLetterItemsForPreview() = TimeLetterPreviewDummies.sampleItemsForPreview
    }
