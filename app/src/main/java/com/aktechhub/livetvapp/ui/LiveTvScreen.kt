package com.aktechhub.livetvapp.ui


import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.aktechhub.livetvapp.repository.Channel
import com.aktechhub.livetvapp.repository.ChannelRepository

@Composable
fun LiveTvScreen(onExit: () -> Unit) {
    val context = LocalContext.current
    val channels by remember { mutableStateOf(ChannelRepository.getChannels()) }
    var currentChannelIndex by remember { mutableIntStateOf(0) }
    var showChannelDetail by remember { mutableStateOf(false) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(channels[currentChannelIndex].streamUrl)) // FIXED
            prepare()
            play()
        }
    }

    DisposableEffect(Unit) {
        val window = (context as? android.app.Activity)?.window
        window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onDispose {
            exoPlayer.release()
            window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focusRequester)
            .focusable(true)
            .onFocusChanged { focusState -> hasFocus = focusState.hasFocus }
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = { lastSwipeTime = System.currentTimeMillis() },
                    onVerticalDrag = { _, dragAmount ->
                        val currentTime = System.currentTimeMillis()
                        if (kotlin.math.abs(dragAmount) > swipeThreshold &&
                            (currentTime - lastSwipeTime) > swipeCooldown
                        ) {
                            changeChannel(dragAmount, currentChannelIndex, channels, exoPlayer) { newIndex ->
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
                if (keyEvent.nativeKeyEvent.action != android.view.KeyEvent.ACTION_DOWN) return@onKeyEvent false

                when (keyEvent.key) {
                    Key.DirectionDown -> {
                        changeChannel(1f, currentChannelIndex, channels, exoPlayer) { newIndex ->
                            currentChannelIndex = newIndex
                            showChannelDetail = true
                        }
                        true
                    }
                    Key.DirectionUp -> {
                        changeChannel(-1f, currentChannelIndex, channels, exoPlayer) { newIndex ->
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
                        if (keyEvent.nativeKeyEvent.keyCode == android.view.KeyEvent.KEYCODE_DPAD_CENTER) {
                            showChannelDetail = true
                            return@onKeyEvent true
                        }
                        false
                    }
                }
            }
    ) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // FIXED: Define currentChannel before using it
        val currentChannel = channels[currentChannelIndex]

        if (showChannelDetail) {
            ChannelDetailScreen(
                channelNumber = currentChannel.number,
                channelName = currentChannel.name,
                channelLogoUrl = currentChannel.logoUrl,
                onTimeout = { showChannelDetail = false }
            )
        }
    }

    BackHandler {
        onExit()
    }
}

// FIXED: Use List<Channel> instead of List<String>
fun changeChannel(
    dragAmount: Float,
    currentIndex: Int,
    channels: List<Channel>,
    exoPlayer: ExoPlayer,
    onChannelChanged: (Int) -> Unit
) {
    val newIndex = when {
        dragAmount > 0 && currentIndex < channels.size - 1 -> currentIndex + 1
        dragAmount < 0 && currentIndex > 0 -> currentIndex - 1
        else -> return
    }

    exoPlayer.apply {
        setMediaItem(MediaItem.fromUri(channels[newIndex].streamUrl)) // FIXED
        prepare()
        play()
    }
    onChannelChanged(newIndex)
}
