package com.kuit.afternote.feature.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.navigation.BottomNavItem
import com.kuit.afternote.core.ui.component.navigation.BottomNavigationBar
import com.kuit.afternote.feature.home.presentation.component.CalendarDay
import com.kuit.afternote.feature.home.presentation.component.CalendarDayStyle
import com.kuit.afternote.feature.home.presentation.component.DailyQuestionCard
import com.kuit.afternote.feature.home.presentation.component.HomeHeader
import com.kuit.afternote.feature.home.presentation.component.HomeInfoCard
import com.kuit.afternote.feature.home.presentation.component.RecipientBadge
import com.kuit.afternote.feature.home.presentation.component.WeeklyCalendarStrip
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray1
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

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
    fun onDailyQuestionCtaClick()
    fun onFabClick()
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    content: HomeScreenContent = HomeScreenContent(),
    event: HomeScreenEvent = EmptyHomeScreenEvent
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Gray1,
        bottomBar = {
            BottomNavigationBar(
                selectedItem = BottomNavItem.HOME,
                onItemSelected = event::onBottomNavTabSelected
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = event::onFabClick,
                modifier = Modifier.size(56.dp),
                containerColor = Color.Transparent,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = androidx.compose.material3.FloatingActionButtonDefaults.elevation(
                    defaultElevation = 10.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFBDE0FF),
                                    Color(0xFFFFE1CC)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "새 애프터노트 추가",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            HomeHeader(
                onProfileClick = event::onProfileClick,
                onSettingsClick = event::onSettingsClick
            )

            // Greeting section
            GreetingSection(userName = content.userName)

            Spacer(modifier = Modifier.height(16.dp))

            // Recipient badge
            RecipientBadge(
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Weekly calendar
            WeeklyCalendarStrip(days = content.calendarDays)

            Spacer(modifier = Modifier.height(24.dp))

            // Daily question card
            DailyQuestionCard(
                question = content.dailyQuestion,
                dateLabel = content.dailyQuestionDate,
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
                        showCheckIcon = false
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
                        showCheckIcon = false
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
    override fun onBottomNavTabSelected(item: BottomNavItem) = Unit
    override fun onProfileClick() = Unit
    override fun onSettingsClick() = Unit
    override fun onDailyQuestionCtaClick() = Unit
    override fun onFabClick() = Unit
}

internal fun defaultCalendarDays(): List<CalendarDay> = listOf(
    CalendarDay("월", 10, CalendarDayStyle.OUTLINED),
    CalendarDay("화", 11, CalendarDayStyle.OUTLINED),
    CalendarDay("수", 12, CalendarDayStyle.OUTLINED),
    CalendarDay("목", 13, CalendarDayStyle.FILLED),
    CalendarDay("금", 14, CalendarDayStyle.FILLED),
    CalendarDay("토", 15, CalendarDayStyle.TODAY),
    CalendarDay("일", 16, CalendarDayStyle.DEFAULT)
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    AfternoteTheme {
        HomeScreen()
    }
}
