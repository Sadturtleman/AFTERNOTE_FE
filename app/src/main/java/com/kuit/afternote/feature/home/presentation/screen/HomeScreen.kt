package com.kuit.afternote.feature.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordHomeContract
import com.kuit.afternote.feature.dailyrecord.presentation.viewmodel.MindRecordViewModel
import com.kuit.afternote.feature.home.presentation.component.CalendarDay
import com.kuit.afternote.feature.home.presentation.component.CalendarDayStyle
import com.kuit.afternote.feature.home.presentation.component.DailyQuestionCard
import com.kuit.afternote.feature.home.presentation.component.HomeHeader
import com.kuit.afternote.feature.home.presentation.component.HomeInfoCard
import com.kuit.afternote.feature.home.presentation.component.RecipientBadge
import com.kuit.afternote.feature.home.presentation.component.WeeklyCalendarStrip
import com.kuit.afternote.feature.user.presentation.uimodel.ProfileUiState
import com.kuit.afternote.feature.user.presentation.viewmodel.ProfileEditViewModelContract
import com.kuit.afternote.feature.user.presentation.viewmodel.ProfileViewModel
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class HomeScreenContent(
    val userName: String = "박서연",
    val calendarDays: List<CalendarDay> = defaultCalendarDays(),
    val dailyQuestion: String = "오늘 하루,\n누구에게 가장 고마웠나요?",
    val dailyQuestionDate: String = "11월 15일, 오늘의 질문",
    val nextStepTitle: String = "가족들이 모르는 '주거래 은행' 정보를\n잊으신 건 없나요?",
    val pastRecordTitle: String = "\"내 인생에서 가장 소중했던 순간은?\"",
    val pastRecordSubtitle: String = "   - 아이가 태어났을 때..."
)

interface HomeScreenEvent {
    fun onBottomNavTabSelected(item: BottomNavItem)
    fun onProfileClick()
    fun onSettingsClick()
    fun onAfterNoteClick()
    fun onDailyQuestionCtaClick()
    fun onTimeLetterClick()
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    event: HomeScreenEvent = EmptyHomeScreenEvent,
    profileViewModel: ProfileEditViewModelContract = hiltViewModel<ProfileViewModel>(),
    recordViewModel: MindRecordHomeContract = hiltViewModel<MindRecordViewModel>()
) {
    var content by remember { mutableStateOf(HomeScreenContent()) }
    val uiState = profileViewModel.uiState.collectAsStateWithLifecycle()
    val recordUiState = recordViewModel.calendarDays.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
        recordViewModel.loadRecordsForDiaryList()


        content = content.copy(
            userName = uiState.value.name,
            dailyQuestionDate = LocalDate.now().format(DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)),
            calendarDays = recordUiState.value
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Gray1,
        topBar = {
            HomeHeader(
                onProfileClick = event::onProfileClick,
                onSettingsClick = event::onSettingsClick
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.HOME,
                onItemSelected = event::onBottomNavTabSelected
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Greeting section
            GreetingSection(userName = uiState.value.name)

            Spacer(modifier = Modifier.height(16.dp))

            // Recipient badge
            RecipientBadge(
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Weekly calendar
            WeeklyCalendarStrip(days = recordUiState.value)

            Spacer(modifier = Modifier.height(24.dp))

            // Daily question card
            DailyQuestionCard(
                question = content.dailyQuestion,
                dateLabel = LocalDate.now().format(DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)),
                onCtaClick = event::onDailyQuestionCtaClick
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Next step section
            SectionTitle(
                title = stringResource(R.string.home_next_step),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            HomeInfoCard(
                modifier = Modifier.padding(horizontal = 20.dp),
                title = content.nextStepTitle,
                badge = {
                    RecipientBadge(
                        text = stringResource(R.string.home_record_now),
                        showCheckIcon = false,
                        onCLick = event::onAfterNoteClick
                    )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Past records section
            SectionTitle(
                title = stringResource(R.string.home_review_records),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            HomeInfoCard(
                modifier = Modifier.padding(horizontal = 20.dp),
                title = content.pastRecordTitle,
                subtitle = content.pastRecordSubtitle,
                badge = {
                    RecipientBadge(
                        text = stringResource(R.string.home_read_past_record),
                        showCheckIcon = false,
                        onCLick = event::onTimeLetterClick
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun GreetingSection(
    userName: String
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = stringResource(R.string.home_greeting, userName),
            fontFamily = Sansneo,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 30.sp,
            letterSpacing = (-0.25).sp,
            color = Gray9
        )
        Text(
            text = stringResource(R.string.home_subtitle),
            fontFamily = Sansneo,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Gray5
        )
    }
}

@Composable
private fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        text = title,
        modifier = modifier.fillMaxWidth(),
        fontFamily = Sansneo,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        color = Gray9
    )
}

private object EmptyHomeScreenEvent : HomeScreenEvent {
    override fun onBottomNavTabSelected(item: BottomNavItem) {
        // No-op: default event when no callback is provided.
    }
    override fun onProfileClick() {
        // No-op: default event when no callback is provided.
    }
    override fun onSettingsClick() {
        // No-op: default event when no callback is provided.
    }
    override fun onDailyQuestionCtaClick() {
        // No-op: default event when no callback is provided.
    }
    override fun onTimeLetterClick() {
        // No-op: default event when no callback is provided.
    }
    override fun onAfterNoteClick() {
        // No-op: default event when no callback is provided.
    }
}

/** Fake ProfileEditViewModelContract for Preview (no Hilt). */
private class FakeProfileEditViewModel : ProfileEditViewModelContract {
    private val _uiState = MutableStateFlow(ProfileUiState(name = "박서연"))
    override val uiState: StateFlow<ProfileUiState> = _uiState
    override fun loadProfile() {
        // No-op: Fake for Preview only; no API call.
    }
    override fun updateProfile(
        name: String?,
        phone: String?,
        profileImageUrl: String?,
        pickedProfileImageUri: String?
    ) {
        // No-op: Fake for Preview only; no state update.
    }
    override fun setSelectedProfileImageUri(uri: android.net.Uri?) {
        // No-op: Fake for Preview only.
    }
    override fun clearUpdateSuccess() {
        // No-op: Fake for Preview only.
    }
}

/** Fake MindRecordHomeContract for Preview (no Hilt). */
private class FakeMindRecordHomeViewModel : MindRecordHomeContract {
    override val calendarDays: StateFlow<List<CalendarDay>> =
        MutableStateFlow(defaultCalendarDays())
    override fun loadRecordsForDiaryList() {
        // No-op: Fake for Preview only; no API call.
    }
}

internal fun defaultCalendarDays(): List<CalendarDay> = listOf(
    CalendarDay("월", 10, CalendarDayStyle.OUTLINED),
    CalendarDay("화", 11, CalendarDayStyle.OUTLINED),
    CalendarDay("수", 12, CalendarDayStyle.OUTLINED),
    CalendarDay("목", 13, CalendarDayStyle.FILLED),
    CalendarDay("금", 14, CalendarDayStyle.FILLED),
    CalendarDay("토", 15, CalendarDayStyle.TODAY),
    CalendarDay("일", 16, CalendarDayStyle.OUTLINED)
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    AfternoteTheme {
        HomeScreen(
            profileViewModel = remember { FakeProfileEditViewModel() },
            recordViewModel = remember { FakeMindRecordHomeViewModel() }
        )
    }
}
