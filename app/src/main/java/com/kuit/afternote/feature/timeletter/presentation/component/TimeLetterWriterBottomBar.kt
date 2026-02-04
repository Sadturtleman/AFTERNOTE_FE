package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.kuit.afternote.R

/**
 * 타임레터 작성 화면 하단 바
 *
 * @param draftCount 임시저장된 레터 개수
 * @param onLinkClick 링크 클릭 콜백
 * @param onAddClick 추가 클릭 콜백
 * @param onSaveDraftClick 임시저장 클릭 콜백
 * @param onDraftCountClick 임시저장 개수 클릭 콜백 (DraftLetterScreen으로 이동)
 * @param modifier Modifier
 */
@Composable
fun TimeLetterWriterBottomBar(
    draftCount: Int,
    onLinkClick: () -> Unit,
    onAddClick: () -> Unit,
    onSaveDraftClick: () -> Unit,
    onDraftCountClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.ic_link),
            contentDescription = "링크",
            modifier = Modifier
                .padding(start = 24.dp)
                .size(24.dp)
                .clickable { onLinkClick() }
        )
        Image(
            painterResource(R.drawable.ic_additional),
            contentDescription = "더보기",
            modifier = Modifier
                .padding(start = 16.33.dp)
                .size(24.dp)
        )
        Text(
            text = "임시저장",
            modifier = Modifier
                .padding(start = 176.97.dp)
                .clickable { onSaveDraftClick() },
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            lineHeight = 22.sp,
            color = Color(0xFF9E9E9E)
        )
        Image(
            painterResource(R.drawable.ic_radio_bar),
            contentDescription = "라디오 바",
            modifier = Modifier
                .padding(start = 16.dp)
        )
        Text(
            text = draftCount.toString(),
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable { onDraftCountClick() },
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            lineHeight = 22.sp,
            color = Color(0xFF9E9E9E)
        )
    }
}

@Composable
fun TimeLetterWriterBottomBar(
    draftCount: Int,
    onLinkClick: () -> Unit,
    onAddClick: () -> Unit,
    onSaveDraftClick: () -> Unit,
    onDraftCountClick: () -> Unit,
    modifier: Modifier = Modifier,
    // [추가] 팝업 제어를 위한 파라미터
    isMenuOpen: Boolean,
    onMenuDismiss: () -> Unit,
    onImageAddClick: () -> Unit,
    onFileAddClick: () -> Unit,
    onVoiceAddClick: () -> Unit,
    onLinkAddClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // [핵심 변경] 아이콘을 Box로 감싸서 Anchor(기준점)로 만듦
        // 기존의 padding(start = 24.dp)를 Box로 옮겨야 레이아웃이 깨지지 않음
        Box(
            modifier = Modifier.padding(start = 24.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_link),
                contentDescription = "링크",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onLinkClick() }
            )

            // [Popup] 상태가 true일 때만 Box(아이콘) 기준으로 렌더링
            if (isMenuOpen) {
                Popup(
                    // 아이콘 위로 16dp 정도 띄워서 표시
                    popupPositionProvider = DropUpPositionProvider(yOffset = 16),
                    onDismissRequest = onMenuDismiss,
                    properties = PopupProperties(focusable = true) // 외부 클릭 시 닫힘
                ) {
                    // 사용자 정의 메뉴 컴포넌트
                    WritingPlusMenu(
                        onImageClick = {
                            onMenuDismiss()
                            onImageAddClick()
                        },
                        onVoiceClick = {onVoiceAddClick()},
                        onFileClick = {onFileAddClick()},
                        onLinkClick = {onLinkAddClick()}
                    )
                }
            }
        }

        Image(
            painterResource(R.drawable.ic_additional),
            contentDescription = "더보기",
            modifier = Modifier
                .padding(start = 16.33.dp)
                .size(24.dp)
                .clickable { onAddClick() } // 클릭 이벤트 연결
        )

        // [팁] Spacer(Modifier.weight(1f))를 사용하면
        // 하드코딩된 padding(176.97.dp) 없이도 양쪽 정렬이 가능합니다.
        // 현재는 디자인 유지를 위해 기존 코드 존중
        Text(
            text = "임시저장",
            modifier = Modifier
                .padding(start = 176.97.dp)
                .clickable { onSaveDraftClick() },
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            lineHeight = 22.sp,
            color = Color(0xFF9E9E9E)
        )
        Image(
            painterResource(R.drawable.ic_radio_bar),
            contentDescription = "라디오 바",
            modifier = Modifier
                .padding(start = 16.dp)
        )
        Text(
            text = draftCount.toString(),
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable { onDraftCountClick() },
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            lineHeight = 22.sp,
            color = Color(0xFF9E9E9E)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeLetterWriterBottomBarPreview() {
    TimeLetterWriterBottomBar(
        draftCount = 3,
        onAddClick = {},
        onLinkClick = {},
        onSaveDraftClick = {},
        onDraftCountClick = {}
    )
}
