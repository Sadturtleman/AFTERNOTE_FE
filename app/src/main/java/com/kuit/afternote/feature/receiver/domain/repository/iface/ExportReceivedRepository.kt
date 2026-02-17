package com.kuit.afternote.feature.receiver.domain.repository.iface

import com.kuit.afternote.feature.receiver.domain.entity.DownloadAllResult

/**
 * 수신 기록 전체를 기기 저장소에 내보내기(저장)하는 Repository.
 *
 * [DownloadAllResult]를 JSON 파일로 저장합니다.
 */
interface ExportReceivedRepository {

    /**
     * [data]를 JSON 파일로 저장합니다.
     * API 29+에서는 Downloads 공용 폴더, 그 미만에서는 앱 전용 외부 저장소에 저장합니다.
     *
     * @param data 저장할 타임레터·마인드레코드·애프터노트 전체 결과
     * @return 성공 시 [Result.success], 실패 시 [Result.failure]
     */
    suspend fun saveToFile(data: DownloadAllResult): Result<Unit>
}
