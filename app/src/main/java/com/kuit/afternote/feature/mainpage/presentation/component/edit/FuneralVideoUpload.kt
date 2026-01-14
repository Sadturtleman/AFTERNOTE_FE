package com.kuit.afternote.feature.mainpage.presentation.component.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.kuit.afternote.ui.theme.White

/**
 * 장례식에 남길 영상 추가 컴포넌트
 *
 * 피그마 디자인 기반:
 * - 라벨: 12sp, Regular, Gray9
 * - 라벨과 버튼 간 간격: 6dp
 * - 큰 원형 버튼: B3 (#BDE0FF), 120dp
 * - 플러스 아이콘: 중앙에 위치, 24dp
 */
@Composable
fun FuneralVideoUpload(
    modifier: Modifier = Modifier,
    label: String = "장례식에 남길 영상",
    videoUrl: String? = null,
    onAddVideoClick: () -> Unit
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
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(color = White, shape = RoundedCornerShape(size = 16.dp))
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_add_circle),
                contentDescription = "영상 추가",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FuneralVideoUploadPreview() {
    AfternoteTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 영상 없음
            FuneralVideoUpload(
                onAddVideoClick = {}
            )

            // 영상 있음 (TODO: 썸네일 표시 구현 필요)
            FuneralVideoUpload(
                videoUrl = "test",
                onAddVideoClick = {}
            )
        }
    }
}
