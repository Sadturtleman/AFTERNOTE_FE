package com.kuit.afternote.feature.mainpage.presentation.component.edit.upload

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.ProfileImage
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * 영정사진 추가 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 라벨: 12sp, Regular, Gray9
 * - 라벨과 이미지 간 간격: 6dp
 * - 큰 원형 배경: B3 (#BDE0FF), 120dp
 * - 프로필 아이콘: 중앙에 위치, 48dp
 * - 작은 플러스 버튼: 우하단에 오버레이, 40dp, B2 (#89C2FF)
 * - 플러스 아이콘: 흰색, 24dp
 */
@Composable
fun MemorialPhotoUpload(
    modifier: Modifier = Modifier,
    label: String = "영정사진 추가",
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = Gray9
            )
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImage(
                profileImageRes = R.drawable.img_default_profile_deceased,
                profileImageSize = 144.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemorialPhotoUploadPreview() {
    AfternoteTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 이미지 없음
            MemorialPhotoUpload()

            // 이미지 있음 (TODO: 실제 이미지 로드 구현 필요)
            MemorialPhotoUpload()
        }
    }
}
