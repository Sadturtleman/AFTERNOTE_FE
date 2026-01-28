package com.kuit.afternote.feature.timeletter.data.repository

import com.kuit.afternote.feature.timeletter.domain.entity.DraftLetter
import com.kuit.afternote.feature.timeletter.domain.repository.iface.DraftLetterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * [DraftLetterRepository] 구현체.
 *
 * 현재 Room/API 연동 전이므로 스텁 구현. 추후 DataSource(DAO 등) 주입 후 실제 저장·조회 로직으로 교체.
 */
class DraftLetterRepositoryImpl
    @Inject
    constructor() : DraftLetterRepository {
        override fun getDraftLetters(): Flow<List<DraftLetter>> = flow { emit(emptyList()) }

        override suspend fun saveDraftLetter(draftLetter: DraftLetter): Long = 0L

        override suspend fun deleteDraftLetter(id: Long) = Unit

        override suspend fun deleteAllDraftLetters() = Unit

        override suspend fun getDraftLetterById(id: Long): DraftLetter? = null
    }
