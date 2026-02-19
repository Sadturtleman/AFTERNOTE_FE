package com.kuit.afternote.feature.timeletter.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.kuit.afternote.R

/**
 * Parameters for [TimeLetterWriterBottomBar].
 */
data class TimeLetterWriterBottomBarParams(
    val draftCount: Int,
    val onLinkClick: () -> Unit,
    val onAddClick: () -> Unit,
    val onSaveDraftClick: () -> Unit,
    val onDraftCountClick: () -> Unit,
    val isMenuOpen: Boolean,
    val onMenuDismiss: () -> Unit,
    val onImageAddClick: () -> Unit,
    val onFileAddClick: () -> Unit,
    val onVoiceAddClick: () -> Unit,
    val onLinkAddClick: () -> Unit
)

/**
 * 타임레터 작성 화면 하단 바
 *
 * @param params [TimeLetterWriterBottomBarParams]
 * @param modifier Modifier
 */
@Composable
fun TimeLetterWriterBottomBar(
    modifier: Modifier = Modifier,
    params: TimeLetterWriterBottomBarParams
) {
    val p = params
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(88.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.ic_link),
                contentDescription = "링크",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { p.onLinkClick() }
            )
        }
        Spacer(Modifier.width(16.dp))
        Box {
            Image(
                painter = painterResource(R.drawable.ic_additional),
                contentDescription = "더보기",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { p.onAddClick() }
            )
            if (p.isMenuOpen) {
                Popup(
                    popupPositionProvider = DropUpPositionProvider(yOffset = 16),
                    onDismissRequest = p.onMenuDismiss,
                    properties = PopupProperties(focusable = true)
                ) {
                    WritingPlusMenu(
                        onImageClick = {
                            p.onMenuDismiss()
                            p.onImageAddClick()
                        },
                        onVoiceClick = p.onVoiceAddClick,
                        onFileClick = p.onFileAddClick,
                        onLinkClick = p.onLinkAddClick
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))
        Text(
            text = "임시저장",
            modifier = Modifier
                .clickable { p.onSaveDraftClick() },
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            lineHeight = 22.sp,
            color = Color(0xFF9E9E9E)
        )
        Spacer(Modifier.width(16.dp))
        Image(
            painterResource(R.drawable.ic_radio_bar),
            contentDescription = "라디오 바",
            modifier = Modifier
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = p.draftCount.toString(),
            modifier = Modifier
                .clickable { p.onDraftCountClick() },
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            fontFamily = FontFamily(Font(R.font.sansneoregular)),
            lineHeight = 22.sp,
            color = Color(0xFF9E9E9E)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeLetterWriterBottomBarPreview() {
    TimeLetterWriterBottomBar(
        params = TimeLetterWriterBottomBarParams(
            draftCount = 3,
            onLinkClick = {},
            onAddClick = {},
            onSaveDraftClick = {},
            onDraftCountClick = {},
            isMenuOpen = false,
            onMenuDismiss = {},
            onImageAddClick = {},
            onFileAddClick = {},
            onVoiceAddClick = {},
            onLinkAddClick = {}
        )
    )
}
