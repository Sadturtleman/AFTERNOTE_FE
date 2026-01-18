package com.kuit.afternote.feature.onboarding.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuit.afternote.R
import com.kuit.afternote.core.ui.component.OutlineTextField
import com.kuit.afternote.ui.theme.AfternoteTheme

@Composable
fun IdentifyInputContent(
    firstPart: String,
    onFirstPartChange: (String) -> Unit,
    secondPart: String,
    onSecondPartChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFFF8F9FA), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 앞자리 입력 (6자리)
        BasicTextField(
            value = firstPart,
            onValueChange = { if (it.length <= 6) onFirstPartChange(it) },
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = { innerTextField ->
                if (firstPart.isEmpty()) {
                    Text("Text Field", color = Color.LightGray, fontSize = 18.sp)
                }
                innerTextField()
            }
        )

        Text("—", modifier = Modifier.padding(horizontal = 8.dp), color = Color.Black)

        // 뒷자리 첫글자 입력 (1자리)
        BasicTextField(
            value = secondPart,
            onValueChange = { if (it.length <= 1) onSecondPartChange(it) },
            modifier = Modifier.width(20.dp),
            textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = { innerTextField ->
                if (secondPart.isEmpty()) {
                    Text("T", color = Color.LightGray, fontSize = 18.sp)
                }
                innerTextField()
            }
        )

        // 마스킹 동그라미 (6개)
        Row(
            modifier = Modifier.padding(start = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(6) {
                Surface(
                    modifier = Modifier.size(12.dp),
                    shape = RoundedCornerShape(50),
                    color = Color(0xFF333333)
                ) {}
            }
        }
    }
}
