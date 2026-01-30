package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * Slot API for [MemorialGuidelineContent]: one composable per section.
 * Edit and view (receiver) screens fill these with their own UI.
 */
data class MemorialGuidelineSlots(
    val introContent: @Composable () -> Unit,
    val photoContent: @Composable () -> Unit,
    val playlistContent: @Composable () -> Unit,
    val lastWishContent: @Composable () -> Unit,
    val videoContent: @Composable () -> Unit
)

/**
 * Shared memorial guideline layout: section order and spacing only.
 * Used by mainpage (edit) and receiver (view) to avoid duplicate structure.
 *
 * @param modifier Modifier for the root Column
 * @param slots Content for each section (intro, photo, playlist, last wish, video)
 * @param sectionSpacing Vertical spacing between sections
 * @param trailingSpacerHeight Optional bottom spacer (e.g. viewport-based in edit)
 */
@Composable
fun MemorialGuidelineContent(
    modifier: Modifier = Modifier,
    slots: MemorialGuidelineSlots,
    sectionSpacing: Dp = 32.dp,
    trailingSpacerHeight: Dp = 0.dp
) {
    Column(modifier = modifier.fillMaxWidth()) {
        slots.introContent()
        Spacer(modifier = Modifier.height(sectionSpacing))
        slots.photoContent()
        Spacer(modifier = Modifier.height(sectionSpacing))
        slots.playlistContent()
        Spacer(modifier = Modifier.height(sectionSpacing))
        slots.lastWishContent()
        Spacer(modifier = Modifier.height(sectionSpacing))
        slots.videoContent()
        if (trailingSpacerHeight > 0.dp) {
            Spacer(modifier = Modifier.height(trailingSpacerHeight))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemorialGuidelineContentPreview() {
    AfternoteTheme {
        MemorialGuidelineContent(
            slots = MemorialGuidelineSlots(
                introContent = { },
                photoContent = { },
                playlistContent = { },
                lastWishContent = { },
                videoContent = { }
            )
        )
    }
}
