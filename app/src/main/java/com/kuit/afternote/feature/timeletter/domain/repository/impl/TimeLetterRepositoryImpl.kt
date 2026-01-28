package com.kuit.afternote.feature.timeletter.domain.repository.impl

import android.util.Log
import com.kuit.afternote.feature.timeletter.data.api.TimeLetterApiService
import com.kuit.afternote.feature.timeletter.data.dto.request.TimeLetterCreateRequest
import com.kuit.afternote.feature.timeletter.data.dto.request.TimeLetterDeleteRequest
import com.kuit.afternote.feature.timeletter.data.dto.request.TimeLetterUpdateRequest
import com.kuit.afternote.feature.timeletter.data.dto.response.TimeLetterBaseResponse
import com.kuit.afternote.feature.timeletter.data.dto.response.TimeLetterListResponse
import com.kuit.afternote.feature.timeletter.data.dto.response.TimeLetterResponse
import com.kuit.afternote.feature.timeletter.domain.repository.iface.TimeLetterRepository
import javax.inject.Inject

class TimeLetterRepositoryImpl @Inject constructor(
    private val apiService: TimeLetterApiService
) : TimeLetterRepository {

    //타임레터 전체 조회
    override suspend fun getEntireTimeLetter(): List<TimeLetterResponse> {
        return try {
            val response = apiService.getEntireTimeLetter()
            if (response.code == 0) {
                response.data?.timeLetters ?: emptyList()
            } else {
                throw Exception("서버 로직 에러: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("TimeLetterRepository", "getEntireTimeLetter failed", e)
            //테스트용 로그
            emptyList()
        }
    }

    // 타임레터 등록
    override suspend fun createTimeLetter(request: TimeLetterCreateRequest): TimeLetterResponse {
        try {
            val response = apiService.createTimeLetter(request)

            if (response.status == 0) {
                return response.data
            } else {
                throw Exception(response.message ?: "타임레터 생성 실패")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    // 타임레터 삭제
    override suspend fun deleteTimeLetters(request: TimeLetterDeleteRequest): String {
        try {
            val response = apiService.deleteTimeLetters(request)

            if (response.status == 0) {
                return response.message ?: "타임레터가 삭제되었습니다."

            } else {
                throw Exception(response.message ?: "타임레터 삭제 실패")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    //타임레터 단일 조회
    override suspend fun getTimeLetter(id: Long): TimeLetterResponse {
        try {
            val response = apiService.getTimeLetter(id)
            if (response.status == 0) {
                return response.data
            } else {
                throw Exception(response.message ?: "편지 상세 조회 실패")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    // 타임레터 수정
    override suspend fun updateTimeLetter(id: Long, request: TimeLetterUpdateRequest): TimeLetterResponse {
        try {
            val response = apiService.updateTimeLetter(id, request)

            if (response.status == 0) {
                return response.data
            } else {
                throw Exception(response.message ?: "타임레터 수정 실패")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }


    //임시저장 전체 조회
    override suspend fun getDraftTimeLetter(): TimeLetterListResponse {
        try {
            val response = apiService.getDraftTimeLetter()

            if (response.status == 0) {

                return response.data

            } else {
                throw Exception(response.message ?: "임시저장 조회 실패")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    // 임시저장 전체 삭제
    override suspend fun deleteDraftTimeLetter(): TimeLetterBaseResponse<Any> { // <--- 반환 타입 String
        try {
            val response = apiService.deleteDraftTimeLetter()
            if (response.status == 0) {
                return response
            } else {
                throw Exception(response.message ?: "삭제 실패")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

}
