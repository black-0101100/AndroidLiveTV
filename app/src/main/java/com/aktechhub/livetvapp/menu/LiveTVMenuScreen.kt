package com.aktechhub.livetvapp.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.aktechhub.livetvapp.model.Channel
import com.aktechhub.livetvapp.model.Genre
import com.aktechhub.livetvapp.model.Language
import com.aktechhub.livetvapp.remote.LocalRepository.channels



@Composable
fun LiveTVMenuScreen(
    modifier: Modifier = Modifier,
    languages: List<Language>,
    genres: List<Genre>,
    channels: List<Channel>,
    onChannelSelect: (Int) -> Unit
) {
    // State for selected Language & Genre
    var selectedLanguageId by remember { mutableIntStateOf(languages.firstOrNull()?.languageId ?: 0) }
    var selectedGenreId by remember { mutableIntStateOf(genres.firstOrNull()?.genreId ?: 0) }

    // Filter channels based on selected language & genre
    val filteredChannels = channels.filter {
        it.languageId == selectedLanguageId && it.genreId == selectedGenreId
    }

    Row(
        modifier = modifier.fillMaxSize().background(Color.Black)
    ) {
        // Language List UI
        LanguageList(
            languages = languages,
            onSelect = { selectedLanguageId = it }, // Update selected Language
            modifier = Modifier.weight(1f)
        )

        // Genre List UI
        GenreList(
            genres = genres,
            onSelect = { selectedGenreId = it }, // Update selected Genre
            modifier = Modifier.weight(1f)
        )

        // Channel List UI - Only show filtered channels
        ChannelList(
            channels = filteredChannels, // Pass only filtered channels
            onSelect = onChannelSelect,
            modifier = Modifier.weight(1f)
        )
    }
}

