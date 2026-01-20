package com.kuit.afternote.feature.timeletter.domain.usecase

import com.kuit.afternote.feature.timeletter.domain.entity.DraftLetter
import com.kuit.afternote.feature.timeletter.domain.repository.iface.DraftLetterRepository
import javax.inject.Inject

class SaveDraftLetterUseCase @Inject constructor(
    private val repository: DraftLetterRepository
){
    suspend operator fun invoke(draftLetter: DraftLetter): Long {
        return repository.saveDraftLetter(draftLetter)
    }
}
