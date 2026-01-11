import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReceiverRow(
    name: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // 전체 클릭 가능
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp), // 내부 여백
            verticalAlignment = Alignment.CenterVertically // 세로 중앙 정렬
        ) {
            // 1. 그라데이션 프로필 아이콘
            Box(
                modifier = Modifier
                    .size(52.dp) // 아이콘 크기
                    .clip(CircleShape) // 원형으로 자르기
                    .background(
                        // 대각선 그라데이션 효과 (하늘색 -> 살구색)
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFD6E6F5), // 연한 하늘색
                                Color(0xFFFFE0D1)  // 연한 살구색
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White, // 아이콘 색상 흰색
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp)) // 아이콘과 이름 사이 간격

            // 2. 이름
            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f) // 남은 공간을 다 차지해서 화살표를 밀어냄
            )

            // 3. 화살표 아이콘
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "이동",
                tint = Color(0xFF6495ED), // 옥수수꽃 파란색 (이미지와 유사한 톤)
                modifier = Modifier.size(28.dp)
            )
        }

        // 4. 하단 구분선
        HorizontalDivider(
            thickness = 1.dp,
            color = Color(0xFFEEEEEE) // 연한 회색
        )
    }
}

@Preview
@Composable
fun PreviewProfileItem() {
    ReceiverRow(name = "박서연")
}
