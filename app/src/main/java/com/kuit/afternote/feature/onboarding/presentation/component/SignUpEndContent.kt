package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.button.ClickButton
import com.kuit.afternote.feature.onboarding.presentation.uimodel.AgreementItem
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B3
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun SignUpEndContent(onSettingCLick: () -> Unit) {
    var items by remember {
        mutableStateOf(
            listOf(
                AgreementItem(
                    title = "애프터노트 서비스 이용 약관",
                    required = true,
                    checked = false
                ),
                AgreementItem(
                    title = "개인정보 수집 및 이용 동의서",
                    required = true,
                    checked = false
                ),
                AgreementItem(
                    title = "E-mail 및 SMS 광고성 정보 수신동의",
                    required = false,
                    description = "다양한 프로모션 소식을 전해드립니다.",
                    checked = false
                )
            )
        )
    }
    val allChecked = items.all { it.checked }

    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "환영합니다!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Sansneo
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text("이제부터 애프터노트에서\n당신의 마지막을 준비하세요.")

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.img_logo),
                contentDescription = null,
                modifier = Modifier.size(width = 140.dp, height = 187.dp)
            )
        }

        Spacer(modifier = Modifier.height(56.dp))

        Row {
            AgreementCheckRow(
                title = "약관에 전체동의",
                checked = allChecked,
                onCheckedChange = { checked ->
                    items = items.map { it.copy(checked = checked) }
                }
            )
        }

        HorizontalDivider()

        Spacer(modifier = Modifier.height(30.dp))

        items.forEachIndexed { index, item ->
            AgreementCheckRow(
                title = item.title,
                checked = item.checked,
                required = item.required,
                description = item.description,
                onCheckedChange = { checked ->
                    items = items.toMutableList().also {
                        it[index] = it[index].copy(checked = checked)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ClickButton(
            color = B3,
            title = "프로필 설정하러 가기",
            onButtonClick = onSettingCLick
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpEndContentPreview() {
    AfternoteTheme {
        SignUpEndContent(
            onSettingCLick = {}
        )
    }
}
