package com.aktechhub.livetvapp.ui

import android.app.Activity
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.aktechhub.livetvapp.R
import com.aktechhub.livetvapp.model.Channel
import com.aktechhub.livetvapp.model.Genre
import com.aktechhub.livetvapp.model.Language
import com.aktechhub.livetvapp.remote.ChannelRepository

@OptIn(UnstableApi::class)
@Composable
fun LiveTvScreen(onExit: () -> Unit) {
    val context = LocalContext.current

    var allChannels by remember { mutableStateOf(emptyList<Channel>()) }
    var genres by remember { mutableStateOf(emptyList<Genre>()) }
    var languages by remember { mutableStateOf(emptyList<Language>()) }
    var currentChannel by remember { mutableStateOf<Channel?>(null) }
    var showChannelDetail by remember { mutableStateOf(false) }
    var isFullScreen by remember { mutableStateOf(true) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
        }
    }

    var selectedLanguageId by remember { mutableIntStateOf(0) }
    var selectedGenreId by remember { mutableIntStateOf(0) }

    // Fetch Data Properly
    LaunchedEffect(Unit) {
        val loadedLanguages = ChannelRepository.getLanguages().sortedBy { it.languageId }
        val loadedGenres = ChannelRepository.getGenres().sortedBy { it.genreId }
        val loadedChannels = ChannelRepository.getChannels().sortedBy { it.channelNumber }

        languages = loadedLanguages
        genres = loadedGenres
        allChannels = loadedChannels

        if (loadedLanguages.isNotEmpty()) {
            selectedLanguageId = loadedLanguages.first().languageId
        }
    }

    //  Set Genre After Language Updates
    LaunchedEffect(selectedLanguageId) {
        if (selectedLanguageId != 0) {
            val filteredGenres = genres.filter { it.languageId == selectedLanguageId }
            if (filteredGenres.isNotEmpty()) {
                selectedGenreId = filteredGenres.first().genreId
            }
        }
    }

    //  Filter Channels Properly
    val filteredChannels by remember {
        derivedStateOf {
            val channels = allChannels.filter {
                it.streamLanguageId == selectedLanguageId && it.streamGenreId == selectedGenreId
            }
            channels
        }
    }

    //  Set Initial Channel After Genre Updates
    LaunchedEffect(selectedGenreId) {
        if (selectedGenreId != 0 && filteredChannels.isNotEmpty()) {
            currentChannel = filteredChannels.first()
            showChannelDetail = true
            exoPlayer.apply {
                setMediaItem(MediaItem.fromUri(currentChannel!!.streamUrl))
                prepare()
                play()
            }
        }
    }

    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onDispose {
            exoPlayer.release()
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        if (!isFullScreen) {
            Row(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight()
                    .background(Color.DarkGray)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.DarkGray)
                ) {
                    languages.forEach { language ->
                        Text(
                            text = language.languageName,
                            color = if (language.languageId == selectedLanguageId) Color.Blue else Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    selectedLanguageId = language.languageId
                                }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.DarkGray)
                ) {
                        genres.filter { it.languageId == selectedLanguageId }.forEach { genre ->
                            Text(
                                text = genre.genreName,
                                color = if (genre.genreId == selectedGenreId) Color.Yellow else Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        selectedGenreId = genre.genreId
                                    }
                            )
                        }
                }


                val configuration = LocalConfiguration.current
                val isTv = configuration.smallestScreenWidthDp >= 600 // Adjust threshold for TV detection

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.DarkGray)

                ) {
                    if (filteredChannels.isEmpty()) {
                        Text(
                            text = "No channels available",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        filteredChannels.forEach { channel ->
                            Text(
                                text = "${channel.channelNumber} - ${channel.channelName}",
                                color = if (channel == currentChannel) Color.Green else Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        currentChannel = channel
                                        exoPlayer.apply {
                                            setMediaItem(MediaItem.fromUri(channel.streamUrl))
                                            prepare()
                                            play()
                                        }
                                        showChannelDetail = true

                                        if (isTv) {
                                            isFullScreen = true
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }

        var lastOkPressTime by remember { mutableLongStateOf(0L) }
        val doublePressThreshold = 300L // Time window for detecting double press (in ms)

        Column(
            modifier = Modifier
                .weight(if (isFullScreen) 1f else 0.5f)
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black)
                    .focusable()
                    .onKeyEvent { event ->
                        if (event.type == KeyEventType.KeyUp && event.key == Key.Enter || event.key == Key.DirectionCenter) {
                            val currentTime = System.currentTimeMillis()
                            if (currentTime - lastOkPressTime < doublePressThreshold) {
                                // Double press detected → Exit full-screen mode
                                isFullScreen = false
                            } else {
                                // Single press detected → Show channel details
                                showChannelDetail = true
                            }
                            lastOkPressTime = currentTime
                            true
                        } else {
                            false
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                if (!isFullScreen) {
                                    isFullScreen = true
                                } else {
                                    showChannelDetail = true
                                }
                            },
                            onDoubleTap = {
                                isFullScreen = false
                            }
                        )
                    }
            ) {
                if (currentChannel != null) {
                    AndroidView(
                        factory = {
                            PlayerView(context).apply {
                                player = exoPlayer
                                useController = false
                                layoutParams = FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.MATCH_PARENT,
                                    FrameLayout.LayoutParams.MATCH_PARENT
                                )
                                setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (showChannelDetail) {
                        ChannelDetailScreen(
                            channelNumber = currentChannel!!.channelNumber.toString(),
                            channelName = currentChannel!!.channelName,
                            channelLogoUrl = currentChannel!!.logoUrl,
                            onTimeout = { showChannelDetail = false }
                        )
                    }
                }
            }

            if (!isFullScreen) {
                Image(
                    painter = painterResource(id = R.drawable.adver),
                    contentDescription = "Advertisement",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border(0.5.dp, color = LightGray)
                        .background(Color.Blue),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    BackHandler {
        onExit()
    }
}