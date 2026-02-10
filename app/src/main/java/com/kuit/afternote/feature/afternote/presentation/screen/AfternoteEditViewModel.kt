package com.kuit.afternote.feature.afternote.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.afternote.data.dto.AfternoteCredentialsDto
import com.kuit.afternote.feature.afternote.data.dto.AfternotePlaylistDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteReceiverRefDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteSongDto
import com.kuit.afternote.feature.afternote.data.dto.AfternoteUpdateRequestDto
import com.kuit.afternote.feature.afternote.domain.usecase.CreateGalleryAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.CreatePlaylistAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.CreateSocialAfternoteUseCase
import com.kuit.afternote.feature.afternote.domain.usecase.UpdateAfternoteUseCase
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CATEGORY_SOCIAL = "소셜네트워크"
private const val CATEGORY_BUSINESS = "비즈니스"
private const val CATEGORY_GALLERY = "갤러리 및 파일"
private const val CATEGORY_MEMORIAL = "추모 가이드라인"

/**
 * 애프터노트 생성/수정 ViewModel.
 *
 * 카테고리에 따라 적절한 create UseCase를 호출하거나,
 * 기존 항목 수정 시 update UseCase를 호출합니다.
 */
@HiltViewModel
class AfternoteEditViewModel
    @Inject
    constructor(
        private val createSocialUseCase: CreateSocialAfternoteUseCase,
        private val createGalleryUseCase: CreateGalleryAfternoteUseCase,
        private val createPlaylistUseCase: CreatePlaylistAfternoteUseCase,
        private val updateUseCase: UpdateAfternoteUseCase
    ) : ViewModel() {

        private val _saveState = MutableStateFlow(AfternoteSaveState())
        val saveState: StateFlow<AfternoteSaveState> = _saveState.asStateFlow()

        /**
         * 애프터노트 저장 (생성 또는 수정).
         *
         * @param editingId null이면 신규 생성, non-null이면 수정
         * @param category 선택된 카테고리 한국어 문자열
         * @param payload 편집 화면에서 수집된 데이터
         * @param receivers 갤러리 카테고리의 수신자 목록
         * @param playlistStateHolder 추모 가이드라인의 플레이리스트 상태
         */
        fun saveAfternote(
            editingId: Long?,
            category: String,
            payload: RegisterAfternotePayload,
            receivers: List<AfternoteEditReceiver>,
            playlistStateHolder: MemorialPlaylistStateHolder?
        ) {
            if (_saveState.value.isSaving) return

            viewModelScope.launch {
                _saveState.update { it.copy(isSaving = true, error = null) }
                val result = if (editingId != null) {
                    performUpdate(
                        afternoteId = editingId,
                        category = category,
                        payload = payload,
                        receivers = receivers,
                        playlistStateHolder = playlistStateHolder
                    )
                } else {
                    performCreate(
                        category = category,
                        payload = payload,
                        receivers = receivers,
                        playlistStateHolder = playlistStateHolder
                    )
                }
                result
                    .onSuccess { id ->
                        _saveState.update {
                            it.copy(isSaving = false, saveSuccess = true, savedId = id)
                        }
                    }
                    .onFailure { e ->
                        _saveState.update {
                            it.copy(
                                isSaving = false,
                                error = e.message ?: "저장에 실패했습니다."
                            )
                        }
                    }
            }
        }

        private suspend fun performCreate(
            category: String,
            payload: RegisterAfternotePayload,
            receivers: List<AfternoteEditReceiver>,
            playlistStateHolder: MemorialPlaylistStateHolder?
        ): Result<Long> {
            val actions = payload.processingMethods.map { it.text } +
                payload.galleryProcessingMethods.map { it.text }
            val processMethod = toServerProcessMethod(
                accountProcessingMethod = payload.accountProcessingMethod,
                informationProcessingMethod = payload.informationProcessingMethod
            )
            val leaveMessage = payload.message.ifEmpty { null }

            return when (category) {
                CATEGORY_GALLERY -> createGalleryUseCase(
                    title = payload.serviceName,
                    processMethod = processMethod,
                    actions = actions,
                    leaveMessage = leaveMessage,
                    receiverIds = receivers.mapNotNull { it.id.toLongOrNull() }
                )
                CATEGORY_MEMORIAL -> {
                    val playlistDto = buildPlaylistDto(playlistStateHolder)
                    createPlaylistUseCase(
                        title = payload.serviceName,
                        playlist = playlistDto
                    )
                }
                else -> createSocialUseCase(
                    title = payload.serviceName,
                    processMethod = processMethod,
                    actions = actions,
                    leaveMessage = leaveMessage,
                    credentialsId = payload.accountId.takeIf { it.isNotEmpty() },
                    credentialsPassword = payload.password.takeIf { it.isNotEmpty() }
                )
            }
        }

        private suspend fun performUpdate(
            afternoteId: Long,
            category: String,
            payload: RegisterAfternotePayload,
            receivers: List<AfternoteEditReceiver>,
            playlistStateHolder: MemorialPlaylistStateHolder?
        ): Result<Long> {
            val actions = payload.processingMethods.map { it.text } +
                payload.galleryProcessingMethods.map { it.text }
            val processMethod = toServerProcessMethod(
                accountProcessingMethod = payload.accountProcessingMethod,
                informationProcessingMethod = payload.informationProcessingMethod
            )

            val body = AfternoteUpdateRequestDto(
                title = payload.serviceName,
                processMethod = processMethod.ifEmpty { null },
                actions = actions.ifEmpty { null },
                leaveMessage = payload.message.ifEmpty { null },
                credentials = when (category) {
                    CATEGORY_SOCIAL, CATEGORY_BUSINESS -> {
                        val id = payload.accountId.takeIf { it.isNotEmpty() }
                        val pw = payload.password.takeIf { it.isNotEmpty() }
                        if (id != null || pw != null) AfternoteCredentialsDto(id = id, password = pw)
                        else null
                    }
                    else -> null
                },
                receivers = when (category) {
                    CATEGORY_GALLERY -> receivers.mapNotNull { r ->
                        r.id.toLongOrNull()?.let { AfternoteReceiverRefDto(receiverId = it) }
                    }.ifEmpty { null }
                    else -> null
                },
                playlist = when (category) {
                    CATEGORY_MEMORIAL -> buildPlaylistDto(playlistStateHolder)
                    else -> null
                }
            )
            return updateUseCase(afternoteId = afternoteId, body = body)
        }

        /**
         * 클라이언트 enum 이름을 서버 processMethod 코드로 변환.
         *
         * - \"사망 후 추모 계정으로 전환\" 옵션 → MEMORIAL
         * - \"사망 후 데이터 보관 요청\" 옵션 → DELETE
         * - \"수신자에게 정보 전달\" 옵션 → RECEIVER
         *
         * 서버에서 내려오는 processMethod 의미에 맞춰 매핑합니다.
         */
        private fun toServerProcessMethod(
            accountProcessingMethod: String,
            informationProcessingMethod: String
        ): String =
            when (accountProcessingMethod) {
                // 사망 후 추모 계정으로 전환
                "MEMORIAL_ACCOUNT" -> "MEMORIAL"
                // 사망 후 데이터 보관 요청 (계정/데이터 보관)
                "PERMANENT_DELETE" -> "DELETE"
                // 수신자(또는 추가 수신자)에게 정보 전달
                "TRANSFER_TO_RECEIVER" -> "RECEIVER"
                else -> accountProcessingMethod.ifEmpty { informationProcessingMethod }
            }

        private fun buildPlaylistDto(
            playlistStateHolder: MemorialPlaylistStateHolder?
        ): AfternotePlaylistDto {
            val songs = playlistStateHolder?.songs?.map { song ->
                AfternoteSongDto(
                    id = song.id.toLongOrNull(),
                    title = song.title,
                    artist = song.artist,
                    coverUrl = song.albumCoverUrl
                )
            } ?: emptyList()
            return AfternotePlaylistDto(songs = songs)
        }
    }
