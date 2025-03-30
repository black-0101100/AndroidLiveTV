package com.aktechhub.livetvapp.ui

import android.app.Activity
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.aktechhub.livetvapp.model.Channel
import com.aktechhub.livetvapp.model.Genre
import com.aktechhub.livetvapp.model.Language
import com.aktechhub.livetvapp.remote.ChannelRepository
import kotlin.math.abs

@OptIn(UnstableApi::class)
@Composable
fun LiveTvScreen(onExit: () -> Unit) {
    val context = LocalContext.current

    var channels by remember { mutableStateOf(emptyList<Channel>()) }
    var genres by remember { mutableStateOf(emptyList<Genre>()) }
    var languages by remember { mutableStateOf(emptyList<Language>()) }
    var currentChannel by remember { mutableStateOf<Channel?>(null) }
    var showChannelDetail by remember { mutableStateOf(false) }
    var isFullScreen by remember { mutableStateOf(true) }

    val swipeThreshold = 100f
    val swipeCooldown = 300L
    var lastSwipeTime by remember { mutableLongStateOf(0L) }

    var selectedLanguageId by remember { mutableIntStateOf(0) }
    var selectedGenreId by remember { mutableIntStateOf(0) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            playWhenReady = true
        }
    }

    LaunchedEffect(Unit) {
        val loadedLanguages = ChannelRepository.getLanguages().sortedBy { it.languageId }
        val loadedGenres = ChannelRepository.getGenres().sortedBy { it.genreId }
        val loadedChannels = ChannelRepository.getChannels().sortedBy { it.channelNumber }

        languages = loadedLanguages
        genres = loadedGenres
        channels = loadedChannels

        if (loadedLanguages.isNotEmpty()) {
            selectedLanguageId = loadedLanguages.first().languageId
        }

        val initialGenres = loadedGenres.filter { it.languageId == selectedLanguageId }
        if (initialGenres.isNotEmpty()) {
            selectedGenreId = initialGenres.first().genreId
        }

        val initialChannels = loadedChannels.filter {
            it.languageId == selectedLanguageId && it.genreId == selectedGenreId
        }
        if (initialChannels.isNotEmpty()) {
            currentChannel = initialChannels.first()
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

    val filteredChannels = channels.filter {
        it.languageId == selectedLanguageId && it.genreId == selectedGenreId
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
                                .clickable { selectedLanguageId = language.languageId }
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
                                .clickable { selectedGenreId = genre.genreId }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.DarkGray)
                ) {
                    filteredChannels.forEach { channel ->
                        Text(
                            text = channel.channelName,
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
                                }
                        )
                    }
                }
            }
        }

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
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragEnd = { lastSwipeTime = System.currentTimeMillis() },
                            onVerticalDrag = { _, dragAmount ->
                                val currentTime = System.currentTimeMillis()
                                if (filteredChannels.isNotEmpty() &&
                                    abs(dragAmount) > swipeThreshold &&
                                    (currentTime - lastSwipeTime) > swipeCooldown
                                ) {
                                    changeChannel(
                                        dragAmount,
                                        filteredChannels.indexOf(currentChannel),
                                        filteredChannels,
                                        exoPlayer
                                    ) { newChannel ->
                                        currentChannel = newChannel
                                        showChannelDetail = true
                                    }
                                    lastSwipeTime = currentTime
                                }
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
        }
    }

    BackHandler {
        onExit()
    }
}

fun changeChannel(
    dragAmount: Float,
    currentIndex: Int,
    channels: List<Channel>,
    exoPlayer: ExoPlayer,
    onChannelChanged: (Channel) -> Unit
) {
    if (channels.isEmpty()) return

    val newIndex = when {
        dragAmount > 0 && currentIndex < channels.size - 1 -> currentIndex + 1  // Swipe Down (Next Channel)
        dragAmount < 0 && currentIndex > 0 -> currentIndex - 1  // Swipe Up (Previous Channel)
        else -> return
    }

    val newChannel = channels[newIndex]
    exoPlayer.apply {
        setMediaItem(MediaItem.fromUri(newChannel.streamUrl))
        prepare()
        play()
    }
    onChannelChanged(newChannel)
}