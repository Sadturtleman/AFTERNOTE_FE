package com.kuit.afternote.feature.afternote.presentation.component.edit.processingmethod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.kuit.afternote.core.ui.component.SelectableRadioCard
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.AccountProcessingMethod
import com.kuit.afternote.feature.afternote.presentation.component.edit.model.ProcessingMethodOption
import com.kuit.afternote.ui.theme.AfternoteTheme
import com.kuit.afternote.ui.theme.B1
import com.kuit.afternote.ui.theme.Gray6
import com.kuit.afternote.ui.theme.Gray9
import com.kuit.afternote.ui.theme.Sansneo

/**
 * Title + description content for use inside [SelectableRadioCard].
 * Figma: title 16sp Medium (selected B1 / unselected Gray9), description 14sp Regular Gray6, 6.dp spacing.
 */
@Composable
fun OptionRadioCardContent(
    option: ProcessingMethodOption,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = option.title,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Medium,
                color = if (selected) B1 else Gray9
            )
        )
        Text(
            text = option.description,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontFamily = Sansneo,
                fontWeight = FontWeight.Normal,
                color = Gray6
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OptionRadioCardContentPreview() {
    AfternoteTheme {
        SelectableRadioCard(
            selected = true,
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            content = {
                OptionRadioCardContent(
                    option = AccountProcessingMethod.MEMORIAL_ACCOUNT,
                    selected = true
                )
            }
        )
    }
}
