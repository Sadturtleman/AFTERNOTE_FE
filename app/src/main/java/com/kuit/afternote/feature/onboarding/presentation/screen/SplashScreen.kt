package com.kuit.afternote.feature.onboarding.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.kuit.afternote.R
import com.kuit.afternote.core.data.NotificationScheduler
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onStartClick: () -> Unit,
    onCheckClick: () -> Unit,
    onSignUpClick: () -> Unit = onLoginClick
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 권한 허용 시: 알림 스케줄링 로직 실행 가능
            Log.d("Permission", "Notification permission granted")
        } else {
            // 권한 거부 시: 사용자 경험을 저해하지 않는 선에서 처리 (예: 스낵바)
            Log.d("Permission", "Notification permission denied")
        }
    }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isPermissionGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!isPermissionGranted) {
                // 권한이 없다면 요청
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        NotificationScheduler.scheduleDailyNotification(context, 9, 0)
    }

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.3f))

            Image(
                painter = painterResource(R.drawable.img_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 40.dp, height = 50.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "나의 만약이, 남겨진 이들의\n막막함이 되지 않도록",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                fontFamily = Sansneo
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            color = B1,
                            fontFamily = Sansneo
                        )
                    ) {
                        append("애프터노트")
                    }
                    append("는 당신의 디지털 세상을\n 미리 정리해 가장 소중한 사람들에게\n 안전하고 따뜻한 다리를 제공합니다.")
                },
                fontSize = 16.sp,
                fontFamily = Sansneo,
                color = Gray5,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))

            ClickButton(
                color = B2,
                onButtonClick = onStartClick,
                title = "시작하기"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ClickButton(
                color = B3,
                onButtonClick = onCheckClick,
                title = "전달 받은 기록 확인하기"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ClickButton(
                color = B3,
                onButtonClick = onSignUpClick,
                title = "회원가입 (테스트용)"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "이미 가입하셨나요?",
                    color = Gray6,
                    fontFamily = Sansneo
                )

                Spacer(modifier = Modifier.width(11.dp))

                Text(
                    text = "로그인하기",
                    color = Gray6,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .clickable {
                            onLoginClick()
                        },
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.weight(0.3f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen(
        onLoginClick = {},
        onStartClick = {},
        onCheckClick = {}
    )
}
