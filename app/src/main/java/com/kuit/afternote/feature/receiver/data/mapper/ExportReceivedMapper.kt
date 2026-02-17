package com.kuit.afternote.feature.receiver.data.mapper

import com.kuit.afternote.feature.receiver.data.dto.DownloadAllResultExportDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedAfternoteExportDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedMindRecordExportDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedTimeLetterExportDto
import com.kuit.afternote.feature.receiver.data.dto.ReceivedTimeLetterMediaExportDto
import com.kuit.afternote.feature.receiver.domain.entity.DownloadAllResult
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedAfternote
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetterMedia

/**
 * [DownloadAllResult]를 JSON 직렬화용 Export DTO로 변환하는 Mapper.
 */
object ExportReceivedMapper {

    /**
     * 도메인 [DownloadAllResult]를 [DownloadAllResultExportDto]로 변환합니다.
     */
    fun toExportDto(result: DownloadAllResult): DownloadAllResultExportDto =
        DownloadAllResultExportDto(
            timeLetters = result.timeLetters.map(::toTimeLetterExportDto),
            mindRecords = result.mindRecords.map(::toMindRecordExportDto),
            afternotes = result.afternotes.map(::toAfternoteExportDto)
        )

    private fun toTimeLetterExportDto(letter: ReceivedTimeLetter): ReceivedTimeLetterExportDto =
        ReceivedTimeLetterExportDto(
            timeLetterId = letter.timeLetterId,
            timeLetterReceiverId = letter.timeLetterReceiverId,
            title = letter.title,
            content = letter.content,
            sendAt = letter.sendAt,
            status = letter.status,
            senderName = letter.senderName,
            deliveredAt = letter.deliveredAt,
            createdAt = letter.createdAt,
            mediaList = letter.mediaList.map(::toMediaExportDto),
            isRead = letter.isRead
        )

    private fun toMediaExportDto(media: ReceivedTimeLetterMedia): ReceivedTimeLetterMediaExportDto =
        ReceivedTimeLetterMediaExportDto(
            id = media.id,
            mediaType = media.mediaType,
            mediaUrl = media.mediaUrl
        )

    private fun toMindRecordExportDto(record: ReceivedMindRecord): ReceivedMindRecordExportDto =
        ReceivedMindRecordExportDto(
            mindRecordId = record.mindRecordId,
            sourceType = record.sourceType,
            content = record.content,
            recordDate = record.recordDate
        )

    private fun toAfternoteExportDto(afternote: ReceivedAfternote): ReceivedAfternoteExportDto =
        ReceivedAfternoteExportDto(
            sourceType = afternote.sourceType,
            lastUpdatedAt = afternote.lastUpdatedAt,
            leaveMessage = afternote.leaveMessage
        )
}
