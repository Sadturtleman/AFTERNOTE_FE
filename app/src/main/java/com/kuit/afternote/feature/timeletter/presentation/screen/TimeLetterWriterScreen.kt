package com.kuit.afternote.feature.timeletter.presentation.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.kuit.afternote.R
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterReceiver
import com.kuit.afternote.feature.timeletter.presentation.component.ReceiverInfoItem
import com.kuit.afternote.feature.timeletter.presentation.component.WritingPlusMenu
import com.kuit.afternote.feature.timeletter.presentation.component.chosungGroupedItems
import com.kuit.afternote.feature.timeletter.presentation.component.groupByChosung
import com.kuit.afternote.core.ui.component.DateWheelPicker
import com.kuit.afternote.core.ui.component.DateWheelPickerDefaults
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.timeletter.presentation.component.DraftSavePopUp
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterRegisteredPopUp
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterWriterBottomBar
import com.kuit.afternote.feature.timeletter.presentation.component.TimeWheelPicker
import java.time.LocalDate
import java.time.LocalTime

private val RecipientDropdownMaxHeight = 300.dp

/**
 * 타임레터 작성 화면
 *
 * @param recipientName 수신자 이름
 * @param title 제목
 * @param content 내용
 * @param sendDate 선택된 발송 날짜
 * @param sendTime 선택된 발송 시간 (HH:mm)
 * @param showDatePicker DatePicker 표시 여부
 * @param showTimePicker TimePicker 표시 여부
 * @param draftCount 임시저장된 레터 개수
 * @param onTitleChange 제목 변경 콜백
 * @param onContentChange 내용 변경 콜백
 * @param onNavigateBack 뒤로가기 콜백
 * @param onRecipientClick 수신자 선택 클릭 콜백
 * @param onRegisterClick 등록 클릭 콜백
 * @param onSaveDraftClick 임시저장 클릭 콜백
 * @param onDraftCountClick 임시저장 개수 클릭 콜백 (DraftLetterScreen으로 이동)
 * @param onDateClick 날짜 선택 클릭 콜백
 * @param onTimeClick 시간 선택 클릭 콜백
 * @param onDatePickerDismiss DatePicker 닫기 콜백
 * @param onDateSelected 날짜 선택 완료 콜백
 * @param onTimePickerDismiss TimePicker 닫기 콜백
 * @param onTimeSelected 시간 선택 콜백 (24시간 형식 hour, minute)
 * @param showWritingPlusMenu 더보기(작성 플러스) 메뉴 표시 여부
 * @param onMoreClick 더보기 아이콘 클릭 콜백
 * @param onDismissPlusMenu 더보기 메뉴 닫기 콜백
 * @param receivers 수신자 목록 (드롭다운에 표시)
 * @param showRecipientDropdown 수신자 선택 드롭다운 표시 여부
 * @param onRecipientDropdownDismiss 수신자 드롭다운 닫기 콜백
 * @param onReceiverSelected 수신자 선택 콜백
 * @param showRegisteredPopUp 타임레터 등록 완료 팝업 표시 여부
 * @param showDraftSavePopUp 임시저장 완료 팝업 표시 여부
 * @param modifier Modifier
 */
@Composable
fun TimeLetterWriterScreen(
    modifier: Modifier = Modifier,
    recipientName: String,
    title: String,
    content: String,
    sendDate: String = "",
    sendTime: String = "",
    showDatePicker: Boolean = false,
    showTimePicker: Boolean = false,
    draftCount: Int = 0,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onRecipientClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onSaveDraftClick: () -> Unit,
    onDraftCountClick: () -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onDatePickerDismiss: () -> Unit = {},
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit = { _, _, _ -> },
    onTimePickerDismiss: () -> Unit = {},
    onTimeSelected: (hour: Int, minute: Int) -> Unit = { _, _ -> },
    showWritingPlusMenu: Boolean = false,
    onMoreClick: () -> Unit = {},
    onDismissPlusMenu: () -> Unit = {},
    receivers: List<TimeLetterReceiver> = emptyList(),
    showRecipientDropdown: Boolean = false,
    onRecipientDropdownDismiss: () -> Unit = {},
    onReceiverSelected: (TimeLetterReceiver) -> Unit = {},
    showRegisteredPopUp: Boolean = false,
    showDraftSavePopUp: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current
    var isMenuOpen by remember { mutableStateOf(false) }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedVoices by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedFiles by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var addedLinks by remember { mutableStateOf<List<String>>(emptyList()) }
    var showLinkDialog by remember { mutableStateOf(false) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 6)
    ) { uris -> if (uris.isNotEmpty()) selectedImages = selectedImages + uris }

    // 2. 음성(오디오) 피커
    val voicePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris -> if (uris.isNotEmpty()) selectedVoices = selectedVoices + uris }

    // 3. 파일 피커 (모든 타입)
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris -> if (uris.isNotEmpty()) selectedFiles = selectedFiles + uris }

    Box(modifier = modifier.fillMaxSize()) {
        if (showWritingPlusMenu) {
            Popup(
                alignment = Alignment.BottomStart,
                offset = IntOffset(
                    with(density) { 64.dp.roundToPx() },
                    with(density) { (-232).dp.roundToPx() }
                ),
                onDismissRequest = onDismissPlusMenu,
                properties = PopupProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
            ) {
                WritingPlusMenu(
                    onImageClick={},
                    onVoiceClick={},
                    onFileClick={},
                    onLinkClick={}
                )
            }
        }

        if (showRecipientDropdown) {
            val groupedReceivers = groupByChosung(receivers) { it.receiver_name }
            val topBarHeightPx = with(density) { 44.dp.roundToPx() }
            val dropdownOffsetY = WindowInsets.statusBars.getTop(density) + topBarHeightPx
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(0, dropdownOffsetY),
                onDismissRequest = onRecipientDropdownDismiss,
                properties = PopupProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(RecipientDropdownMaxHeight)
                        .shadow(elevation = 8.dp, shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                        .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                        .background(Color.White)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp)
                    ) {
                        chosungGroupedItems(groupedReceivers) { receiver ->
                            Box(
                                modifier = Modifier.clickable {
                                    onReceiverSelected(receiver)
                                    onRecipientDropdownDismiss()
                                }
                            ) {
                                ReceiverInfoItem(receiver = receiver)
                            }
                        }
                    }
                }
            }
        }

        if (showRegisteredPopUp) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { },
                properties = PopupProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                TimeLetterRegisteredPopUp()
            }
        }
        if (showDraftSavePopUp) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { },
                properties = PopupProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                DraftSavePopUp()
            }
        }
        Scaffold(
            modifier = Modifier.fillMaxWidth(),
            topBar = {
                Column(modifier = Modifier.statusBarsPadding()) {
                    TopBar(
                        title = "${recipientName}님께",
                        onBackClick = onNavigateBack,
                        onActionClick = onRegisterClick,
                        actionText = "등록",
                        navigationIcon = {
                            IconButton(onClick = { keyboardController?.hide() }) {
                                Image(
                                    painter = painterResource(R.drawable.img_writing),
                                    contentDescription = "키보드 숨기기",
                                    modifier = Modifier.size(width = 20.dp, height = 22.dp)
                                )
                            }
                        },
                        titleContent = {
                            Row(
                                modifier = Modifier.clickable { onRecipientClick() },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${recipientName}님께",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight(700),
                                    fontFamily = FontFamily(Font(R.font.sansneobold)),
                                    color = Color(0xFF212121)
                                )
                                Image(
                                    painter = painterResource(R.drawable.ic_down),
                                    contentDescription = "수신자 선택",
                                    modifier = Modifier.padding(start = 14.dp)
                                )
                            }
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF9E9E9E),
                        thickness = 0.4.dp
                    )
                }
            },
            bottomBar = {
                TimeLetterWriterBottomBar(
                    draftCount = draftCount,
                    onLinkClick = { isMenuOpen = !isMenuOpen },
                    onAddClick = { /* 더보기 동작 */ },
                    onSaveDraftClick = onSaveDraftClick,
                    onDraftCountClick = onDraftCountClick,
                    isMenuOpen = isMenuOpen,
                    onMenuDismiss = { isMenuOpen = false },
                    onImageAddClick = {
                        imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    onVoiceAddClick = {
                        voicePickerLauncher.launch(arrayOf("audio/*"))
                    },
                    onFileAddClick = {
                        filePickerLauncher.launch(arrayOf("*/*"))
                    },
                    onLinkAddClick = {
                        showLinkDialog = true
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .height(62.dp)
                        .padding(horizontal = 20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onDateClick() }
                    ) {
                        Text(
                            text = "발송 날짜",
                            color = Color(0xFF000000),
                            fontFamily = FontFamily(Font(R.font.sansneoregular)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // 선택된 날짜 표시
                            if (sendDate.isNotEmpty()) {
                                Text(
                                    text = sendDate,
                                    color = Color(0xFF212121),
                                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                            Image(
                                painter = painterResource(R.drawable.ic_down_vector),
                                contentDescription = "날짜 선택",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(top = 15.dp)
                                    .size(width = 12.dp, height = 6.dp)
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 15.dp),
                            color = Color(0xFF9E9E9E),
                            thickness = 0.4.dp
                        )
                    }

                    Spacer(modifier = Modifier.width(26.dp))

                    Column(
                        modifier = Modifier
                            .width(106.dp)
                            .clickable { onTimeClick() }
                    ) {
                        Text(
                            text = "발송 시간",
                            color = Color(0xFF000000),
                            fontFamily = FontFamily(Font(R.font.sansneoregular)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 18.sp
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (sendTime.isNotEmpty()) {
                                Text(
                                    text = sendTime,
                                    color = Color(0xFF212121),
                                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(top = 10.dp)
                                )
                            }
                            Image(
                                painter = painterResource(R.drawable.ic_down_vector),
                                contentDescription = "시간 선택",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(top = 15.dp)
                                    .size(width = 12.dp, height = 6.dp)
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 15.dp),
                            color = Color(0xFF9E9E9E),
                            thickness = 0.4.dp
                        )
                    }

                    // [3] 이미지 그리드 (제목과 본문 사이로 이동!)
                    if (selectedImages.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        SelectedImageGrid(images = selectedImages) { selectedImages = selectedImages - it }
                    }

                    // (2) 음성
                    if (selectedVoices.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        AttachmentList(
                            items = selectedVoices,
                            iconRes = R.drawable.ic_sound, // 아이콘 리소스 확인 필요
                            labelExtractor = { "음성 녹음 파일 ${selectedVoices.indexOf(it) + 1}" }, // 실제 파일명 대신 임시 이름
                            onRemove = { selectedVoices = selectedVoices - it }
                        )
                    }

                    // (3) 파일
                    if (selectedFiles.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        AttachmentList(
                            items = selectedFiles,
                            iconRes = R.drawable.ic_file, // 아이콘 리소스 확인 필요
                            labelExtractor = { "첨부 파일 ${selectedFiles.indexOf(it) + 1}" },
                            onRemove = { selectedFiles = selectedFiles - it }
                        )
                    }

                    // (4) 링크
                    if (addedLinks.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        AttachmentList(
                            items = addedLinks,
                            iconRes = R.drawable.ic_plus_link, // 아이콘 리소스 확인 필요
                            labelExtractor = { it }, // 링크 주소 그대로 표시
                            onRemove = { addedLinks = addedLinks - it }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 16.dp)
                ) {
                    BasicTextField(
                        value = title,
                        onValueChange = onTitleChange,
                        textStyle = TextStyle(
                            color = Color(0xFF000000),
                            fontFamily = FontFamily(Font(R.font.sansneoregular)),
                            fontSize = 18.sp,
                            fontWeight = FontWeight(400), lineHeight = 24.sp // 가독성 위해 줄간격 추가
                        ),
                        cursorBrush = SolidColor(Color(0xFF000000)),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                if (title.isEmpty()) {
                                    Text(
                                        text = "제목",
                                        // BodyLarge-R
                                        style = TextStyle(
                                            fontSize = 18.sp,
                                            fontFamily = FontFamily(Font(R.font.sansneoregular)),
                                            fontWeight = FontWeight(400),
                                            color = Color(0xFF212121),
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 15.dp),
                        color = Color(0xFF9E9E9E),
                        thickness = 0.4.dp
                    )
                }

                Spacer(modifier = Modifier.height(17.dp))

                BasicTextField(
                    value = content,
                    onValueChange = onContentChange,
                    textStyle = TextStyle(
                        color = Color(0xFF212121),
                        fontFamily = FontFamily(Font(R.font.sansneoregular)),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    cursorBrush = SolidColor(Color(0xFF212121)),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (content.isEmpty()) {
                                Text(
                                    text = "소중한 사람에게 타임 레터를 작성하세요.",
                                    color = Color(0xFF9E9E9E),
                                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth().heightIn(min = 200.dp)
                )
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // DatePicker 오버레이
        if (showDatePicker) {
            var selectedDate by remember { mutableStateOf(LocalDate.now()) }
            // 배경 딤 처리 + 클릭 시 닫기
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDatePickerDismiss() }
                    .zIndex(1f)
            )

            // DateWheelPicker 오버레이 (발송 날짜 아래 위치) - zIndex로 딤 위에 그려 휠 터치 전달
            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(vertical = 16.dp)
            ) {
                DateWheelPicker(
                    modifier = Modifier.width(DateWheelPickerDefaults.ContainerWidth),
                    currentDate = selectedDate,
                    onDateChanged = { date ->
                        selectedDate = date
                        onDateSelected(date.year, date.monthValue, date.dayOfMonth)
                    }
                )
            }
        }
        if (showLinkDialog) {
            LinkInputDialog(
                onDismiss = { showLinkDialog = false },
                onConfirm = { link ->
                    if (link.isNotBlank()) {
                        addedLinks = addedLinks + link
                    }
                    showLinkDialog = false
                }
            )
        }
        // TimePicker 오버레이 (TimeWheelPicker)
        if (showTimePicker) {
            val now = LocalTime.now()
            val (initialHour, initialMinute) = if (sendTime.isNotBlank()) {
                val parts = sendTime.split(":")
                if (parts.size == 2) {
                    val h = parts[0].toIntOrNull() ?: now.hour
                    val m = parts[1].toIntOrNull() ?: now.minute
                    h to m
                } else {
                    now.hour to now.minute
                }
            } else {
                now.hour to now.minute
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onTimePickerDismiss() }
                    .zIndex(1f)
            )

            // TimeWheelPicker 카드 - zIndex로 딤 위에 그려 휠 터치 전달
            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(vertical = 16.dp)
            ) {
                TimeWheelPicker(
                    initialHour = initialHour,
                    initialMinute = initialMinute,
                    onTimeChanged = { hour, minute -> onTimeSelected(hour, minute) }
                )
            }
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectedImageGrid(
    images: List<Uri>,
    onRemoveImage: (Uri) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = "첨부된 이미지 (${images.size})",
            fontSize = 12.sp,
            color = Color(0xFF9E9E9E),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // LazyVerticalGrid 대신 FlowRow 사용
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 3
        ) {
            images.forEach { uri ->
                Box(
                    modifier = Modifier
                        .width(100.dp) // 적절한 크기 고정 (또는 weight 로직 사용 가능)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "삭제",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(20.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .padding(3.dp)
                            .clickable { onRemoveImage(uri) }
                    )
                }
            }
        }
    }
}

@Composable
fun <T> AttachmentList(
    items: List<T>,
    iconRes: Int,
    labelExtractor: (T) -> String,
    onRemove: (T) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = labelExtractor(item),
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "삭제",
                    tint = Color(0xFF999999),
                    modifier = Modifier.size(18.dp).clickable { onRemove(item) }
                )
            }
        }
    }
}

// [다이얼로그] 링크 입력창
@Composable
fun LinkInputDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "링크",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(20.dp))

                // 입력 필드
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("URL을 입력하세요", color = Color(0xFF999999)) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 버튼 영역
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { onConfirm(text) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBBDEFB)), // 연한 파랑
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("링크 추가하기", color = Color(0xFF212121), fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "돌아가기",
                        color = Color(0xFF333333),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clickable { onDismiss() }
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}



@Preview(
    showBackground = true
)
@Composable
private fun TimeLetterWriterScreenPreview() {
    TimeLetterWriterScreen(
        recipientName = "박채연",
        title = "",
        content = "",
        sendDate = "",
        sendTime = "",
        draftCount = 3,
        onTitleChange = {},
        onContentChange = {},
        onNavigateBack = {},
        onRecipientClick = {},
        onRegisterClick = {},
        onSaveDraftClick = {},
        onDraftCountClick = {},
        onDateClick = {},
        onTimeClick = {},
        onTimePickerDismiss = {},
        onTimeSelected = { _, _ -> }
    )
}
