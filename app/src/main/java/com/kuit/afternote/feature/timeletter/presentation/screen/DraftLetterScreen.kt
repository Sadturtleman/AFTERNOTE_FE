package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.timeletter.presentation.component.DraftDeleteBottomBar
import com.kuit.afternote.feature.timeletter.presentation.component.DraftLetterHeader
import com.kuit.afternote.feature.timeletter.presentation.component.DraftLetterListItem

/**
 * 임시저장 편지 데이터 모델
 */
data class DraftLetterItem(
    val id: String,
    val receiverName: String,
    val sendDate: String,
    val title: String
)

/**
 * 임시저장 편지 목록 화면
 *
 * @param modifier Modifier
 * @param onCloseClick 닫기 클릭 콜백
 */
@Composable
fun DraftLetterScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {}
) {
    // 편집 모드 상태
    var isEditMode by remember { mutableStateOf(false) }
    // 선택된 아이템 ID 목록
    var selectedItems by remember { mutableStateOf(setOf<String>()) }

    // 임시 목업 데이터
    val draftLetters = remember {
        listOf(
            DraftLetterItem("1", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
            DraftLetterItem("2", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
            DraftLetterItem("3", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
            DraftLetterItem("4", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
            DraftLetterItem("5", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
            DraftLetterItem("6", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
            DraftLetterItem("7", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
            DraftLetterItem("8", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
            DraftLetterItem("9", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
            DraftLetterItem("10", "김지은", "2029. 11. 20", "지은아 결혼을 축하해")
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                DraftLetterHeader(
                    isEditMode = isEditMode,
                    onCloseClick = onCloseClick,
                    onEditClick = {
                        isEditMode = true
                        selectedItems = emptySet()
                    },
                    onCompleteClick = {
                        isEditMode = false
                        selectedItems = emptySet()
                    }
                )
            }
        },
        bottomBar = {
            if (isEditMode) {
                DraftDeleteBottomBar(
                    onDeleteAll = {
                        // TODO: 전체 삭제 로직
                    },
                    onDeleteSelected = {
                        // TODO: 선택 삭제 로직
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(26.dp))

            // 카운트 텍스트
            DraftCountText(
                isEditMode = isEditMode,
                totalCount = draftLetters.size,
                selectedCount = selectedItems.size
            )

            Spacer(modifier = Modifier.height(23.dp))

            // 리스트
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(draftLetters) { letter ->
                    DraftLetterListItem(
                        item = letter,
                        isEditMode = isEditMode,
                        isSelected = selectedItems.contains(letter.id),
                        onItemClick = {
                            if (isEditMode) {
                                selectedItems = if (selectedItems.contains(letter.id)) {
                                    selectedItems - letter.id
                                } else {
                                    selectedItems + letter.id
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DraftCountText(
    isEditMode: Boolean,
    totalCount: Int,
    selectedCount: Int
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(0xFF9E9E9E))) {
                append("총 ")
            }
            withStyle(style = SpanStyle(color = Color(0xFF328BFF))) {
                append(if (isEditMode) selectedCount.toString() else totalCount.toString())
            }
            withStyle(style = SpanStyle(color = Color(0xFF9E9E9E))) {
                append(if (isEditMode) "개 선택됨" else "개")
            }
        },
        fontSize = 10.sp,
        fontWeight = FontWeight.W400,
        fontFamily = FontFamily(Font(R.font.sansneoregular)),
        lineHeight = 16.sp,
        modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
    )
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "일반 모드"
)
@Composable
private fun DraftLetterScreenPreview() {
    DraftLetterScreen()
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "편집 모드"
)
@Composable
private fun DraftLetterScreenEditPreview() {
    // 편집 모드 프리뷰용
    var selectedItems by remember { mutableStateOf(setOf("1", "3")) }

    val draftLetters = listOf(
        DraftLetterItem("1", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
        DraftLetterItem("2", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
        DraftLetterItem("3", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
        DraftLetterItem("4", "김지은", "2029. 11. 20", "지은아 결혼을 축하해"),
        DraftLetterItem("5", "김지은", "2029. 11. 20", "지은아 결혼을 축하해")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DraftLetterHeader(
                isEditMode = true,
                onCloseClick = {},
                onEditClick = {},
                onCompleteClick = {}
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 36.dp)
            ) {
                DraftDeleteBottomBar(
                    onDeleteAll = {},
                    onDeleteSelected = {}
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(26.dp))

            DraftCountText(
                isEditMode = true,
                totalCount = draftLetters.size,
                selectedCount = selectedItems.size
            )

            Spacer(modifier = Modifier.height(23.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(draftLetters) { letter ->
                    DraftLetterListItem(
                        item = letter,
                        isEditMode = true,
                        isSelected = selectedItems.contains(letter.id),
                        onItemClick = {
                            selectedItems = if (selectedItems.contains(letter.id)) {
                                selectedItems - letter.id
                            } else {
                                selectedItems + letter.id
                            }
                        }
                    )
                }
            }
        }
    }
}
