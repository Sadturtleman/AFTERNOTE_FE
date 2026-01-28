package com.kuit.afternote.feature.timeletter.domain.repository.iface

import com.kuit.afternote.feature.timeletter.domain.entity.DraftLetter
import kotlinx.coroutines.flow.Flow
//TODO: 이것도 draft인지 아닌지를 상태로 받아서 나중에 다 지워야 됨.
interface DraftLetterRepository {
    fun getDraftLetters(): Flow<List<DraftLetter>>
    suspend fun saveDraftLetter(draftLetter: DraftLetter): Long
    suspend fun deleteDraftLetter(id: Long)
    suspend fun deleteAllDraftLetters()
    suspend fun getDraftLetterById(id: Long): DraftLetter?
}
