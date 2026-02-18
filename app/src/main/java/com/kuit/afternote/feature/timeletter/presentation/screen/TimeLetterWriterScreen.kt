// K2 false positive: state assigned in callback lambdas is read at recomposition (KT-78881).
@file:Suppress("AssignedValueIsNeverRead")
package com.kuit.afternote.feature.timeletter.presentation.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.DateWheelPicker
import com.kuit.afternote.core.ui.component.DateWheelPickerDefaults
import com.kuit.afternote.core.ui.component.navigation.TopBar
import com.kuit.afternote.feature.timeletter.data.dto.TimeLetterReceiver
import com.kuit.afternote.feature.timeletter.presentation.component.DraftSavePopUp
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterRegisteredPopUp
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterWriterBottomBar
import com.kuit.afternote.feature.timeletter.presentation.component.TimeLetterWriterBottomBarParams
import com.kuit.afternote.feature.timeletter.presentation.component.TimeWheelPicker
import com.kuit.afternote.feature.timeletter.presentation.component.WaitingAgainPopUp
import java.time.LocalDate
import java.time.LocalTime

/**
 * UI state for [TimeLetterWriterScreen].
 */
data class TimeLetterWriterScreenState(
    val receiverIds: List<Long> = emptyList(),
    val title: String = "",
    val content: String = "",
    val sendDate: String = "",
    val sendTime: String = "",
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val draftCount: Int = 0,
    val receivers: List<TimeLetterReceiver> = emptyList(),
    val showWritingPlusMenu: Boolean = false,
    val showRegisteredPopUp: Boolean = false,
    val showDraftSavePopUp: Boolean = false,
    val showWaitingAgainPopUp: Boolean = false,
    val selectedImageUriStrings: List<String> = emptyList(),
    val selectedVoiceUriStrings: List<String> = emptyList(),
    val addedLinks: List<String> = emptyList()
)

/**
 * Parameters for [TimeLetterWriterScreenContent].
 */
data class TimeLetterWriterScreenContentParams(
    val state: TimeLetterWriterScreenState,
    val events: TimeLetterWriterScreenEvents,
    val recipientDisplayText: String,
    val selectedFiles: List<Uri>,
    val addedLinks: List<String>,
    val showLinkDialog: Boolean,
    val onSelectedFilesChange: (List<Uri>) -> Unit,
    val onAddedLinksChange: (List<String>) -> Unit,
    val onShowLinkDialogChange: (Boolean) -> Unit,
    val imagePickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    val voicePickerLauncher: ActivityResultLauncher<Array<String>>,
    val filePickerLauncher: ActivityResultLauncher<Array<String>>
)

/**
 * Parameters for [TimeLetterWriterContent].
 */
data class TimeLetterWriterContentParams(
    val state: TimeLetterWriterScreenState,
    val events: TimeLetterWriterScreenEvents,
    val innerPadding: PaddingValues,
    val selectedFiles: List<Uri>,
    val addedLinks: List<String>,
    val onSelectedFilesChange: (List<Uri>) -> Unit,
    val onAddedLinksChange: (List<String>) -> Unit
)

/**
 * Parameters for [TimeLetterWriterAttachmentsSection].
 */
data class TimeLetterWriterAttachmentsParams(
    val selectedImageUriStrings: List<String>,
    val onRemoveImage: (String) -> Unit,
    val selectedVoiceUriStrings: List<String>,
    val onRemoveVoiceUri: (String) -> Unit,
    val selectedFiles: List<Uri>,
    val addedLinks: List<String>,
    val onSelectedFilesChange: (List<Uri>) -> Unit,
    val onAddedLinksChange: (List<String>) -> Unit
)

/**
 * Callbacks for [TimeLetterWriterScreen].
 */
data class TimeLetterWriterScreenEvents(
    val onTitleChange: (String) -> Unit,
    val onContentChange: (String) -> Unit,
    val onNavigateBack: () -> Unit,
    val onRecipientClick: () -> Unit,
    val onRegisterClick: () -> Unit,
    val onSaveDraftClick: () -> Unit,
    val onDraftCountClick: () -> Unit,
    val onDateClick: () -> Unit,
    val onTimeClick: () -> Unit,
    val onBackClick: () -> Unit,
    val onDatePickerDismiss: () -> Unit = {},
    val onDateSelected: (year: Int, month: Int, day: Int) -> Unit = { _, _, _ -> },
    val onTimePickerDismiss: () -> Unit = {},
    val onTimeSelected: (hour: Int, minute: Int) -> Unit = { _, _ -> },
    val onMoreClick: () -> Unit = {},
    val onDismissPlusMenu: () -> Unit = {},
    val onAddImages: (List<Uri>) -> Unit = {},
    val onRemoveImage: (String) -> Unit = {},
    val onAddVoiceUris: (List<Uri>) -> Unit = {},
    val onRemoveVoiceUri: (String) -> Unit = {},
    val onAddedLinksChange: (List<String>) -> Unit = {}
)

@Composable
fun TimeLetterWriterScreen(
    modifier: Modifier = Modifier,
    state: TimeLetterWriterScreenState,
    events: TimeLetterWriterScreenEvents
) {
    val recipientDisplayText = recipientDisplayText(
        receiverIds = state.receiverIds,
        receivers = state.receivers
    )
    var selectedFiles by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var showLinkDialog by remember { mutableStateOf(false) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 6)
    ) { uris -> if (uris.isNotEmpty()) events.onAddImages(uris) }
    val voicePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris -> if (uris.isNotEmpty()) events.onAddVoiceUris(uris) }
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris -> if (uris.isNotEmpty()) selectedFiles = selectedFiles + uris }

    TimeLetterWriterScreenContent(
        modifier = modifier,
        params = TimeLetterWriterScreenContentParams(
            state = state,
            events = events,
            recipientDisplayText = recipientDisplayText,
            selectedFiles = selectedFiles,
            addedLinks = state.addedLinks,
            showLinkDialog = showLinkDialog,
            onSelectedFilesChange = { selectedFiles = it },
            onAddedLinksChange = events.onAddedLinksChange,
            onShowLinkDialogChange = { showLinkDialog = it },
            imagePickerLauncher = imagePickerLauncher,
            voicePickerLauncher = voicePickerLauncher,
            filePickerLauncher = filePickerLauncher
        )
    )
}

@Composable
private fun TimeLetterWriterScreenContent(
    modifier: Modifier,
    params: TimeLetterWriterScreenContentParams
) {
    val state = params.state
    val events = params.events
    val onImageAddClick: () -> Unit = {
        params.imagePickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
    val onLinkAddClick: () -> Unit = { params.onShowLinkDialogChange(true) }

    Box(modifier = modifier.fillMaxSize()) {
        TimeLetterWriterPopups(state = state)
        Scaffold(
            modifier = Modifier.fillMaxWidth(),
            topBar = {
                TimeLetterWriterTopBar(
                    recipientDisplayText = params.recipientDisplayText,
                    onNavigateBack = events.onNavigateBack,
                    onRegisterClick = events.onRegisterClick,
                    onBackClick = events.onBackClick,
                    onRecipientClick = events.onRecipientClick
                )
            },
            bottomBar = {
                TimeLetterWriterBottomBar(
                    params = TimeLetterWriterBottomBarParams(
                        draftCount = state.draftCount,
                        onLinkClick = onLinkAddClick,
                        onAddClick = events.onMoreClick,
                        onSaveDraftClick = events.onSaveDraftClick,
                        onDraftCountClick = events.onDraftCountClick,
                        isMenuOpen = state.showWritingPlusMenu,
                        onMenuDismiss = events.onDismissPlusMenu,
                        onImageAddClick = onImageAddClick,
                        onVoiceAddClick = { params.voicePickerLauncher.launch(arrayOf("audio/*")) },
                        onFileAddClick = { params.filePickerLauncher.launch(arrayOf("*/*")) },
                        onLinkAddClick = onLinkAddClick
                    )
                )
            }
        ) { innerPadding ->
            TimeLetterWriterContent(
                params = TimeLetterWriterContentParams(
                    state = state,
                    events = events,
                    innerPadding = innerPadding,
                    selectedFiles = params.selectedFiles,
                    addedLinks = params.addedLinks,
                    onSelectedFilesChange = params.onSelectedFilesChange,
                    onAddedLinksChange = params.onAddedLinksChange
                )
            )
        }
        if (state.showDatePicker) {
            TimeLetterWriterDatePickerOverlay(
                onDatePickerDismiss = events.onDatePickerDismiss,
                onDateSelected = events.onDateSelected
            )
        }
        if (params.showLinkDialog) {
            LinkInputDialog(
                onDismiss = { params.onShowLinkDialogChange(false) },
                onConfirm = { link ->
                    if (link.isNotBlank()) params.onAddedLinksChange(params.addedLinks + link)
                    params.onShowLinkDialogChange(false)
                }
            )
        }
        if (state.showTimePicker) {
            TimeLetterWriterTimePickerOverlay(
                sendTime = state.sendTime,
                onTimePickerDismiss = events.onTimePickerDismiss,
                onTimeSelected = events.onTimeSelected
            )
        }
    }
}

@Composable
private fun recipientDisplayText(
    receiverIds: List<Long>,
    receivers: List<TimeLetterReceiver>
): String {
    return when {
        receiverIds.isEmpty() -> stringResource(R.string.content_description_select_receiver)
        receiverIds.size == 1 -> {
            val name = receivers.find { it.id == receiverIds[0] }?.receiver_name
            if (name != null) stringResource(R.string.time_letter_detail_receiver_to, name)
            else stringResource(R.string.content_description_select_receiver)
        }
        else -> stringResource(R.string.time_letter_recipients_count, receiverIds.size)
    }
}

@Composable
private fun TimeLetterWriterPopups(state: TimeLetterWriterScreenState) {
    // WritingPlusMenu is shown only by TimeLetterWriterBottomBar (anchored to add icon).
    // Do not show it here to avoid duplicate menus.
    if (state.showRegisteredPopUp) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = { },
            properties = PopupProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            TimeLetterRegisteredPopUp()
        }
    }
    if (state.showDraftSavePopUp) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = { },
            properties = PopupProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            DraftSavePopUp()
        }
    }
    if (state.showWaitingAgainPopUp) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = { },
            properties = PopupProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            WaitingAgainPopUp()
        }
    }
}

@Composable
private fun TimeLetterWriterTopBar(
    recipientDisplayText: String,
    onNavigateBack: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    onRecipientClick: () -> Unit
) {
    Column(modifier = Modifier.statusBarsPadding()) {
        TopBar(
            title = recipientDisplayText,
            onBackClick = onNavigateBack,
            onActionClick = onRegisterClick,
            actionText = "등록",
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Image(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "뒤로가기",
                        modifier = Modifier.size(width = 6.dp, height = 12.dp)
                    )
                }
            },
            titleContent = {
                Row(
                    modifier = Modifier.clickable { onRecipientClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = recipientDisplayText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight(700),
                        fontFamily = FontFamily(Font(R.font.sansneobold)),
                        color = Color(0xFF212121)
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_down),
                        contentDescription = stringResource(R.string.content_description_select_receiver),
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
}

@Composable
private fun TimeLetterWriterContent(params: TimeLetterWriterContentParams) {
    val state = params.state
    val events = params.events
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(params.innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        TimeLetterWriterDateTimeRow(
            sendDate = state.sendDate,
            sendTime = state.sendTime,
            onDateClick = events.onDateClick,
            onTimeClick = events.onTimeClick
        )
        TimeLetterWriterTitleSection(
            title = state.title,
            onTitleChange = events.onTitleChange
        )
        Spacer(modifier = Modifier.height(17.dp))
        TimeLetterWriterBodySection(
            content = state.content,
            onContentChange = events.onContentChange
        )
        Spacer(modifier = Modifier.height(24.dp))
        TimeLetterWriterAttachmentsSection(
            params = TimeLetterWriterAttachmentsParams(
                selectedImageUriStrings = state.selectedImageUriStrings,
                onRemoveImage = events.onRemoveImage,
                selectedVoiceUriStrings = state.selectedVoiceUriStrings,
                onRemoveVoiceUri = events.onRemoveVoiceUri,
                selectedFiles = params.selectedFiles,
                addedLinks = params.addedLinks,
                onSelectedFilesChange = params.onSelectedFilesChange,
                onAddedLinksChange = params.onAddedLinksChange
            )
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun TimeLetterWriterDateTimeRow(
    sendDate: String,
    sendTime: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(62.dp)
            .padding(horizontal = 20.dp)
    ) {
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
    }
}

@Composable
private fun TimeLetterWriterTitleSection(title: String, onTitleChange: (String) -> Unit) {
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
}

@Composable
private fun TimeLetterWriterBodySection(
    content: String,
    onContentChange: (String) -> Unit
) {
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
}

@Composable
private fun TimeLetterWriterAttachmentsSection(params: TimeLetterWriterAttachmentsParams) {
    if (params.selectedImageUriStrings.isNotEmpty()) {
        SelectedImageGrid(
            images = params.selectedImageUriStrings.map { it.toUri() },
            onRemoveImage = { uri -> params.onRemoveImage(uri.toString()) }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
    if (params.selectedVoiceUriStrings.isNotEmpty()) {
        AttachmentList(
            items = params.selectedVoiceUriStrings,
            iconRes = R.drawable.ic_sound,
            labelExtractor = { "음성 녹음 파일 ${params.selectedVoiceUriStrings.indexOf(it) + 1}" },
            onRemove = { params.onRemoveVoiceUri(it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
    if (params.selectedFiles.isNotEmpty()) {
        AttachmentList(
            items = params.selectedFiles,
            iconRes = R.drawable.ic_file,
            labelExtractor = { "첨부 파일 ${params.selectedFiles.indexOf(it) + 1}" },
            onRemove = { params.onSelectedFilesChange(params.selectedFiles - it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
    if (params.addedLinks.isNotEmpty()) {
        AttachmentList(
            items = params.addedLinks,
            iconRes = R.drawable.ic_plus_link,
            labelExtractor = { it },
            onRemove = { params.onAddedLinksChange(params.addedLinks - it) }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun TimeLetterWriterDatePickerOverlay(
    onDatePickerDismiss: () -> Unit,
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onDateSelected(
                        selectedDate.year,
                        selectedDate.monthValue,
                        selectedDate.dayOfMonth
                    )
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
                .align(Alignment.Center)
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
}

@Composable
private fun TimeLetterWriterTimePickerOverlay(
    sendTime: String,
    onTimePickerDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
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
    Box(modifier = Modifier.fillMaxSize()) {
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
                .align(Alignment.Center)
        ) {
            TimeWheelPicker(
                initialHour = initialHour,
                initialMinute = initialMinute,
                onTimeChanged = { hour, minute -> onTimeSelected(hour, minute) }
            )
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
        state = TimeLetterWriterScreenState(
            receiverIds = listOf(1L),
            draftCount = 3,
            receivers = listOf(
                TimeLetterReceiver(
                    id = 1L,
                    receiver_name = "박채연",
                    send_at = "2025-01-01",
                    title = "제목",
                    content = "내용",
                    image_url = null,
                    relation = "친구"
                )
            )
        ),
        events = TimeLetterWriterScreenEvents(
            onTitleChange = {},
            onContentChange = {},
            onNavigateBack = {},
            onRecipientClick = {},
            onRegisterClick = {},
            onSaveDraftClick = {},
            onDraftCountClick = {},
            onDateClick = {},
            onTimeClick = {},
            onBackClick = {}
        )
    )
}
