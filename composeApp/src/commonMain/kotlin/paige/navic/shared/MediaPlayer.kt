package paige.navic.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import paige.subsonic.api.model.TrackCollection

interface MediaPlayer {
	var tracks: TrackCollection?
	val progress: State<Float>
	val currentIndex: State<Int>
	val isPaused: State<Boolean>

	fun play(tracks: TrackCollection, songIndex: Int)
	fun pause()
	fun resume()
	fun seek(normalized: Float)

	fun next()
	fun previous()
}

@Composable
expect fun rememberMediaPlayer(): MediaPlayer
