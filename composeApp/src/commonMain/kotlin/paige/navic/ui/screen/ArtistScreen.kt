package paige.navic.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import navic.composeapp.generated.resources.Res
import navic.composeapp.generated.resources.option_sort_frequent
import navic.composeapp.generated.resources.title_albums
import org.jetbrains.compose.resources.stringResource
import paige.navic.LocalNavStack
import paige.navic.data.model.Screen
import paige.navic.ui.component.common.ErrorBox
import paige.navic.ui.component.common.TrackRow
import paige.navic.ui.component.layout.ArtCarousel
import paige.navic.ui.component.layout.ArtCarouselItem
import paige.navic.ui.component.layout.NestedTopBar
import paige.navic.ui.viewmodel.ArtistViewModel
import paige.navic.util.UiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ArtistScreen(
	artistId: String,
	viewModel: ArtistViewModel = viewModel(key = artistId) { ArtistViewModel(artistId) }
) {
	val backStack = LocalNavStack.current
	val artistState by viewModel.artistState.collectAsState()
	val topSongs by viewModel.topSongs.collectAsState()

	val spatialSpec = MaterialTheme.motionScheme.slowSpatialSpec<Float>()
	val effectSpec = MaterialTheme.motionScheme.slowEffectsSpec<Float>()

	Scaffold(
		topBar = {
			NestedTopBar({ Text(((artistState as? UiState.Success)?.data?.name ?: "")) })
		}
	) { innerPadding ->
		AnimatedContent(
			targetState = artistState,
			transitionSpec = {
				(fadeIn(
					animationSpec = effectSpec
				) + scaleIn(
					initialScale = 0.8f,
					animationSpec = spatialSpec
				)) togetherWith (fadeOut(
					animationSpec = effectSpec
				) + scaleOut(
					animationSpec = spatialSpec
				))
			},
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
		) {
			when (it) {
				is UiState.Error -> Box(Modifier.fillMaxSize()) {
					ErrorBox(it)
				}
				is UiState.Loading -> Box(Modifier.fillMaxSize()) {
					ContainedLoadingIndicator(Modifier.size(80.dp).align(Alignment.Center))
				}
				is UiState.Success -> {
					val artist = it.data
					Column(
						modifier = Modifier
							.fillMaxSize()
							.verticalScroll(rememberScrollState())
							.padding(bottom = 117.9.dp),
						verticalArrangement = Arrangement.spacedBy(12.dp),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						topSongs.takeIf { topSongs.isNotEmpty() }?.let { songs ->
							Text(
								stringResource(Res.string.option_sort_frequent),
								style = MaterialTheme.typography.titleMediumEmphasized,
								fontWeight = FontWeight(600),
								modifier = Modifier
									.height(32.dp)
									.padding(top = 8.dp)
									.padding(horizontal = 20.dp)
									.fillMaxWidth()
							)
							LazyHorizontalGrid(
								rows = GridCells.Fixed(3),
								modifier = Modifier.fillMaxWidth().height(250.dp)
							) {
								items(songs) { song ->
									TrackRow(
										modifier = Modifier.weight(1f),
										track = song
									)
								}
							}
						}
						artist.album?.let { albums ->
							ArtCarousel(
								Res.string.title_albums,
								albums.sortedByDescending { it.playCount }
							) { album ->
								ArtCarouselItem(album.coverArt, album.name) {
									backStack.add(Screen.Tracks(album))
								}
							}
						}
					}
				}
			}
		}
	}
}
