package paige.navic

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.NavDisplay.popTransitionSpec
import androidx.navigation3.ui.NavDisplay.predictivePopTransitionSpec
import androidx.navigation3.ui.NavDisplay.transitionSpec
import androidx.savedstate.serialization.SavedStateConfiguration
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import paige.navic.shared.Ctx
import paige.navic.shared.MediaPlayerViewModel
import paige.navic.shared.rememberCtx
import paige.navic.shared.rememberMediaPlayer
import paige.navic.ui.component.layout.BottomBar
import paige.navic.ui.component.layout.MainScaffold
import paige.navic.ui.component.layout.TopBar
import paige.navic.ui.screen.AlbumsScreen
import paige.navic.ui.screen.ArtistsScreen
import paige.navic.ui.screen.LibraryScreen
import paige.navic.ui.screen.PlaylistsScreen
import paige.navic.ui.screen.SearchScreen
import paige.navic.ui.screen.SettingsAboutScreen
import paige.navic.ui.screen.SettingsAcknowledgementsScreen
import paige.navic.ui.screen.SettingsAppearanceScreen
import paige.navic.ui.screen.SettingsBehaviourScreen
import paige.navic.ui.screen.SettingsScreen
import paige.navic.ui.screen.TracksScreen
import paige.navic.ui.theme.NavicTheme
import paige.navic.ui.viewmodel.AlbumsViewModel
import paige.subsonic.api.model.ListType
import paige.subsonic.api.model.TrackCollection

@Serializable
data object Library : NavKey
@Serializable
data object Albums : NavKey
@Serializable
data object Playlists : NavKey
@Serializable
data object Artists : NavKey
@Serializable
data object Settings : NavKey
@Serializable
data object SettingsAppearance : NavKey
@Serializable
data object SettingsBehaviour : NavKey
@Serializable
data object SettingsAbout : NavKey
@Serializable
data object SettingsAcknowledgements : NavKey
@Serializable
data object Search : NavKey
@Serializable
data class Tracks(val partialCollection: TrackCollection) : NavKey
@Serializable
data class SortedAlbums(val listType: ListType) : NavKey

private val config = SavedStateConfiguration {
	serializersModule = SerializersModule {
		polymorphic(NavKey::class) {
			subclass(Library::class, Library.serializer())
			subclass(Albums::class, Albums.serializer())
			subclass(Playlists::class, Playlists.serializer())
			subclass(Artists::class, Artists.serializer())
			subclass(Settings::class, Settings.serializer())
			subclass(SettingsAppearance::class, SettingsAppearance.serializer())
			subclass(SettingsBehaviour::class, SettingsBehaviour.serializer())
			subclass(SettingsAbout::class, SettingsAbout.serializer())
			subclass(Search::class, Search.serializer())
			subclass(Tracks::class, Tracks.serializer())
			subclass(SortedAlbums::class, SortedAlbums.serializer())
			subclass(SettingsAcknowledgements::class, SettingsAcknowledgements.serializer())
		}
	}
}

val LocalCtx = staticCompositionLocalOf<Ctx> {
	error("no ctx")
}

val LocalMediaPlayer = staticCompositionLocalOf<MediaPlayerViewModel> {
	error("no media player")
}

val LocalNavStack = staticCompositionLocalOf<NavBackStack<NavKey>> {
	error("no backstack")
}

val LocalImageBuilder = staticCompositionLocalOf<ImageRequest.Builder> {
	error("no image builder")
}

val LocalSnackbarState = staticCompositionLocalOf<SnackbarHostState> {
	error("no snackbar state")
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun App() {
	val ctx = rememberCtx()
	val platformContext = LocalPlatformContext.current
	val mediaPlayer = rememberMediaPlayer()
	val backStack = rememberNavBackStack(config, Library)
	val sceneStrategy = rememberListDetailSceneStrategy<Any>()
	val imageBuilder = ImageRequest.Builder(platformContext)
		.crossfade(true)
	val snackbarState = remember { SnackbarHostState() }

	CompositionLocalProvider(
		LocalCtx provides ctx,
		LocalMediaPlayer provides mediaPlayer,
		LocalNavStack provides backStack,
		LocalImageBuilder provides imageBuilder,
		LocalSnackbarState provides snackbarState
	) {
		NavicTheme {
			Row {
				MainScaffold(
					snackbarState = snackbarState,
					topBar = { TopBar() },
					bottomBar = { BottomBar() }
				) {
					Box(modifier = Modifier.fillMaxSize()) {
						val metadata = transitionSpec {
							ContentTransform(fadeIn(), fadeOut())
						} + popTransitionSpec {
							ContentTransform(fadeIn(), fadeOut())
						} + predictivePopTransitionSpec {
							ContentTransform(fadeIn(), fadeOut())
						}
						NavDisplay(
							backStack = backStack,
							sceneStrategy = sceneStrategy,
							onBack = { backStack.removeLastOrNull() },
							entryProvider = entryProvider {
								entry<Library>(metadata = metadata + ListDetailSceneStrategy.listPane("root")) {
									LibraryScreen()
								}
								entry<Albums>(metadata = metadata + ListDetailSceneStrategy.listPane("root")) {
									AlbumsScreen()
								}
								entry<Playlists>(metadata = metadata + ListDetailSceneStrategy.listPane("root")) {
									PlaylistsScreen()
								}
								entry<Artists>(metadata = metadata + ListDetailSceneStrategy.listPane("root")) {
									ArtistsScreen()
								}
								entry<Settings>(metadata = ListDetailSceneStrategy.listPane("settings")) {
									SettingsScreen()
								}
								entry<SettingsAppearance>(metadata = ListDetailSceneStrategy.detailPane("settings")) {
									SettingsAppearanceScreen()
								}
								entry<SettingsBehaviour>(metadata = ListDetailSceneStrategy.detailPane("settings")) {
									SettingsBehaviourScreen()
								}
								entry<SettingsAbout>(metadata = ListDetailSceneStrategy.detailPane("settings")) {
									SettingsAboutScreen()
								}
								entry<SettingsAcknowledgements>(metadata = ListDetailSceneStrategy.detailPane("settings")) {
									SettingsAcknowledgementsScreen()
								}
								entry<Tracks>(metadata = ListDetailSceneStrategy.detailPane("root")) { key ->
									TracksScreen(key.partialCollection)
								}
								entry<Search> {
									SearchScreen()
								}
								entry<SortedAlbums> { key ->
									AlbumsScreen(viewModel = viewModel(key = key.listType.value) {
										AlbumsViewModel(key.listType)
									})
								}
							},
							transitionSpec = {
								slideInHorizontally(initialOffsetX = { it }) togetherWith
									slideOutHorizontally(targetOffsetX = { -it })
							},
							popTransitionSpec = {
								slideInHorizontally(initialOffsetX = { -it }) togetherWith
									slideOutHorizontally(targetOffsetX = { it })
							},
							predictivePopTransitionSpec = {
								slideInHorizontally(initialOffsetX = { -it }) togetherWith
									slideOutHorizontally(targetOffsetX = { it })
							}
						)
					}
				}
			}
		}
	}
}