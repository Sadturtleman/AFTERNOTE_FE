package com.kuit.afternote.feature.afternote.data.mapper.response

import com.kuit.afternote.feature.afternote.data.dto.response.AfternoteListResponse
import com.kuit.afternote.feature.afternote.data.mapper.toDomainList
import com.kuit.afternote.feature.afternote.domain.model.PagedAfternotes

fun AfternoteListResponse.toPagedNotes() =
    PagedAfternotes(
        items = content.toDomainList(),
        hasNext = hasNext,
    )
