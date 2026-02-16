package com.kuit.afternote.data.provider

import com.kuit.afternote.core.dummy.receiver.ReceiverDummies
import com.kuit.afternote.domain.provider.ReceiverDataProvider
import javax.inject.Inject

/**
 * Fake implementation. Only place that imports and uses ReceiverDummies.
 */
class FakeReceiverDataProvider @Inject constructor() : ReceiverDataProvider {
    override fun getReceiverList() = ReceiverDummies.receiverList
    override fun getDefaultReceiverTitle(): String = ReceiverDummies.defaultReceiverTitleForDev()
    override fun getAfternoteListSeedsForReceiverList() = ReceiverDummies.defaultAfternoteListSeedsForReceiverList()
    override fun getAfternoteListSeedsForReceiverDetail() = ReceiverDummies.defaultAfternoteListSeedsForReceiverDetail()
    override fun getTimeLetterItemsForPreview() = ReceiverDummies.sampleTimeLetterItemsForPreview
}
