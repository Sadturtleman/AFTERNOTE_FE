package com.kuit.afternote.feature.mainpage.presentation.component

import com.kuit.afternote.R
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * getIconResForTitle 함수에 대한 Unit Test
 */
class GetIconResForTitleTest {
    @Test
    fun `인스타그램 제목은 insta_pattern을 반환한다`() {
        val result = getIconResForTitle("인스타그램")
        assertEquals(R.drawable.insta_pattern, result)
    }

    @Test
    fun `Instagram 제목은 insta_pattern을 반환한다`() {
        val result = getIconResForTitle("Instagram")
        assertEquals(R.drawable.insta_pattern, result)
    }

    @Test
    fun `갤러리 제목은 gallery를 반환한다`() {
        val result = getIconResForTitle("갤러리")
        assertEquals(R.drawable.gallery, result)
    }

    @Test
    fun `Gallery 제목은 gallery를 반환한다`() {
        val result = getIconResForTitle("Gallery")
        assertEquals(R.drawable.gallery, result)
    }

    @Test
    fun `알 수 없는 제목은 logo를 반환한다`() {
        val result = getIconResForTitle("기타")
        assertEquals(R.drawable.logo, result)
    }

    @Test
    fun `빈 문자열은 logo를 반환한다`() {
        val result = getIconResForTitle("")
        assertEquals(R.drawable.logo, result)
    }
}
