package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
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
    imageUrl: String? = null,
    onAddPhotoClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
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

//        Box(
//            modifier = Modifier.size(120.dp),
//            contentAlignment = Alignment.Center
//        ) {
        // 큰 원형 배경
        // 프로필 아이콘 (이미지가 없을 때만 표시)
        if (imageUrl == null) {
            Image(
                painter = painterResource(R.drawable.img_deceased_profile),
                contentDescription = "영정사진 기본 프로필",
                modifier = Modifier
                    .size(144.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            // TODO: 실제 이미지 로드 (Coil 등 사용)
        }
//        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemorialPhotoUploadPreview() {
    AfternoteTheme {
        Column(
//            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 이미지 없음
            MemorialPhotoUpload(
                onAddPhotoClick = {}
            )

            // 이미지 있음 (TODO: 실제 이미지 로드 구현 필요)
            MemorialPhotoUpload(
                imageUrl = "test",
                onAddPhotoClick = {}
            )
        }
    }
}
