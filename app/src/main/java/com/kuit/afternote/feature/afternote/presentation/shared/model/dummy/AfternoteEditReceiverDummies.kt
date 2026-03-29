package com.kuit.afternote.feature.afternote.presentation.shared.model.dummy

import com.kuit.afternote.feature.afternote.presentation.author.edit.model.AfternoteEditReceiver

/** Fake receiver rows for edit / provider previews (not core — avoids core → feature coupling). */
object AfternoteEditReceiverDummies {
    val defaultList: List<AfternoteEditReceiver> =
        listOf(
            AfternoteEditReceiver(
                id = "receiver_1",
                name = "김지은",
                label = "딸",
            ),
            AfternoteEditReceiver(
                id = "receiver_2",
                name = "김혜성",
                label = "아들",
            ),
            AfternoteEditReceiver(
                id = "receiver_3",
                name = "박서연",
                label = "조카",
            ),
            AfternoteEditReceiver(
                id = "receiver_4",
                name = "황은주",
                label = "언니",
            ),
            AfternoteEditReceiver(
                id = "receiver_5",
                name = "황은경",
                label = "동생",
            ),
        )
}
