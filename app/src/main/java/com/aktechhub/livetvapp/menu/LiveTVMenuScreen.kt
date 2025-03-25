package com.aktechhub.livetvapp.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LiveTVMenuScreen(
    modifier: Modifier = Modifier,
    onLanguageSelect: (Int) -> Unit,
    onCategorySelect: (Int) -> Unit,
    onChannelSelect: (Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxSize().background(Color.Black)
    ) {
        // Language List UI (First)
        LanguageList(onSelect = onLanguageSelect, modifier = Modifier.weight(1f))

        // Category List UI (Second)
        CategoryList(onSelect = onCategorySelect, modifier = Modifier.weight(1f))

        // Channel List UI (Third)
        ChannelList(onSelect = onChannelSelect, modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLiveTVMenuScreen() {
    LiveTVMenuScreen(
        onLanguageSelect = {},
        onCategorySelect = {},
        onChannelSelect = {}
    )
}
