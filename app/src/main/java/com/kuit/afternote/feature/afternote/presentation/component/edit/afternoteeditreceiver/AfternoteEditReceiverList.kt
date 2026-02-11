<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverList.kt
package com.kuit.afternote.feature.mainpage.presentation.component.edit.mainpageeditreceiver
========
package com.kuit.afternote.feature.afternote.presentation.component.edit.afternoteeditreceiver
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/afternoteeditreceiver/AfternoteEditReceiverList.kt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.button.AddCircleButton
import com.kuit.afternote.core.ui.component.detail.EditDropdownMenu
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverList.kt
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiver
import com.kuit.afternote.feature.mainpage.presentation.component.edit.model.MainPageEditReceiverCallbacks
========
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiver
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AfternoteEditReceiverCallbacks
import com.kuit.afternote.app.compositionlocal.DataProviderLocals
import com.kuit.afternote.data.provider.FakeAfternoteEditDataProvider
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/afternoteeditreceiver/AfternoteEditReceiverList.kt
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
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverList.kt
fun MainPageEditReceiverList(
    modifier: Modifier = Modifier,
    mainPageEditReceivers: List<MainPageEditReceiver>,
    events: MainPageEditReceiverCallbacks = MainPageEditReceiverCallbacks(),
    state: MainPageEditReceiverListState = rememberMainPageEditReceiverListState()
========
fun AfternoteEditReceiverList(
    modifier: Modifier = Modifier,
    afternoteEditReceivers: List<AfternoteEditReceiver>,
    events: AfternoteEditReceiverCallbacks = AfternoteEditReceiverCallbacks(),
    state: AfternoteEditReceiverListState = rememberAfternoteEditReceiverListState()
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/afternoteeditreceiver/AfternoteEditReceiverList.kt
) {
    val focusManager = LocalFocusManager.current

    // 초기화: 수신자들의 expanded 상태 설정
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverList.kt
    LaunchedEffect(mainPageEditReceivers) {
        state.initializeExpandedStates(mainPageEditReceivers, null)
========
    LaunchedEffect(afternoteEditReceivers) {
        state.initializeExpandedStates(afternoteEditReceivers, null)
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/afternoteeditreceiver/AfternoteEditReceiverList.kt
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverList.kt
        mainPageEditReceivers.forEachIndexed { _, receiver ->
            MainPageEditReceiverItem(
========
        afternoteEditReceivers.forEachIndexed { _, receiver ->
            AfternoteEditReceiverItem(
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/afternoteeditreceiver/AfternoteEditReceiverList.kt
                receiver = receiver,
                expanded = state.expandedStates[receiver.id] ?: false,
                onMoreClick = {
                    focusManager.clearFocus()
                    state.toggleItemExpanded(receiver.id)
                },
                onDismissDropdown = {
                    state.expandedStates[receiver.id] = false
                },
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverList.kt
                onEditClick = { events.onItemEditClick(receiver.id) },
========
                showEditItem = false,
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/afternoteeditreceiver/AfternoteEditReceiverList.kt
                onDeleteClick = { events.onItemDeleteClick(receiver.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 추가 버튼 (파란 원형 버튼)
        AddCircleButton(
            contentDescription = "수신자 추가",
            onClick = {
                state.toggleTextField()
                events.onAddClick()
            }
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
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverList.kt
private fun MainPageEditReceiverItem(
    modifier: Modifier = Modifier,
    receiver: MainPageEditReceiver,
========
private fun AfternoteEditReceiverItem(
    modifier: Modifier = Modifier,
    receiver: AfternoteEditReceiver,
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/afternoteeditreceiver/AfternoteEditReceiverList.kt
    expanded: Boolean = false,
    onMoreClick: () -> Unit = {},
    onDismissDropdown: () -> Unit = {},
    showEditItem: Boolean = true,
    onDeleteClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 아바타 (기본 프로필 이미지)
        Image(
            painter = painterResource(R.drawable.img_recipient_profile),
            contentDescription = "프로필 사진",
            modifier = Modifier.size(58.dp),
        )

        // 이름과 라벨
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = receiver.name,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Gray9
                )
            )
            Text(
                text = receiver.label,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal,
                    color = Gray5
                )
            )
        }

        // 더보기 아이콘 + 드롭다운 메뉴
        Box {
            Image(
                painter = painterResource(R.drawable.ic_more_horizontal_1),
                contentDescription = "더보기",
                modifier = Modifier
                    .clickable(onClick = onMoreClick)
            )
            EditDropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissDropdown,
                showEditItem = showEditItem,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
<<<<<<<< HEAD:app/src/main/java/com/kuit/afternote/feature/mainpage/presentation/component/edit/mainpageeditreceiver/MainPageEditReceiverList.kt
private fun MainPageEditReceiverListPreview() {
    AfternoteTheme {
        MainPageEditReceiverList(
            mainPageEditReceivers = listOf(
                MainPageEditReceiver(id = "1", name = "김지은", label = "친구"),
                MainPageEditReceiver(id = "2", name = "박선호", label = "가족")
            ),
            events = MainPageEditReceiverCallbacks()
        )
========
private fun AfternoteEditReceiverListPreview() {
    AfternoteTheme {
        CompositionLocalProvider(
            DataProviderLocals.LocalAfternoteEditDataProvider provides FakeAfternoteEditDataProvider()
        ) {
            val provider = DataProviderLocals.LocalAfternoteEditDataProvider.current
            AfternoteEditReceiverList(
                afternoteEditReceivers = provider.getAfternoteEditReceivers(),
                events = AfternoteEditReceiverCallbacks()
            )
        }
>>>>>>>> 18f1a0c82c9329353ff9254b226743d587b4b33f:app/src/main/java/com/kuit/afternote/feature/afternote/presentation/component/edit/afternoteeditreceiver/AfternoteEditReceiverList.kt
    }
}
