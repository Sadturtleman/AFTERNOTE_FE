package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.feature.mainpage.presentation.model.DropdownMenuOverlayParams
import com.kuit.afternote.feature.mainpage.presentation.model.Recipient
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import com.kuit.afternote.ui.theme.White

/**
 * 수신자 리스트 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 흰색 배경, 둥근 모서리 16dp
 * - 수신자 아이템 리스트
 * - 하단 중앙에 추가 버튼 (파란 원형 버튼)
 */
@Composable
fun RecipientList(
    modifier: Modifier = Modifier,
    recipients: List<Recipient>,
    onAddClick: () -> Unit = {},
    onItemEditClick: (String) -> Unit = {},
    onItemDeleteClick: (String) -> Unit = {},
    onItemAdded: (String) -> Unit = {},
    onTextFieldVisibilityChanged: (Boolean) -> Unit = {},
    initialShowTextField: Boolean = false,
    initialExpandedItemId: String? = null
) {
    var showTextField by remember { mutableStateOf(initialShowTextField) }
    val focusManager = LocalFocusManager.current

    // 각 아이템의 expanded 상태를 추적하기 위한 맵
    val expandedStates = remember { mutableStateMapOf<String, Boolean>() }
    // 각 아이템의 위치를 추적하기 위한 맵
    val itemPositions = remember { mutableStateMapOf<String, Offset>() }
    // 각 아이템의 크기를 추적하기 위한 맵
    val itemSizes = remember { mutableStateMapOf<String, IntSize>() }
    // 부모 Box의 루트 위치
    var boxPositionInRoot by remember { mutableStateOf(Offset.Zero) }
    val density = LocalDensity.current

    recipients.forEach { recipient ->
        if (!expandedStates.containsKey(recipient.id)) {
            expandedStates[recipient.id] = initialExpandedItemId == recipient.id
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                boxPositionInRoot = coordinates.positionInRoot()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            recipients.forEachIndexed { _, recipient ->
                val expanded = expandedStates[recipient.id] ?: false

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            // 각 아이템의 위치와 크기 저장
                            itemPositions[recipient.id] = coordinates.positionInRoot()
                            itemSizes[recipient.id] = coordinates.size
                        }
                ) {
                    RecipientItem(
                        recipient = recipient,
                        onMoreClick = {
                            focusManager.clearFocus()
                            expandedStates[recipient.id] = !expanded
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 추가 버튼 (파란 원형 버튼)
            Image(
                painter = painterResource(R.drawable.ic_add_circle),
                contentDescription = "수신자 추가",
                modifier = Modifier
                    .clickable(onClick = {
                        showTextField = !showTextField
                        onAddClick()
                    })
            )
        }

        // 드롭다운 메뉴 오버레이
        DropdownMenuOverlay(
            params = DropdownMenuOverlayParams(
                itemIds = recipients.map { it.id },
                expandedStates = expandedStates,
                itemPositions = itemPositions,
                itemSizes = itemSizes,
                boxPositionInRoot = boxPositionInRoot,
                onItemEditClick = onItemEditClick,
                onItemDeleteClick = onItemDeleteClick,
                onExpandedStateChanged = { id, isExpanded ->
                    expandedStates[id] = isExpanded
                }
            )
        )
    }
}

/**
 * 수신자 아이템 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 아바타: 회색 원형 배경, 40dp
 * - 이름: 14sp, Regular, Gray9
 * - 라벨: 12sp, Regular, Gray5
 * - 더보기 아이콘: 오른쪽 정렬
 */
@Composable
private fun RecipientItem(
    modifier: Modifier = Modifier,
    recipient: Recipient,
    onMoreClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 아바타 (기본 프로필 이미지)
        Box(
            modifier = Modifier
                .size(58.dp)
                .clip(CircleShape)
        ) {
            Image(
                painter = painterResource(R.drawable.img_recipient_profile),
                contentDescription = "프로필 사진",
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        // 이름과 라벨
        Column(
            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = recipient.name,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray9
                )
            )
            Text(
                text = recipient.label,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray5
                )
            )
        }

        // 더보기 아이콘
        Image(
            painter = painterResource(R.drawable.ic_more_horizontal_1),
            contentDescription = "더보기",
            modifier = Modifier
                .clickable(onClick = onMoreClick)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RecipientListPreview() {
    AfternoteTheme {
        RecipientList(
            recipients = listOf(
                Recipient("1", "김지은", "친구"),
                Recipient("2", "박선호", "가족")
            ),
            onAddClick = {},
            onItemEditClick = {},
            onItemDeleteClick = {},
            onItemAdded = {},
            onTextFieldVisibilityChanged = {}
        )
    }
}
