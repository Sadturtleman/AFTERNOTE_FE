package com.kuit.afternote.feature.timeletter.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuit.afternote.R
import com.kuit.afternote.core.BottomNavItem
import com.kuit.afternote.core.BottomNavigationBar
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterBlockItem
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterListItem
import com.kuit.afternote.feature.timeletter.presentation.component.ViewModeToggle
import com.kuit.afternote.feature.timeletter.presentation.uimodel.TimeLetterUiState
import com.kuit.afternote.feature.timeletter.presentation.uimodel.ViewMode
import com.kuit.afternote.feature.timeletter.presentation.viewmodel.TimeLetterViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme

@Composable
fun TimeLetterScreen(
    modifier: Modifier = Modifier,
    viewModel: TimeLetterViewModel = viewModel(),
    onNavItemSelected: (BottomNavItem) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val viewMode by viewModel.viewMode.collectAsState()

    Column(modifier = modifier) {
        // 공통 헤더
        TimeLetterHeader(onBackClick = onBackClick)

        Spacer(modifier= Modifier.padding(top=18.dp))

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
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(letters) { letter ->
                        when (viewMode) {
                            ViewMode.LIST -> {
                                TimeLetterListItem(
                                    receiverName = letter.receivername,
                                    sendDate = letter.sendDate,
                                    title = letter.title,
                                    content = letter.content,
                                    imageResId = letter.imageResId
                                )
                                Spacer(modifier = Modifier.height(18.dp))
                            }
                            ViewMode.BLOCK -> {
                                TimeLetterBlockItem(
                                    receiverName = letter.receivername,
                                    sendDate = letter.sendDate,
                                    title = letter.title,
                                    content = letter.content,
                                    imageResId = letter.imageResId,
                                    theme = letter.theme
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
            }
        }

        BottomNavigationBar(
            selectedItem = BottomNavItem.TIME_LETTER,
            onItemSelected = onNavItemSelected
        )
    }
}

// 공통 헤더 분리
@Composable
private fun TimeLetterHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(29.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.vector),
            contentDescription = "뒤로가기",
            modifier = Modifier
                .padding(start = 23.dp)
                .size(width = 6.dp, height = 12.dp)
                .clickable { onBackClick() }
        )
        Text(
            text = "타임 레터",
            color = Color(0xFF212121),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 131.dp, top = 5.dp)
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
            painter = painterResource(id = R.drawable.letter),
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
    device = "spec:width=390dp,height=844dp,dpi=420,isRound=false"
)
@Composable
private fun TimeLetterScreenPreview() {
    AfternoteTheme {
        TimeLetterScreen()
    }
}
