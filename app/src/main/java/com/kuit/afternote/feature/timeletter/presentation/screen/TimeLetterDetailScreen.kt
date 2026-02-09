    package com.kuit.afternote.feature.timeletter.presentation.screen

    import androidx.compose.foundation.Image
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material3.*
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.kuit.afternote.R

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TimeLetterDetailScreen(
        onBackClick: () -> Unit = {}
    ) {
        // 전체 화면 스크롤 가능하도록 설정
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                // 커스텀 상단 바 ( <  박채연님께 ▼ )
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { /* 수신자 선택 로직 */ }
                        ) {
                            Text(
                                text = "박채연님께",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Image(
                                painter= painterResource(id= R.drawable.ic_down),
                                contentDescription = "Select Receiver"
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Image(
                                painter= painterResource(id= R.drawable.ic_arrow_back),
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            containerColor = Color.White
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState) // 스크롤 적용
                    .padding(horizontal = 24.dp) // 좌우 여백
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // 1. 날짜 정보
                Text(
                    text = "작성 일자: 2023.11.21",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "발송 일자 및 시간: 2026. 11. 24. 00:00",
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(30.dp))

                // 2. 제목
                Text(
                    text = "채연아 20번째 생일을 축하해",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 3. 구분선
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFEEEEEE) // 연한 회색
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 4. 본문 내용
                Text(
                    text = "너가 태어난 게 엊그제 같은데 벌써 스무살이라니, 엄마가 없어도 씩씩하게 컸을 채연이를 상상하면 너무 기특해서 안아주고 싶구나. 요즘 학교는 어떻게 다니고 있니? 공부는 잘하고 있지? 친구는 많이 사귀었니? 채연이가 가고싶어했던 그 학교, 학과에 갔을지 엄마는 너무 궁금해하면서 이 편지를 쓰고 있어. 엄마랑 다시 만나면 그동안 어떻게 지냈는지 하나하나 다 알려줘야 해. 사랑한다 생일 축하해 내 딸.",
                    fontSize = 16.sp,
                    lineHeight = 24.sp, // 줄 간격 넉넉하게
                    color = Color(0xFF333333) // 너무 진한 검정보다는 약간 부드러운 검정
                )

                Spacer(modifier = Modifier.height(40.dp)) // 하단 여백
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewTimeLetterDetail() {
        MaterialTheme {
            TimeLetterDetailScreen()
        }
    }
