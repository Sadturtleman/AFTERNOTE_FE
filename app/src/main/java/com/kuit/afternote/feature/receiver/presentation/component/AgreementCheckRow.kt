package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray5
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun AgreementCheckRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        CircleCheckBox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Row {
                Text(
                    text = title,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Bold,
                    color = Gray9
                )
            }
        }
    }
}

@Composable
fun AgreementCheckRow(
    title: String,
    checked: Boolean,
    required: Boolean,
    description: String? = null,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        CircleCheckBox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Row {
                Text(
                    text = title,
                    fontFamily = Sansneo,
                    fontWeight = FontWeight.Medium,
                    color = Gray9
                )
                Text(
                    text = if (required) " (필수)" else " (선택)",
                    fontFamily = Sansneo,
                    color = Gray9
                )
            }

            description?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    fontFamily = Sansneo,
                    color = Gray5,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgreementCheckRowPreview() {
    AfternoteTheme {
        Column {
            AgreementCheckRow(
                title = "약관에 전체동의",
                checked = true,
                onCheckedChange = {}
            )
            AgreementCheckRow(
                title = "애프터노트 서비스 이용 약관",
                checked = false,
                required = true,
                description = "다양한 프로모션 소식을 전해드립니다.",
                onCheckedChange = {}
            )
        }
    }
}
