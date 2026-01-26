package com.kuit.afternote.feature.timeletter.data.repository

import android.util.Log
import com.kuit.afternote.data.remote.requireData
import com.kuit.afternote.feature.timeletter.data.api.TimeLetterApiService
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterCreateRequest
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterDeleteRequest
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterMediaRequest
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterMediaType as DtoMediaType
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterStatus as DtoStatus
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterUpdateRequest
import com.kuit.afternote.feature.timeletter.data.mapper.TimeLetterMapper
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetter
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterList
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterMediaType as DomainMediaType
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterStatus as DomainStatus
import com.kuit.afternote.feature.timeletter.domain.repository.TimeLetterRepository
import javax.inject.Inject

/**
 * TimeLetterRepository 구현체. (스웨거 기준)
 */
class TimeLetterRepositoryImpl @Inject constructor(
    private val api: TimeLetterApiService
) : TimeLetterRepository {

    override suspend fun getTimeLetters(): Result<TimeLetterList> = runCatching {
        Log.d(TAG, "getTimeLetters: calling API")
        val response = api.getTimeLetters()
        Log.d(TAG, "getTimeLetters: response=$response")
        TimeLetterMapper.toTimeLetterList(response.requireData())
    }

    override suspend fun createTimeLetter(
        title: String?,
        content: String?,
        sendAt: String?,
        status: DomainStatus,
        mediaList: List<Pair<DomainMediaType, String>>?
    ): Result<TimeLetter> = runCatching {
        Log.d(TAG, "createTimeLetter: title=$title, status=$status")
        val dtoStatus = when (status) {
            DomainStatus.DRAFT -> DtoStatus.DRAFT
            DomainStatus.SCHEDULED -> DtoStatus.SCHEDULED
            DomainStatus.SENT -> DtoStatus.SENT
        }
        val dtoMediaList = mediaList?.map { (type, url) ->
            val dtoType = when (type) {
                DomainMediaType.IMAGE -> DtoMediaType.IMAGE
                DomainMediaType.VIDEO -> DtoMediaType.VIDEO
                DomainMediaType.AUDIO -> DtoMediaType.AUDIO
            }
            TimeLetterMediaRequest(mediaType = dtoType, mediaUrl = url)
        }
        val response = api.createTimeLetter(
            TimeLetterCreateRequest(
                title = title,
                content = content,
                sendAt = sendAt,
                status = dtoStatus,
                mediaList = dtoMediaList
            )
        )
        Log.d(TAG, "createTimeLetter: response=$response")
        TimeLetterMapper.toTimeLetter(response.requireData())
    }

    override suspend fun getTimeLetter(timeLetterId: Long): Result<TimeLetter> = runCatching {
        Log.d(TAG, "getTimeLetter: timeLetterId=$timeLetterId")
        val response = api.getTimeLetter(timeLetterId = timeLetterId)
        Log.d(TAG, "getTimeLetter: response=$response")
        TimeLetterMapper.toTimeLetter(response.requireData())
    }

    override suspend fun updateTimeLetter(
        timeLetterId: Long,
        title: String?,
        content: String?,
        sendAt: String?,
        status: DomainStatus?,
        mediaList: List<Pair<DomainMediaType, String>>?
    ): Result<TimeLetter> = runCatching {
        Log.d(TAG, "updateTimeLetter: timeLetterId=$timeLetterId, title=$title, status=$status")
        val dtoStatus = status?.let {
            when (it) {
                DomainStatus.DRAFT -> DtoStatus.DRAFT
                DomainStatus.SCHEDULED -> DtoStatus.SCHEDULED
                DomainStatus.SENT -> DtoStatus.SENT
            }
        }
        val dtoMediaList = mediaList?.map { (type, url) ->
            val dtoType = when (type) {
                DomainMediaType.IMAGE -> DtoMediaType.IMAGE
                DomainMediaType.VIDEO -> DtoMediaType.VIDEO
                DomainMediaType.AUDIO -> DtoMediaType.AUDIO
            }
            TimeLetterMediaRequest(mediaType = dtoType, mediaUrl = url)
        }
        val response = api.updateTimeLetter(
            timeLetterId = timeLetterId,
            body = TimeLetterUpdateRequest(
                title = title,
                content = content,
                sendAt = sendAt,
                status = dtoStatus,
                mediaList = dtoMediaList
            )
        )
        Log.d(TAG, "updateTimeLetter: response=$response")
        TimeLetterMapper.toTimeLetter(response.requireData())
    }

    override suspend fun deleteTimeLetters(timeLetterIds: List<Long>): Result<Unit> = runCatching {
        Log.d(TAG, "deleteTimeLetters: timeLetterIds=$timeLetterIds")
        api.deleteTimeLetters(TimeLetterDeleteRequest(timeLetterIds = timeLetterIds))
        Log.d(TAG, "deleteTimeLetters: SUCCESS")
        Unit
    }

    override suspend fun getTemporaryTimeLetters(): Result<TimeLetterList> = runCatching {
        Log.d(TAG, "getTemporaryTimeLetters: calling API")
        val response = api.getTemporaryTimeLetters()
        Log.d(TAG, "getTemporaryTimeLetters: response=$response")
        TimeLetterMapper.toTimeLetterList(response.requireData())
    }

    override suspend fun deleteAllTemporary(): Result<Unit> = runCatching {
        Log.d(TAG, "deleteAllTemporary: calling API")
        api.deleteAllTemporary()
        Log.d(TAG, "deleteAllTemporary: SUCCESS")
        Unit
    }

    companion object {
        private const val TAG = "TimeLetterRepositoryImpl"
    }
}
