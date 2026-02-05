package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.R
import com.kuit.afternote.app.compositionlocal.DataProviderLocals
import com.kuit.afternote.core.ui.component.ScaffoldContentWithOptionalFab
import com.kuit.afternote.core.ui.component.list.TimeLetterBlockList
import com.kuit.afternote.data.provider.FakeReceiverDataProvider
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.feature.timeletter.presentation.component.LetterTheme
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterListItem
import com.kuit.afternote.feature.timeletter.presentation.component.ViewModeToggle
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterItem
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterUiState
import com.kuit.afternote.feature.timeletter.presentation.uimodel.ViewMode
import com.kuit.afternote.feature.timeletter.presentation.viewmodel.TimeLetterViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * 타임레터 메인 화면
 *
 * 타임레터 목록을 리스트 또는 블록 형태로 표시합니다.
 *
 * @param viewModel 타임레터 ViewModel
 * @param onNavItemSelected 하단 네비게이션 아이템 선택 콜백
 * @param onBackClick 뒤로가기 클릭 콜백
 * @param modifier Modifier
 */
@Composable
fun TimeLetterScreen(
    modifier: Modifier = Modifier,
    viewModel: TimeLetterViewModel = androidx.lifecycle.viewmodel.compose
        .viewModel(),
    onNavItemSelected: (BottomNavItem) -> Unit = {},
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val viewMode by viewModel.viewMode.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TimeLetterHeader(onBackClick = onBackClick)
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.TIME_LETTER,
                onItemSelected = onNavItemSelected
            )
        }
    ) { innerPadding ->
        ScaffoldContentWithOptionalFab(
            paddingValues = innerPadding,
            showFab = true,
            onFabClick = onAddClick
        ) { contentModifier ->
            Column(
                modifier = contentModifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(18.dp))

            // 상태에 따른 조건부 렌더링
            when (uiState) {
                is TimeLetterUiState.Loading -> {
                    // 로딩 상태 (필요시 로딩 인디케이터)
                    Box(modifier = Modifier.weight(1f))
                }

                is TimeLetterUiState.Empty -> {
                    // 빈 화면 (토글 없음)
                    LetterEmptyContent(
                        modifier = Modifier.weight(1f)
                    )
                }

                is TimeLetterUiState.Success -> {
                    val letters = (uiState as TimeLetterUiState.Success).letters

                    // 필터 + 토글 Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "전체보기",
                            fontFamily = FontFamily(Font(R.font.sansneomedium)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Image(
                            painterResource(R.drawable.ic_down_vector),
                            contentDescription = "아래 열기",
                            modifier = Modifier.padding(start = 13.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        ViewModeToggle(
                            currentMode = viewMode,
                            onModeChange = { viewModel.updateViewMode(it) }
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // 리스트/블록 뷰
                    when (viewMode) {
                        ViewMode.LIST -> {
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(
                                    items = letters,
                                    key = { it.id }
                                ) { letter ->
                                    TimeLetterListItem(
                                        receiverName = letter.receivername,
                                        sendDate = letter.sendDate,
                                        title = letter.title,
                                        content = letter.content,
                                        imageResId = letter.imageResId
                                    )
                                    Spacer(modifier = Modifier.height(18.dp))
                                }
                            }
                        }
                        ViewMode.BLOCK -> {
                            TimeLetterBlockList(
                                modifier = Modifier.weight(1f),
                                timeLetterItemList = letters,
                                contentPadding = PaddingValues(bottom = 16.dp)
                            )
                        }
                    }
                }
            }
            }
        }
    }
}

// 공통 헤더 분리
@Composable
private fun TimeLetterHeader(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(29.dp),
        contentAlignment = Alignment.Center // 자식들을 중앙 정렬
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_vector),
            contentDescription = "뒤로가기",
            modifier = Modifier
                .align(Alignment.CenterStart) // 왼쪽 중앙에 배치
                .padding(start = 23.dp)
                .size(width = 6.dp, height = 12.dp)
                .clickable { onBackClick() }
        )

        Text(
            text = "타임 레터",
            color = Color(0xFF212121),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// 빈 화면 컨텐츠
@Composable
private fun LetterEmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_time_letter_placeholder),
            contentDescription = "편지이미지",
            modifier = Modifier
                .width(88.dp)
                .height(57.57.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "아직 등록된 타임 레터가 없습니다.\n타임 레터를 작성하여\n소중한 사람에게 마음을 전하세요",
            fontSize = 14.sp,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            color = Color(0xFF9E9E9E),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "리스트형"
)
@Composable
private fun TimeLetterScreenListPreview() {
    AfternoteTheme {
        CompositionLocalProvider(
            DataProviderLocals.LocalReceiverDataProvider provides FakeReceiverDataProvider()
        ) {
            TimeLetterScreenPreviewContent(initialViewMode = ViewMode.LIST)
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false",
    name = "블록형"
)
@Composable
private fun TimeLetterScreenBlockPreview() {
    AfternoteTheme {
        CompositionLocalProvider(
            DataProviderLocals.LocalReceiverDataProvider provides FakeReceiverDataProvider()
        ) {
            TimeLetterScreenPreviewContent(initialViewMode = ViewMode.BLOCK)
        }
    }
}

@Composable
private fun TimeLetterScreenPreviewContent(initialViewMode: ViewMode) {
    val provider = DataProviderLocals.LocalReceiverDataProvider.current
    val letters = provider.getTimeLetterItemsForPreview()
    var currentViewMode by remember { mutableStateOf(initialViewMode) }
    val uiState: TimeLetterUiState = TimeLetterUiState.Success(letters)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                TimeLetterHeader(onBackClick = {})
            }
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.TIME_LETTER,
                onItemSelected = {}
            )
        }
    ) { innerPadding ->
        ScaffoldContentWithOptionalFab(
            paddingValues = innerPadding,
            showFab = true,
            onFabClick = {}
        ) { contentModifier ->
            Column(
                modifier = contentModifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(18.dp))

                when (uiState) {
                is TimeLetterUiState.Loading -> {
                    Box(modifier = Modifier.weight(1f))
                }

                is TimeLetterUiState.Empty -> {
                    LetterEmptyContent(modifier = Modifier.weight(1f))
                }

                is TimeLetterUiState.Success -> {
                    val letters = uiState.letters

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "전체보기",
                            fontFamily = FontFamily(Font(R.font.sansneomedium)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Image(
                            painterResource(R.drawable.ic_down_vector),
                            contentDescription = "아래 열기",
                            modifier = Modifier.padding(start = 13.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        ViewModeToggle(
                            currentMode = currentViewMode,
                            onModeChange = { currentViewMode = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    when (currentViewMode) {
                        ViewMode.LIST -> {
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(
                                    items = letters,
                                    key = { it.id }
                                ) { letter ->
                                    TimeLetterListItem(
                                        receiverName = letter.receivername,
                                        sendDate = letter.sendDate,
                                        title = letter.title,
                                        content = letter.content,
                                        imageResId = letter.imageResId
                                    )
                                    Spacer(modifier = Modifier.height(18.dp))
                                }
                            }
                        }

                        ViewMode.BLOCK -> {
                            TimeLetterBlockList(
                                modifier = Modifier.weight(1f),
                                timeLetterItemList = letters,
                                contentPadding = PaddingValues(bottom = 16.dp)
                            )
                        }
                    }
                }
            }
            }
        }
    }
}
