package com.kuit.afternote.feature.receiver.data.repository.impl

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.kuit.afternote.feature.receiver.data.mapper.ExportReceivedMapper
import com.kuit.afternote.feature.receiver.domain.entity.DownloadAllResult
import com.kuit.afternote.feature.receiver.domain.repository.iface.ExportReceivedRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * [ExportReceivedRepository] 구현체.
 *
 * [DownloadAllResult]를 JSON으로 직렬화하여 기기에 저장합니다.
 * API 29+에서는 Downloads 공용 폴더, 그 미만에서는 앱 전용 외부 저장소에 저장합니다.
 */
class ExportReceivedRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context
    ) : ExportReceivedRepository {

    override suspend fun saveToFile(data: DownloadAllResult): Result<Unit> =
        runCatching {
            val dto = ExportReceivedMapper.toExportDto(data)
            val jsonString = Json.encodeToString(dto)
            val fileName = FILE_NAME_PATTERN.format(
                SimpleDateFormat(DATE_FORMAT, Locale.US).format(Date())
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveViaMediaStore(context, fileName, jsonString)
            } else {
                saveViaExternalFilesDir(context, fileName, jsonString)
            }
        }

    private fun saveViaMediaStore(context: Context, fileName: String, content: String) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE_JSON)
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val uri = context.contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            values
        )
            ?: throw IllegalStateException("Failed to create file in Downloads")
        context.contentResolver.openOutputStream(uri).use { outputStream: OutputStream? ->
            (outputStream ?: throw IllegalStateException("Failed to open output stream"))
                .write(content.toByteArray(Charsets.UTF_8))
        }
    }

    private fun saveViaExternalFilesDir(context: Context, fileName: String, content: String) {
        val dir = context.getExternalFilesDir(null) ?: context.filesDir
        val file = File(dir, fileName)
        file.writeText(content, Charsets.UTF_8)
    }

    companion object {
        private const val DATE_FORMAT = "yyyyMMdd_HHmmss"
        private const val FILE_NAME_PATTERN = "afternote_backup_%s.json"
        private const val MIME_TYPE_JSON = "application/json"
    }
}
