package com.kuit.afternote.feature.timeletter.data.mapper

import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterListResponse
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterMediaResponse
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterResponse
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetter
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterList
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterMedia
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterMediaType as DtoMediaType
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterStatus as DtoStatus
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterMediaType as DomainMediaType
import com.kuit.afternote.feature.timeletter.domain.model.TimeLetterStatus as DomainStatus

/**
 * TimeLetter DTO를 Domain 모델로 변환. (스웨거 기준)
 */
object TimeLetterMapper {
    private fun toDomainStatus(dto: DtoStatus): DomainStatus =
        when (dto) {
            DtoStatus.DRAFT -> DomainStatus.DRAFT
            DtoStatus.SCHEDULED -> DomainStatus.SCHEDULED
            DtoStatus.SENT -> DomainStatus.SENT
        }

    private fun toDomainMediaType(dto: DtoMediaType): DomainMediaType =
        when (dto) {
            DtoMediaType.IMAGE -> DomainMediaType.IMAGE
            DtoMediaType.VIDEO -> DomainMediaType.VIDEO
            DtoMediaType.AUDIO -> DomainMediaType.AUDIO
        }

    private fun toDomainMedia(dto: TimeLetterMediaResponse): TimeLetterMedia =
        TimeLetterMedia(
            id = dto.id,
            mediaType = toDomainMediaType(dto.mediaType),
            mediaUrl = dto.mediaUrl
        )

    fun toTimeLetter(dto: TimeLetterResponse): TimeLetter =
        TimeLetter(
            id = dto.id,
            title = dto.title,
            content = dto.content,
            sendAt = dto.sendAt,
            status = toDomainStatus(dto.status),
            mediaList = dto.mediaList?.map { toDomainMedia(it) } ?: emptyList(),
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt,
            receiverIds = emptyList()
        )

    fun toTimeLetterList(dto: TimeLetterListResponse): TimeLetterList =
        TimeLetterList(
            timeLetters = dto.timeLetters.map { toTimeLetter(it) },
            totalCount = dto.totalCount
        )
}
