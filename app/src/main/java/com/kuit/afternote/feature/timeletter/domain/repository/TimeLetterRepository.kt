package com.kuit.afternote.feature.timeletter.domain.repository

import com.kuit.afternote.feature.timeletter.domain.model.TimeLetter
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterList
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterMedia
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterMediaType
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterStatus

/**
 * TimeLetter 도메인 Repository 인터페이스. (스웨거 기준)
 */
interface TimeLetterRepository {

    suspend fun getTimeLetters(): Result<TimeLetterList>

    suspend fun createTimeLetter(
        title: String?,
        content: String?,
        sendAt: String?,
        status: TimeLetterStatus,
        mediaList: List<Pair<TimeLetterMediaType, String>>?
    ): Result<TimeLetter>

    suspend fun getTimeLetter(timeLetterId: Long): Result<TimeLetter>

    suspend fun updateTimeLetter(
        timeLetterId: Long,
        title: String?,
        content: String?,
        sendAt: String?,
        status: TimeLetterStatus?,
        mediaList: List<Pair<TimeLetterMediaType, String>>?
    ): Result<TimeLetter>

    suspend fun deleteTimeLetters(timeLetterIds: List<Long>): Result<Unit>

    suspend fun getTemporaryTimeLetters(): Result<TimeLetterList>

    suspend fun deleteAllTemporary(): Result<Unit>
}
