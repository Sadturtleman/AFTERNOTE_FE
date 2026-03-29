package com.kuit.afternote.feature.afternote.domain.usecase

import com.kuit.afternote.feature.afternote.domain.model.AuthorReceiverDirectoryEntry
import com.kuit.afternote.feature.afternote.domain.model.Detail
import com.kuit.afternote.feature.afternote.domain.model.DetailReceiver
import com.kuit.afternote.feature.afternote.domain.port.AuthorReceiversDirectoryPort
import com.kuit.afternote.feature.afternote.domain.port.CurrentAuthorUserIdPort
import com.kuit.afternote.feature.afternote.domain.repository.AfternoteRepository
import javax.inject.Inject

/**
 * 애프터노트 상세 조회 UseCase.
 *
 * GET /api/afternotes/{afternoteId}. When the API returns receivers with only receiverId,
 * name/relation are resolved from GET /users/receivers so they appear on the detail screen.
 */
class GetAfternoteDetailUseCase
    @Inject
    constructor(
        private val repository: AfternoteRepository,
        private val currentAuthorUserId: CurrentAuthorUserIdPort,
        private val authorReceiversDirectory: AuthorReceiversDirectoryPort,
    ) {
        suspend operator fun invoke(afternoteId: Long): Result<Detail> {
            val detailResult = repository.getAfternoteDetail(afternoteId = afternoteId)
            val detail = detailResult.getOrElse { return detailResult }
            val userId = currentAuthorUserId() ?: return detailResult
            val receiversListResult = authorReceiversDirectory(userId)
            val receiversList = receiversListResult.getOrElse { return detailResult }
            val resolvedReceivers =
                resolveReceiverNames(
                    receivers = detail.receivers,
                    receiversList = receiversList,
                )
            return Result.success(detail.copy(receivers = resolvedReceivers))
        }

        private fun resolveReceiverNames(
            receivers: List<DetailReceiver>,
            receiversList: List<AuthorReceiverDirectoryEntry>,
        ): List<DetailReceiver> =
            receivers.map { r ->
                if (r.receiverId != null) {
                    val fromList = receiversList.find { it.receiverId == r.receiverId }
                    r.copy(
                        name = fromList?.name ?: r.name,
                        relation = fromList?.relation ?: r.relation,
                    )
                } else {
                    r
                }
            }
    }
