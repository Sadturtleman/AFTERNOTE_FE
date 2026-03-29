package com.kuit.afternote.integration.afternote

import com.kuit.afternote.feature.afternote.domain.model.AuthorReceiverDirectoryEntry
import com.kuit.afternote.feature.afternote.domain.model.received.InboxMindRecord
import com.kuit.afternote.feature.afternote.domain.model.received.InboxTimeLetter
import com.kuit.afternote.feature.afternote.domain.model.received.InboxTimeLetterMedia
import com.kuit.afternote.feature.afternote.domain.model.received.ReceivedListWithCount
import com.kuit.afternote.feature.afternote.domain.port.AuthorReceiversDirectoryPort
import com.kuit.afternote.feature.afternote.domain.port.CurrentAuthorUserIdPort
import com.kuit.afternote.feature.afternote.domain.port.LoadMindRecordsByAuthCodePort
import com.kuit.afternote.feature.afternote.domain.port.LoadSenderMessageByAuthCodePort
import com.kuit.afternote.feature.afternote.domain.port.LoadTimeLettersByAuthCodePort
import com.kuit.afternote.feature.afternote.domain.port.ReceiverAuthCodeProvider
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedMindRecord
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetter
import com.kuit.afternote.feature.receiver.domain.entity.ReceivedTimeLetterMedia
import com.kuit.afternote.feature.receiver.domain.usecase.GetMindRecordsByAuthCodeUseCase
import com.kuit.afternote.feature.receiver.domain.usecase.GetSenderMessageUseCase
import com.kuit.afternote.feature.receiver.domain.usecase.GetTimeLettersByAuthCodeUseCase
import com.kuit.afternote.feature.receiverauth.session.ReceiverAuthSessionHolder
import com.kuit.afternote.feature.user.domain.usecase.GetReceiversUseCase
import com.kuit.afternote.feature.user.domain.usecase.GetUserIdUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentAuthorUserIdAdapter
    @Inject
    constructor(
        private val getUserIdUseCase: GetUserIdUseCase,
    ) : CurrentAuthorUserIdPort {
        override suspend fun invoke(): Long? = getUserIdUseCase()
    }

@Singleton
class AuthorReceiversDirectoryAdapter
    @Inject
    constructor(
        private val getReceiversUseCase: GetReceiversUseCase,
    ) : AuthorReceiversDirectoryPort {
        override suspend fun invoke(userId: Long): Result<List<AuthorReceiverDirectoryEntry>> =
            getReceiversUseCase(userId).map { list ->
                list.map {
                    AuthorReceiverDirectoryEntry(
                        receiverId = it.receiverId,
                        name = it.name,
                        relation = it.relation,
                    )
                }
            }
    }

@Singleton
class LoadTimeLettersByAuthCodeAdapter
    @Inject
    constructor(
        private val getTimeLettersByAuthCodeUseCase: GetTimeLettersByAuthCodeUseCase,
    ) : LoadTimeLettersByAuthCodePort {
        override suspend fun invoke(authCode: String): Result<ReceivedListWithCount<InboxTimeLetter>> =
            getTimeLettersByAuthCodeUseCase(authCode).map { r ->
                ReceivedListWithCount(
                    items = r.items.map { it.toInboxTimeLetter() },
                    totalCount = r.totalCount,
                )
            }
    }

@Singleton
class LoadMindRecordsByAuthCodeAdapter
    @Inject
    constructor(
        private val getMindRecordsByAuthCodeUseCase: GetMindRecordsByAuthCodeUseCase,
    ) : LoadMindRecordsByAuthCodePort {
        override suspend fun invoke(authCode: String): Result<ReceivedListWithCount<InboxMindRecord>> =
            getMindRecordsByAuthCodeUseCase(authCode).map { r ->
                ReceivedListWithCount(
                    items = r.items.map { it.toInboxMindRecord() },
                    totalCount = r.totalCount,
                )
            }
    }

@Singleton
class LoadSenderMessageByAuthCodeAdapter
    @Inject
    constructor(
        private val getSenderMessageUseCase: GetSenderMessageUseCase,
    ) : LoadSenderMessageByAuthCodePort {
        override suspend fun invoke(authCode: String): Result<String?> = getSenderMessageUseCase(authCode)
    }

@Singleton
class ReceiverAuthCodeFromSessionAdapter
    @Inject
    constructor(
        private val sessionHolder: ReceiverAuthSessionHolder,
    ) : ReceiverAuthCodeProvider {
        override fun currentAuthCode(): String? = sessionHolder.getAuthCode()
    }

private fun ReceivedTimeLetter.toInboxTimeLetter(): InboxTimeLetter =
    InboxTimeLetter(
        timeLetterId = timeLetterId,
        timeLetterReceiverId = timeLetterReceiverId,
        title = title,
        content = content,
        sendAt = sendAt,
        status = status,
        senderName = senderName,
        deliveredAt = deliveredAt,
        createdAt = createdAt,
        mediaList = mediaList.map { it.toInboxMedia() },
        isRead = isRead,
    )

private fun ReceivedTimeLetterMedia.toInboxMedia(): InboxTimeLetterMedia =
    InboxTimeLetterMedia(
        id = id,
        mediaType = mediaType,
        mediaUrl = mediaUrl,
    )

private fun ReceivedMindRecord.toInboxMindRecord(): InboxMindRecord =
    InboxMindRecord(
        mindRecordId = mindRecordId,
        sourceType = sourceType,
        content = content,
        recordDate = recordDate,
    )
