package com.kuit.afternote.feature.timeletter.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface DraftLetterDao {
    @Query("SELECT * FROM draft_letters ORDER BY createdAt DESC")
    fun getAllDraftLetters(): Flow<List<DraftLetterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraftLetter(draftLetter: DraftLetterEntity): Long

    @Query("DELETE FROM draft_letters WHERE id = :id")
    suspend fun deleteDraftLetter(id: Long)

    @Query("DELETE FROM draft_letters")
    suspend fun deleteAllDraftLetters()

    @Query("SELECT * FROM draft_letters WHERE id = :id")
    suspend fun getDraftLetterById(id: Long): DraftLetterEntity?
}
