package paige.navic.shared

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import paige.subsonic.api.model.Track
import paige.subsonic.api.model.TrackCollection

class JvmMediaPlayerViewModel : MediaPlayerViewModel() {
	override fun play(tracks: TrackCollection, startIndex: Int) {
	}

	override fun playSingle(track: Track) {
	}

	override fun pause() {
	}

	override fun resume() {
	}

	override fun seek(normalized: Float) {
	}

	override fun next() {
	}

	override fun previous() {
	}

}

@Composable
actual fun rememberMediaPlayer(): MediaPlayerViewModel {
	return viewModel { JvmMediaPlayerViewModel() }
}
