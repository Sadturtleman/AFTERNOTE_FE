package com.kuit.afternote.feature.dailyrecord.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuit.afternote.feature.dailyrecord.data.RecordPost
import com.kuit.afternote.feature.dailyrecord.data.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyRecordViewModel
    @Inject
    constructor(
        private val recordRepository: RecordRepository
    ) : ViewModel() {
        var posts by mutableStateOf<List<RecordPost>>(emptyList())
            private set

        var postDetail by mutableStateOf<RecordPost?>(null)
            private set

        var uploadedImageUrl by mutableStateOf<String?>(null)
            private set

        fun getPosts() {
            viewModelScope.launch {
                posts = recordRepository.getPosts()
            }
        }

        fun getPostDetail(postId: Long) {
            viewModelScope.launch {
                postDetail = recordRepository.getPostDetail(postId)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun createPost(
            title: String,
            content: String,
            imageUrl: String? = null,
            onSuccess: () -> Unit = {}
        ) {
            viewModelScope.launch {
                recordRepository.createPost(title, content, imageUrl)
                posts = recordRepository.getPosts()
                onSuccess()
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun updatePost(
            postId: Long,
            title: String,
            content: String,
            imageUrl: String? = null,
            onSuccess: () -> Unit = {}
        ) {
            viewModelScope.launch {
                val updated = recordRepository.updatePost(postId, title, content, imageUrl)
                if (updated != null) {
                    postDetail = updated
                    posts = recordRepository.getPosts()
                    onSuccess()
                }
            }
        }

        fun deletePost(
            postId: Long,
            onSuccess: () -> Unit = {}
        ) {
            viewModelScope.launch {
                if (recordRepository.deletePost(postId)) {
                    posts = recordRepository.getPosts()
                    onSuccess()
                }
            }
        }

        fun clearUploadedImageUrl() {
            uploadedImageUrl = null
        }
    }
