package com.kuit.afternote.feature.dailyrecord.data.repository

import com.kuit.afternote.feature.dailyrecord.data.RecordPost
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory implementation. Replace with API-backed impl when backend is ready.
 */
@Singleton
class RecordRepositoryImpl
    @Inject
    constructor() : RecordRepository {
        private val posts = mutableListOf(
            RecordPost(
                id = 1,
                title = "Jetpack Compose 시작하기",
                content = "Jetpack Compose는 Android의 최신 UI 툴킷입니다. 선언형 UI로 더 쉽고 빠르게 UI를 만들 수 있습니다.",
                imageUrl = null,
                createdAt = "2025-10-05T10:00:00",
                updatedAt = "2025-10-05T10:00:00"
            ),
            RecordPost(
                id = 2,
                title = "Kotlin Coroutines 완벽 가이드",
                content = "비동기 프로그래밍을 쉽게! Coroutines를 사용하면 복잡한 비동기 코드를 간단하게 작성할 수 있습니다.",
                imageUrl = null,
                createdAt = "2025-10-05T11:30:00",
                updatedAt = "2025-10-05T11:30:00"
            ),
            RecordPost(
                id = 3,
                title = "Android MVVM 아키텍처",
                content = "MVVM 패턴으로 코드를 구조화하면 테스트와 유지보수가 쉬워집니다. ViewModel과 LiveData/StateFlow를 활용해봅시다.",
                imageUrl = null,
                createdAt = "2025-10-05T14:20:00",
                updatedAt = "2025-10-05T14:20:00"
            )
        )
        private var nextId = 4L

        override suspend fun getPosts(): List<RecordPost> {
            delay(500)
            return posts.toList()
        }

        override suspend fun getPostDetail(postId: Long): RecordPost? {
            delay(300)
            return posts.find { it.id == postId }
        }

        override suspend fun createPost(
            title: String,
            content: String,
            imageUrl: String?
        ): RecordPost {
            delay(500)
            val now = LocalDateTime.now().toString()
            val newPost = RecordPost(
                id = nextId++,
                title = title,
                content = content,
                imageUrl = imageUrl,
                createdAt = now,
                updatedAt = now
            )
            posts.add(0, newPost)
            return newPost
        }

        override suspend fun updatePost(
            postId: Long,
            title: String,
            content: String,
            imageUrl: String?
        ): RecordPost? {
            delay(500)
            val index = posts.indexOfFirst { it.id == postId }
            if (index == -1) return null
            val old = posts[index]
            val updated = old.copy(
                title = title,
                content = content,
                imageUrl = imageUrl,
                updatedAt = LocalDateTime.now().toString()
            )
            posts[index] = updated
            return updated
        }

        override suspend fun deletePost(postId: Long): Boolean {
            delay(300)
            return posts.removeIf { it.id == postId }
        }
    }
