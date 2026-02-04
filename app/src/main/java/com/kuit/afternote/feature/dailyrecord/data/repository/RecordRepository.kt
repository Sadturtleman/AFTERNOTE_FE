package com.kuit.afternote.feature.dailyrecord.data.repository

import com.kuit.afternote.feature.dailyrecord.data.RecordPost

/**
 * Repository for record/diary posts. Replace impl with API-backed impl when backend is ready.
 */
interface RecordRepository {
    suspend fun getPosts(): List<RecordPost>

    suspend fun getPostDetail(postId: Long): RecordPost?

    suspend fun createPost(
        title: String,
        content: String,
        imageUrl: String?
    ): RecordPost

    suspend fun updatePost(
        postId: Long,
        title: String,
        content: String,
        imageUrl: String?
    ): RecordPost?

    suspend fun deletePost(postId: Long): Boolean
}
