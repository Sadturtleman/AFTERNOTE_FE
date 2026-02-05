import com.kuit.afternote.feature.dailyrecord.data.api.DailyRecordApiService
import com.kuit.afternote.feature.dailyrecord.data.dto.PostRequestDto

class DailyRecordRepositoryImpl(
    private val api: DailyRecordApiService
) {
    suspend fun postContent(title: String, content: String): Result<Unit> {
        return try {
            val dto = PostRequestDto(title, content)
            val response = api.createPost(dto)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("서버 오류"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
