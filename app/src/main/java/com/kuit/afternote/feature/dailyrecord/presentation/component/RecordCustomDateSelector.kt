import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.feature.dailyrecord.presentation.component.DropdownSelector

/**
 * 날짜 선택 하는 거
 * 다이얼로그로 하면 열리는 날짜 선택기
 */
@Composable
fun RecordCustomDateSelector(
    onDateSelected2: Int,
    onDateSelected1: Int,
    onDateSelected: (Int, Int, Int) -> Unit
) {
    var selectedYear by remember { mutableStateOf(2025) }
    var selectedMonth by remember { mutableStateOf(12) }
    var selectedDay by remember { mutableStateOf(30) }

    val years = (2020..2030).toList()
    val months = (1..12).toList()
    val days = (1..31).toList()

    Column {
        Row {
            DropdownSelector("년", years, selectedYear) { selectedYear = it }
            DropdownSelector("월", months, selectedMonth) { selectedMonth = it }
            DropdownSelector("일", days, selectedDay) { selectedDay = it }
        }

        Text(
            text = "확인",
            modifier = Modifier
                .clickable {
                    onDateSelected(selectedYear, selectedMonth, selectedDay)
                }
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Prevee() {
    RecordCustomDateSelector(
        onDateSelected2 = 2025,
        onDateSelected1 = 1,
        onDateSelected = { _, _, _ -> }
    )
}
