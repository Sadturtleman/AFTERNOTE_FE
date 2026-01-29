
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.kuit.afternote.ui.theme.B2
import com.kuit.afternote.ui.theme.Gray4
import com.kuit.afternote.ui.theme.Sansneo

@Composable
fun ReceiverRecordListRow(
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
            Image(
                painter = painterResource(R.drawable.img_recipient_profile),
                contentDescription = "프로필 사진",
                modifier = Modifier.size(52.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. 이름
            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                fontFamily = Sansneo
            )

            // 3. 화살표 아이콘
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "이동",
                tint = B2,
                modifier = Modifier.size(14.dp)
            )
        }

        // 4. 하단 구분선
        HorizontalDivider(
            thickness = 1.dp,
            color = Gray4 // 연한 회색
        )
    }
}

@Preview
@Composable
fun PreviewProfileItem() {
    ReceiverRecordListRow(name = "박서연")
}
