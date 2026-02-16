package com.kuit.afternote.core.dummy.afternote

/**
 * Dummy data for 애프터노트 메인 목록 (list 화면).
 * Replace with repository/API load when backend is ready.
 */
object AfternoteListDummies {
    /**
     * Default afternote list (service name to date) for main list / preview.
     * Covers SOCIAL_NETWORK, GALLERY_AND_FILES, MEMORIAL.
     */
    fun defaultAfternoteList(): List<Pair<String, String>> =
        listOf(
            "인스타그램" to "2025.02.01",
            "갤러리" to "2025.01.28",
            "추모 가이드라인" to "2025.01.28",
            "페이스북" to "2025.01.20",
            "파일" to "2025.01.15",
            "카카오톡" to "2025.01.10"
        )
}
