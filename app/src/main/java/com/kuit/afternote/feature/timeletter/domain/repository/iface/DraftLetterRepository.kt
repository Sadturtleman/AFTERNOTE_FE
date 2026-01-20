package com.kuit.afternote.feature.timeletter.domain.repository.iface

import com.kuit.afternote.feature.timeletter.domain.entity.DraftLetter
import kotlinx.coroutines.flow.Flow

interface DraftLetterRepository {
    fun getDraftLetters(): Flow<List<DraftLetter>>
    suspend fun saveDraftLetter(draftLetter: DraftLetter): Long
    suspend fun deleteDraftLetter(id: Long)
    suspend fun deleteAllDraftLetters()
    suspend fun getDraftLetterById(id: Long): DraftLetter?
}
