package paige.navic.ui.viewmodels

import androidx.compose.foundation.ScrollState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.subsonic.api.model.Artist
import dev.zt64.subsonic.api.model.ArtistInfo
import dev.zt64.subsonic.api.model.Song
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import paige.navic.data.session.SessionManager
import paige.navic.utils.UiState

data class ArtistState(
	val artist: Artist,
	val topSongs: List<Song>,
	val info: ArtistInfo,
	val similarArtists: List<Artist>
)

class ArtistViewModel(
	private val artistId: String
) : ViewModel() {
	private val _artistState = MutableStateFlow<UiState<ArtistState>>(UiState.Loading)
	val artistState = _artistState.asStateFlow()

	val scrollState = ScrollState(initial = 0)

	init {
		viewModelScope.launch {
			try {
				val artist = SessionManager.api.getArtist(artistId)

				coroutineScope {
					val albumsDeferred = artist.album.map { album ->
						async { SessionManager.api.getAlbum(album.id) }
					}

					val _artistInfo = async { SessionManager.api.getArtistInfo(artist) }

					val albums = albumsDeferred.awaitAll()

					val topSongs = albums.flatMap { album ->
						album.songs
							.sortedByDescending { it.playCount }
							.filter { it.playCount > 0 }
					}

					val artistInfo = _artistInfo.await()

					val similarArtists = artistInfo.similarArtists.map {
						async { SessionManager.api.getArtist(it.id) }
					}.awaitAll()

					_artistState.value = UiState.Success(ArtistState(
						artist,
						topSongs,
						artistInfo,
						similarArtists
					))
				}
			} catch (e: Exception) {
				_artistState.value = UiState.Error(e)
			}
		}
	}
}