package com.kuit.afternote.core

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 하단 네비게이션 바 아이템
 *
 * 피그마 디자인 기반:
 * - 아이콘 위에 텍스트
 * - 활성화: 파란색(B1), 비활성화: 회색(Gray4)
 * - 폰트: 12sp
 */
@Composable
fun BottomNavigationItem(
    iconRes: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = label,
            colorFilter = ColorFilter.tint(if (isSelected) B1 else Gray4),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) B1 else Gray4,
            fontSize = 12.sp,
            fontFamily = Sansneo
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavigationItemPreview() {
    AfternoteTheme {
        BottomNavigationItem(
            iconRes = R.drawable.ic_home,
            label = "홈",
            isSelected = true,
            onClick = {}
        )
    }
}
