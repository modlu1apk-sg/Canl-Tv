package com.example.ui.components

import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoPlayer(
    streamUrl: String,
    channelName: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var isMuted by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var retryTrigger by remember { mutableStateOf(0) }
    
    // Maintain a reference to MediaPlayer for mute control
    var mediaPlayerInstance by remember { mutableStateOf<MediaPlayer?>(null) }
    
    // Safely trigger loading state resetting outside of layout/composition pass
    LaunchedEffect(streamUrl, retryTrigger) {
        isLoading = true
        isError = false
        mediaPlayerInstance = null
    }

    Box(
        modifier = modifier
            .background(Color.Black)
            .aspectRatio(16 / 9f)
    ) {
        if (!isError) {
            key(streamUrl + "_$retryTrigger") {
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            // Apply layout parameters
                            layoutParams = android.view.ViewGroup.LayoutParams(
                                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                android.view.ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            
                            // Set prepare, load and error listeners
                            setOnPreparedListener { mp ->
                                mediaPlayerInstance = mp
                                isLoading = false
                                isError = false
                                
                                // Apply mute settings
                                if (isMuted) {
                                    mp.setVolume(0f, 0f)
                                } else {
                                    mp.setVolume(1f, 1f)
                                }
                                
                                mp.start()
                                isPlaying = true
                            }
                            
                            setOnErrorListener { _, what, extra ->
                                isLoading = false
                                isError = true
                                mediaPlayerInstance = null
                                true // return true to handle error
                            }
                            
                            // Standard MediaInfo listeners for buffering
                            setOnInfoListener { _, what, _ ->
                                when (what) {
                                    MediaPlayer.MEDIA_INFO_BUFFERING_START -> {
                                        isLoading = true
                                        true
                                    }
                                    MediaPlayer.MEDIA_INFO_BUFFERING_END -> {
                                        isLoading = false
                                        true
                                    }
                                    else -> false
                                }
                            }
                            
                            // Start stream loading immediately inside factory to avoid update-loop recompositions
                            try {
                                setVideoURI(Uri.parse(streamUrl))
                                requestFocus()
                            } catch (e: Exception) {
                                isLoading = false
                                isError = true
                            }
                        }
                    },
                    update = { _ ->
                        // No-op. The view key recreated the view anyway on changes.
                    },
                    onRelease = { videoView ->
                        try {
                            videoView.setOnPreparedListener(null)
                            videoView.setOnErrorListener(null)
                            videoView.setOnInfoListener(null)
                            videoView.stopPlayback()
                        } catch (e: Exception) {}
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Overlay Header (Channel Name & Close Button)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Şu An Oynatılıyor:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
                Text(
                    text = channelName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            IconButton(
                onClick = onClose,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.2f),
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Oynatmayı Kapat"
                )
            }
        }

        // Loading indicator overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Yayın Bağlantısı Kuruluyor...",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Error overlay with Retry
        if (isError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Yayın Başlatılamadı ⚠️",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Coğrafi engelleme, geçici yayın kesintisi veya zayıf internet bağlantısı olabilir.",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            isError = false
                            isLoading = true
                            retryTrigger++
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Yeniden Dene")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Yeniden Dene")
                    }
                }
            }
        }

        // Bottom Controls Overlay (Play/Pause, Mute/Unmute, and Volume Indicators)
        if (!isLoading && !isError) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Control: Play / Pause
                    IconButton(
                        onClick = {
                            try {
                                val view = mediaPlayerInstance
                                if (view != null) {
                                    if (view.isPlaying) {
                                        view.pause()
                                        isPlaying = false
                                    } else {
                                        view.start()
                                        isPlaying = true
                                    }
                                }
                            } catch (e: Exception) {
                                isError = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Oynatmayı Duraklat" else "Oynat",
                            tint = Color.White
                        )
                    }

                    // Green Active Indicator
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Green, shape = MaterialTheme.shapes.extraSmall)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CANLI YAYIN",
                            color = Color.Green,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Right Control: Mute / Unmute
                    IconButton(
                        onClick = {
                            try {
                                val mp = mediaPlayerInstance
                                if (mp != null) {
                                    if (isMuted) {
                                        mp.setVolume(1f, 1f)
                                        isMuted = false
                                    } else {
                                        mp.setVolume(0f, 0f)
                                        isMuted = true
                                    }
                                } else {
                                    isMuted = !isMuted
                                }
                            } catch (e: Exception) {
                                isMuted = !isMuted
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                            contentDescription = if (isMuted) "Sesi Aç" else "Sesi Kapat",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

// Helper to construct dynamic layout parameters
private fun BoxRowScopeLayoutParamHeader(ctx: android.content.Context): android.view.ViewGroup.LayoutParams {
    return android.view.ViewGroup.LayoutParams(
        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
        android.view.ViewGroup.LayoutParams.MATCH_PARENT
    )
}
