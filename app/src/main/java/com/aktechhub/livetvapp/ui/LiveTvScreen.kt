package com.aktechhub.livetvapp.ui

import android.app.Activity
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
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
import com.aktechhub.livetvapp.menu.LiveTVMenuScreen
import com.aktechhub.livetvapp.model.Channel
import com.aktechhub.livetvapp.model.Genre
import com.aktechhub.livetvapp.model.Language
import com.aktechhub.livetvapp.remote.ChannelRepository
import kotlin.math.abs

@OptIn(UnstableApi::class)
@Composable
fun LiveTvScreen(onExit: () -> Unit) {
    val context = LocalContext.current
    var channels by remember { mutableStateOf<List<Channel>>(emptyList()) }
    var genres by remember { mutableStateOf<List<Genre>>(emptyList()) }
    var languages by remember { mutableStateOf<List<Language>>(emptyList()) }
    var currentChannelIndex by remember { mutableIntStateOf(0) }
    var showChannelDetail by remember { mutableStateOf(false) }

    rememberCoroutineScope()

    // Fetch channels from API
    LaunchedEffect(Unit) {
        channels = ChannelRepository.getChannels()
        genres = ChannelRepository.getGenres()
        languages = ChannelRepository.getLanguages()
    }


    // ExoPlayer Setup
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            // Ensure playback starts automatically when media is set
            playWhenReady = true
        }
    }

    // Automatically set and play the first channel when channels are loaded
    LaunchedEffect(channels) {
        if (channels.isNotEmpty() && !exoPlayer.isPlaying) {
            exoPlayer.apply {
                setMediaItem(MediaItem.fromUri(channels[currentChannelIndex].streamUrl))
                prepare()
                play() // Explicitly call play() to ensure it starts
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

    val swipeThreshold = 100f
    val swipeCooldown = 300L
    var lastSwipeTime by remember { mutableLongStateOf(0L) }

    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(modifier = Modifier.fillMaxSize()) {

        // LEFT
        LiveTVMenuScreen(
            modifier = Modifier
                .weight(0.50f),
            languages,
            genres,
            channels,
            onChannelSelect = {},

        )

        // RIGHT
        Column(
            modifier = Modifier
                .weight(0.50f)
                .fillMaxSize()
                .background(Color.Black) // Set the entire right side background to black
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(Color.Black)
                    .border(0.5.dp, color = LightGray)
                    .focusRequester(focusRequester)
                    .focusable(true)
                    .onFocusChanged { focusState -> hasFocus = focusState.hasFocus }
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragEnd = { lastSwipeTime = System.currentTimeMillis() },
                            onVerticalDrag = { _, dragAmount ->
                                val currentTime = System.currentTimeMillis()
                                if (channels.isNotEmpty() && abs(dragAmount) > swipeThreshold &&
                                    (currentTime - lastSwipeTime) > swipeCooldown
                                ) {
                                    changeChannel(
                                        dragAmount,
                                        currentChannelIndex,
                                        channels,
                                        exoPlayer
                                    ) { newIndex ->
                                        currentChannelIndex = newIndex
                                        showChannelDetail = true
                                    }
                                    lastSwipeTime = currentTime
                                }
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTapGestures {
                            showChannelDetail = true
                        }
                    }
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.nativeKeyEvent.action != KeyEvent.ACTION_DOWN) return@onKeyEvent false

                        when (keyEvent.key) {
                            Key.DirectionDown -> {
                                changeChannel(
                                    1f,
                                    currentChannelIndex,
                                    channels,
                                    exoPlayer
                                ) { newIndex ->
                                    currentChannelIndex = newIndex
                                    showChannelDetail = true
                                }
                                true
                            }

                            Key.DirectionUp -> {
                                changeChannel(
                                    -1f,
                                    currentChannelIndex,
                                    channels,
                                    exoPlayer
                                ) { newIndex ->
                                    currentChannelIndex = newIndex
                                    showChannelDetail = true
                                }
                                true
                            }

                            Key.Enter, Key.NumPadEnter -> {
                                showChannelDetail = true
                                true
                            }

                            else -> {
                                if (keyEvent.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                                    showChannelDetail = true
                                    return@onKeyEvent true
                                }
                                false
                            }
                        }
                    }
            ) {
                if (channels.isNotEmpty()) {
                    AndroidView(
                        factory = {
                            PlayerView(context).apply {
                                player = exoPlayer
                                useController = false
                                layoutParams = FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    if (showChannelDetail) {
                        val currentChannel = channels[currentChannelIndex]
                        ChannelDetailScreen(
                            channelNumber = currentChannel.channelNumber.toString(),
                            channelName = currentChannel.channelName,
                            channelLogoUrl = currentChannel.logoUrl,
                            onTimeout = { showChannelDetail = false }
                        )
                    }
                }
            }

            Image(
                painter = painterResource(id = R.drawable.adver), // Use a local resource
                // Or use Coil for a URL: painter = rememberAsyncImagePainter("https://example.com/image.jpg"),
                contentDescription = "Background Image",
                modifier = Modifier
                    .weight(1f)
                    .border(0.5.dp, color = LightGray),
                contentScale = ContentScale.Crop // Adjust the image scaling (Crop, Fit, etc.)
            )


        }



        BackHandler {
            onExit()
        }
    }
}

// Channel Switching Function
fun changeChannel(
    dragAmount: Float,
    currentIndex: Int,
    channels: List<Channel>,
    exoPlayer: ExoPlayer,
    onChannelChanged: (Int) -> Unit
) {
    if (channels.isEmpty()) return

    val newIndex = when {
        dragAmount > 0 && currentIndex < channels.size - 1 -> currentIndex + 1
        dragAmount < 0 && currentIndex > 0 -> currentIndex - 1
        else -> return
    }

    exoPlayer.apply {
        setMediaItem(MediaItem.fromUri(channels[newIndex].streamUrl))
        prepare()
        play()
    }
    onChannelChanged(newIndex)
}
