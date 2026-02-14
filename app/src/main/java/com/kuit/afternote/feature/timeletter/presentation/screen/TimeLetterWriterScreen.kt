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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.kuit.afternote.core.ui.component.DateWheelPicker
import com.kuit.afternote.core.ui.component.DateWheelPickerDefaults
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterReceiver
import com.kuit.afternote.feature.timeletter.presentation.component.DraftSavePopUp
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterRegisteredPopUp
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterWriterBottomBar
import com.kuit.afternote.feature.timeletter.presentation.component.TimeWheelPicker
import com.kuit.afternote.feature.timeletter.presentation.component.WritingPlusMenu
import com.kuit.afternote.feature.timeletter.presentation.component.WaitingAgainPopUp
import java.time.LocalDate
import java.time.LocalTime

private val RecipientDropdownMaxHeight = 300.dp

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
    showDraftSavePopUp: Boolean = false,
    showWaitingAgainPopUp: Boolean = false
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current

    // UI State
    var isMenuOpen by remember { mutableStateOf(false) }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedVoices by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedFiles by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var addedLinks by remember { mutableStateOf<List<String>>(emptyList()) }
    var showLinkDialog by remember { mutableStateOf(false) }

    // Activity Result Launchers
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 6)
    ) { uris -> if (uris.isNotEmpty()) selectedImages = selectedImages + uris }

    val voicePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris -> if (uris.isNotEmpty()) selectedVoices = selectedVoices + uris }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris -> if (uris.isNotEmpty()) selectedFiles = selectedFiles + uris }

    Box(modifier = modifier.fillMaxSize()) {
        // [Popup & Overlays]
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
                    onImageClick = {},
                    onVoiceClick = {},
                    onFileClick = {},
                    onLinkClick = {}
                )
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

        if (showWaitingAgainPopUp) {
            Popup(
                alignment = Alignment.Center,
                onDismissRequest = { },
                properties = PopupProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                WaitingAgainPopUp()
            }
        }

        // [Main Scaffold]
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
                    onLinkClick = {  },
                    onAddClick = { isMenuOpen = !isMenuOpen },
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
            // [Content Area - Vertical Scroll]
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // [1] 발송 날짜/시간 선택 영역 (Row)
                Row(
                    modifier = Modifier
                        .height(62.dp)
                        .padding(horizontal = 20.dp)
                ) {
                    // 날짜
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(62.dp)
                            .clickable { onDateClick() }
                    ) {
                        Text(
                            text = "발송 날짜",
                            color = Color(0xFF000000),
                            fontFamily = FontFamily(Font(R.font.sansneoregular)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (sendDate.isNotEmpty()) {
                                Text(
                                    text = sendDate,
                                    color = Color(0xFF212121),
                                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
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

                    // 시간
                    Column(
                        modifier = Modifier
                            .width(106.dp)
                            .height(62.dp)
                            .clickable { onTimeClick() }
                    ) {
                        Text(
                            text = "발송 시간",
                            color = Color(0xFF000000),
                            fontFamily = FontFamily(Font(R.font.sansneoregular)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (sendTime.isNotEmpty()) {
                                Text(
                                    text = sendTime,
                                    color = Color(0xFF212121),
                                    fontFamily = FontFamily(Font(R.font.sansneoregular)),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
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
                } // <--- [중요] Row Close

                // [2] 제목 입력 영역
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
                            fontWeight = FontWeight(400),
                            lineHeight = 24.sp
                        ),
                        cursorBrush = SolidColor(Color(0xFF000000)),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box {
                                if (title.isEmpty()) {
                                    Text(
                                        text = "제목",
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

                // [3] 본문 입력 영역
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
                        .fillMaxWidth()
                        .heightIn(min = 200.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // [4] 첨부 파일 표시 영역 (본문 하단에 수직 배치)

                // (1) 이미지
                if (selectedImages.isNotEmpty()) {
                    SelectedImageGrid(images = selectedImages) { selectedImages = selectedImages - it }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // (2) 음성
                if (selectedVoices.isNotEmpty()) {
                    AttachmentList(
                        items = selectedVoices,
                        iconRes = R.drawable.ic_sound,
                        labelExtractor = { "음성 녹음 파일 ${selectedVoices.indexOf(it) + 1}" },
                        onRemove = { selectedVoices = selectedVoices - it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // (3) 파일
                if (selectedFiles.isNotEmpty()) {
                    AttachmentList(
                        items = selectedFiles,
                        iconRes = R.drawable.ic_file,
                        labelExtractor = { "첨부 파일 ${selectedFiles.indexOf(it) + 1}" },
                        onRemove = { selectedFiles = selectedFiles - it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // (4) 링크
                if (addedLinks.isNotEmpty()) {
                    AttachmentList(
                        items = addedLinks,
                        iconRes = R.drawable.ic_plus_link,
                        labelExtractor = { it },
                        onRemove = { addedLinks = addedLinks - it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 하단 여백 (스크롤 시 마지막 내용이 잘리지 않도록)
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // [Dialogs & Pickers Overlays]
        // 1. DatePicker
        if (showDatePicker) {
            var selectedDate by remember { mutableStateOf(LocalDate.now()) }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onDateSelected(selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth)
                        onDatePickerDismiss()
                    }
                    .zIndex(1f)
            )

            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(vertical = 16.dp)
                    .align(Alignment.Center) // 중앙 정렬 추가
            ) {
                DateWheelPicker(
                    modifier = Modifier.width(DateWheelPickerDefaults.ContainerWidth),
                    currentDate = selectedDate,
                    onDateChanged = { date ->
                        selectedDate = date
                        onDateSelected(date.year, date.monthValue, date.dayOfMonth)
                    },
                    minDate = LocalDate.now()
                )
            }
        }

        // 2. Link Input Dialog
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

        // 3. TimePicker
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

            Box(
                modifier = Modifier
                    .zIndex(2f)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(vertical = 16.dp)
                    .align(Alignment.Center) // 중앙 정렬 추가
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

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 3
        ) {
            images.forEach { uri ->
                Box(
                    modifier = Modifier
                        .width(100.dp)
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
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onRemove(item) }
                )
            }
        }
    }
}

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

                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { onConfirm(text) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBBDEFB)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
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

@Preview(showBackground = true)
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
