package com.kuit.afternote.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuit.afternote.core.ui.component.button.AddFloatingActionButton
import com.kuit.afternote.ui.theme.AfternoteTheme

/**
 * Scaffold content area: full-size Box with [paddingValues], optional inner [contentPadding],
 * a content slot, and an optional FAB.
 *
 * Use inside Scaffold's content lambda so callers do not repeat the Box + padding + FAB pattern.
 */
@Composable
fun ScaffoldContentWithOptionalFab(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    contentPadding: PaddingValues = PaddingValues(start = 20.dp, end = 20.dp, bottom = 16.dp),
    showFab: Boolean = false,
    onFabClick: () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(contentPadding)
    ) {
        content(Modifier)
        if (showFab) {
            AddFloatingActionButton(onClick = onFabClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScaffoldContentWithOptionalFabPreview() {
    AfternoteTheme {
        ScaffoldContentWithOptionalFab(
            paddingValues = PaddingValues(),
            showFab = true,
            onFabClick = {},
            content = { mod -> Box(modifier = mod.fillMaxSize()) {} }
        )
    }
}
