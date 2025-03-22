package com.aktechhub.livetvapp.ui

import android.content.res.Configuration
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.aktechhub.livetvapp.repository.ChannelRepository

@Composable
fun LiveTvScreen(onExit: () -> Unit) {
    val context = LocalContext.current
    val isTv = context.resources.configuration.uiMode and
            Configuration.UI_MODE_TYPE_MASK == Configuration.UI_MODE_TYPE_TELEVISION

    val channels by remember { mutableStateOf(ChannelRepository.getChannels()) }

    var currentChannelIndex by remember { mutableIntStateOf(0) }
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(channels[currentChannelIndex]))
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

    val swipeThreshold = 100f // Minimum swipe distance to trigger channel change
    val swipeCooldown = 300L  // Time delay to prevent multiple changes in one swipe

    var lastSwipeTime by remember { mutableLongStateOf(0L) } // Stores last swipe time

    // ** Fix: Request focus for TV remote **
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .focusRequester(focusRequester) // Request focus
            .focusable(true) // Allow focus
            .onFocusChanged { focusState ->
                hasFocus = focusState.hasFocus
            }
            .pointerInput(Unit) {
                if (!isTv) { // Touch gestures only for mobile
                    detectVerticalDragGestures(
                        onDragEnd = { lastSwipeTime = System.currentTimeMillis() },
                        onVerticalDrag = { _, dragAmount ->
                            val currentTime = System.currentTimeMillis()
                            if (kotlin.math.abs(dragAmount) > swipeThreshold &&
                                (currentTime - lastSwipeTime) > swipeCooldown
                            ) {
                                changeChannel(dragAmount, currentChannelIndex, channels, exoPlayer) { newIndex ->
                                    currentChannelIndex = newIndex
                                }
                                lastSwipeTime = currentTime
                            }
                        }
                    )
                }
            }
            .onKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.action != android.view.KeyEvent.ACTION_DOWN) return@onKeyEvent false

                when (keyEvent.key) {
                    Key.DirectionDown -> {
                        changeChannel(1f, currentChannelIndex, channels, exoPlayer) { newIndex ->
                            currentChannelIndex = newIndex
                        }
                        true
                    }
                    Key.DirectionUp -> {
                        changeChannel(-1f, currentChannelIndex, channels, exoPlayer) { newIndex ->
                            currentChannelIndex = newIndex
                        }
                        true
                    }
                    else -> false
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

        Text(
            text = "Channel ${currentChannelIndex + 1}",
            color = Color.White,
            fontSize = if (isTv) 32.sp else 20.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.7f))
        )
    }

    // ** Fix: Handle back button properly **
    BackHandler {
        onExit()
    }
}

fun changeChannel(
    dragAmount: Float,
    currentIndex: Int,
    channels: List<String>,
    exoPlayer: ExoPlayer,
    onChannelChanged: (Int) -> Unit
) {
    val newIndex = when {
        dragAmount > 0 && currentIndex < channels.size - 1 -> currentIndex + 1
        dragAmount < 0 && currentIndex > 0 -> currentIndex - 1
        else -> return
    }

    exoPlayer.apply {
        setMediaItem(MediaItem.fromUri(channels[newIndex]))
        prepare()
        play()
    }
    onChannelChanged(newIndex)
}
