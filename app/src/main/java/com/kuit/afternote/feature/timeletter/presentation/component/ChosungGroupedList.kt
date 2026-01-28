package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

/**
 * 한글 초성 리스트
 */
private val CHOSUNG = arrayOf(
    'ㄱ',
    'ㄲ',
    'ㄴ',
    'ㄷ',
    'ㄸ',
    'ㄹ',
    'ㅁ',
    'ㅂ',
    'ㅃ',
    'ㅅ',
    'ㅆ',
    'ㅇ',
    'ㅈ',
    'ㅉ',
    'ㅊ',
    'ㅋ',
    'ㅌ',
    'ㅍ',
    'ㅎ'
)

/**
 * 문자열의 첫 글자에서 초성 추출
 * @param text 초성을 추출할 문자열
 * @return 추출된 초성 문자 (한글이 아니면 첫 글자 대문자 반환)
 */
fun getChosung(text: String): Char {
    if (text.isEmpty()) return '#'

    val firstChar = text[0]

    // 한글인 경우 초성 추출
    if (firstChar in '가'..'힣') {
        val chosungIndex = (firstChar.code - '가'.code) / 588
        return CHOSUNG[chosungIndex]
    }

    // 한글이 아니면 그대로 반환 (영어 등)
    return firstChar.uppercaseChar()
}

/**
 * 리스트를 초성별로 그룹화
 *
 * @param items 그룹화할 아이템 리스트
 * @param getName 아이템에서 이름을 추출하는 함수
 * @return 초성을 키로, 해당 초성의 아이템 리스트를 값으로 하는 정렬된 Map
 */
fun <T> groupByChosung(
    items: List<T>,
    getName: (T) -> String
): Map<Char, List<T>> =
    items
        .groupBy { getChosung(getName(it)) }
        .toSortedMap()

/**
 * LazyColumn에서 초성별 그룹화된 리스트를 표시하는 확장 함수
 *
 * @param groupedItems 초성별로 그룹화된 아이템 Map
 * @param itemContent 각 아이템을 표시할 Composable
 */
fun <T> LazyListScope.chosungGroupedItems(
    groupedItems: Map<Char, List<T>>,
    itemContent: @Composable (T) -> Unit
) {
    groupedItems.forEach { (chosung, itemsInGroup) ->
        // 초성 헤더
        item {
            ChosungHeader(chosung = chosung)
        }

        // 해당 초성의 아이템들
        items(itemsInGroup) { item ->
            itemContent(item)
        }
    }
}
