package com.kuit.afternote.feature.afternote.domain.model

/**
 * One page of afternotes from GET /afternotes.
 *
 * @param items Items for this page
 * @param hasNext Whether more pages are available
 */
data class PagedAfternotes(
    val items: List<AfternoteItem>,
    val hasNext: Boolean
)
