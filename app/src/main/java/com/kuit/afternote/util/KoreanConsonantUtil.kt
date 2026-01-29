package com.kuit.afternote.util

/**
 * 한글 초성(자음) 관련 유틸리티
 */
object KoreanConsonantUtil {

    private val KOREAN_CONSONANTS = listOf(
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
        'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    )

    private const val KOREAN_UNICODE_START = 0xAC00
    private const val KOREAN_UNICODE_END = 0xD7A3
    private const val VOWEL_COUNT = 21
    private const val FINAL_CONSONANT_COUNT = 28

    /**
     * 문자열의 첫 글자에서 초성을 추출합니다.
     * 한글이 아닌 경우 '#'을 반환합니다.
     */
    fun getInitialConsonant(text: String): Char {
        if (text.isEmpty()) return '#'

        val firstChar = text.first()
        val unicode = firstChar.code

        return if (unicode in KOREAN_UNICODE_START..KOREAN_UNICODE_END) {
            val consonantIndex = (unicode - KOREAN_UNICODE_START) / (VOWEL_COUNT * FINAL_CONSONANT_COUNT)
            KOREAN_CONSONANTS[consonantIndex]
        } else {
            '#'
        }
    }

    /**
     * 리스트를 초성별로 그룹화합니다.
     * @param items 그룹화할 아이템 리스트
     * @param keySelector 초성을 추출할 문자열을 반환하는 함수
     * @return 초성을 키로 하고 해당 초성으로 시작하는 아이템 리스트를 값으로 하는 Map
     */
    fun <T> groupByInitialConsonant(
        items: List<T>,
        keySelector: (T) -> String
    ): Map<Char, List<T>> {
        return items
            .groupBy { getInitialConsonant(keySelector(it)) }
            .toSortedMap(compareBy { KOREAN_CONSONANTS.indexOf(it).takeIf { idx -> idx >= 0 } ?: Int.MAX_VALUE })
    }
}
