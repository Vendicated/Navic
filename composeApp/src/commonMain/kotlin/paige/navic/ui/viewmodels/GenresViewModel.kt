package paige.navic.ui.viewmodels

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zt64.subsonic.api.model.Album
import dev.zt64.subsonic.api.model.AlbumListType
import dev.zt64.subsonic.api.model.Genre
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import paige.navic.data.session.SessionManager
import paige.navic.utils.UiState
import kotlin.random.Random

data class GenreWithAlbums(
	val genre: Genre,
	val albums: List<Album>
)

class GenresViewModel : ViewModel() {
	private val _isRefreshing = MutableStateFlow(false)
	val isRefreshing = _isRefreshing.asStateFlow()

	private val _genresState = MutableStateFlow<UiState<List<GenreWithAlbums>>>(UiState.Loading)
	val genresState = _genresState.asStateFlow()

	val gridState = LazyGridState()

	init {
		viewModelScope.launch {
			SessionManager.isLoggedIn.collect {
				refreshGenres()
			}
		}
	}

	fun refreshGenres() {
		viewModelScope.launch {
			val currentState = _genresState.value
			val hasExistingData = currentState is UiState.Success && currentState.data.isNotEmpty()

			if (hasExistingData) {
				_isRefreshing.value = true
			} else {
				_genresState.value = UiState.Loading
			}

			try {
				val genres = coroutineScope {
					SessionManager.api.getGenres().map { genre ->
						async {
							GenreWithAlbums(
								genre = genre,
								albums = SessionManager.api.getAlbums(
									AlbumListType.ByGenre(genre.name),
									size = 5
								).shuffled(Random(genre.name.hashCode()))
							)
						}
					}.awaitAll()
				}
				_genresState.value = UiState.Success(genres)
			} catch (e: Exception) {
				if (!hasExistingData) {
					_genresState.value = UiState.Error(e)
				}
			} finally {
				_isRefreshing.value = false
			}
		}
	}
}