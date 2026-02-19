package com.bear.englishlearning.ui.components

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun YouTubePlayerComposable(
    videoId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val lifecycleOwner = LocalLifecycleOwner.current

    var playerView by remember { mutableStateOf<YouTubePlayerView?>(null) }
    var fullScreenView by remember { mutableStateOf<View?>(null) }
    var youTubePlayer by remember { mutableStateOf<YouTubePlayer?>(null) }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { ctx ->
            YouTubePlayerView(ctx).apply {
                enableAutomaticInitialization = false
                lifecycleOwner.lifecycle.addObserver(this)

                val options = IFramePlayerOptions.Builder()
                    .controls(1)
                    .fullscreen(1)
                    .build()

                initialize(object : AbstractYouTubePlayerListener() {
                    override fun onReady(player: YouTubePlayer) {
                        youTubePlayer = player
                        player.cueVideo(videoId, 0f)
                    }
                }, options)

                addFullscreenListener(object : FullscreenListener {
                    override fun onEnterFullscreen(
                        fullscreenViewParam: View,
                        exitFullscreen: () -> Unit
                    ) {
                        val decor = activity?.window?.decorView as? ViewGroup ?: return
                        decor.addView(
                            fullscreenViewParam,
                            ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        )
                        fullScreenView = fullscreenViewParam
                    }

                    override fun onExitFullscreen() {
                        val decor = activity?.window?.decorView as? ViewGroup ?: return
                        fullScreenView?.let { decor.removeView(it) }
                        fullScreenView = null
                    }
                })

                playerView = this
            }
        },
        update = {
            youTubePlayer?.cueVideo(videoId, 0f)
        }
    )

    DisposableEffect(lifecycleOwner) {
        onDispose {
            val decor = activity?.window?.decorView as? ViewGroup
            fullScreenView?.let { decor?.removeView(it) }
            fullScreenView = null
            playerView?.release()
            playerView = null
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
