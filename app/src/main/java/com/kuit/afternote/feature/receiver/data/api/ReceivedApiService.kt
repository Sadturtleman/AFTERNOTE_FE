package com.kuit.afternote.feature.receiver.data.api

import com.kuit.afternote.data.remote.ApiResponse
import com.kuit.afternote.feature.receiver.data.dto.CreateMindRecordReceiverRequestDto
import com.kuit.afternote.feature.receiver.data.dto.CreateTimeLetterReceiverRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * 수신자(Received) API 서비스. (스웨거 기준)
 *
 * - POST /api/received/time-letters: 타임레터 수신자 등록
 * - POST /api/received/mind-records: 마인드레코드 수신자 등록
 */
interface ReceivedApiService {

    /**
     * 타임레터에 수신자를 등록합니다. 여러 수신자를 한 번에 등록할 수 있습니다.
     *
     * @return data: 등록된 수신자 ID 목록
     */
    @POST("api/received/time-letters")
    suspend fun registerTimeLetterReceivers(
        @Body body: CreateTimeLetterReceiverRequestDto
    ): ApiResponse<List<Long>?>

    /**
     * 마인드레코드에 수신자를 등록합니다. 여러 수신자를 한 번에 등록할 수 있습니다.
     *
     * @return data: 등록된 수신자 ID 목록
     */
    @POST("api/received/mind-records")
    suspend fun registerMindRecordReceivers(
        @Body body: CreateMindRecordReceiverRequestDto
    ): ApiResponse<List<Long>?>

}
