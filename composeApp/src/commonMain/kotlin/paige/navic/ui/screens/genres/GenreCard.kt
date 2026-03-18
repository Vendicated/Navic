package paige.navic.ui.screens.genres

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.materialkolor.rememberDynamicColorScheme
import dev.zt64.compose.pipette.HsvColor
import dev.zt64.subsonic.api.model.AlbumListType
import navic.composeapp.generated.resources.Res
import navic.composeapp.generated.resources.count_albums
import org.jetbrains.compose.resources.pluralStringResource
import paige.navic.LocalCtx
import paige.navic.LocalNavStack
import paige.navic.data.models.Screen
import paige.navic.data.models.Settings
import paige.navic.data.models.Settings.ThemeMode
import paige.navic.data.session.SessionManager
import paige.navic.data.session.SessionManager.getCoverArtUrl
import paige.navic.ui.theme.defaultFont
import paige.navic.ui.viewmodels.GenreWithAlbums
import kotlin.math.abs

@Composable
fun GenreCard(
	modifier: Modifier = Modifier,
	genre: GenreWithAlbums
) {
	val ctx = LocalCtx.current
	val backStack = LocalNavStack.current
	val platformContext = LocalPlatformContext.current
	val inDarkTheme = isSystemInDarkTheme()
	val isDark = remember(Settings.shared.themeMode) {
		when (Settings.shared.themeMode) {
			ThemeMode.System -> inDarkTheme
			ThemeMode.Dark -> true
			ThemeMode.Light -> false
		}
	}
	val seedColor = remember(genre.genre.name) {
		HsvColor(
			hue = abs(genre.genre.name.hashCode() % 360).toFloat(),
			saturation = 0.6f,
			value = 0.5f
		).toColor()
	}
	val colorScheme = rememberDynamicColorScheme(
		seedColor = seedColor,
		isDark = isDark
	)
	val firstAlbumCoverArt = genre.albums.firstOrNull()?.coverArtId
	val secondAlbumCoverArt = genre.albums.getOrNull(1)?.coverArtId
	val firstModel = remember(firstAlbumCoverArt) {
		ImageRequest.Builder(platformContext)
			.data(SessionManager.api.getCoverArtUrl(firstAlbumCoverArt))
			.memoryCacheKey(firstAlbumCoverArt)
			.diskCacheKey(firstAlbumCoverArt)
			.diskCachePolicy(CachePolicy.ENABLED)
			.memoryCachePolicy(CachePolicy.ENABLED)
			.build()
	}
	val secondModel = remember(secondAlbumCoverArt) {
		ImageRequest.Builder(platformContext)
			.data(SessionManager.api.getCoverArtUrl(secondAlbumCoverArt))
			.memoryCacheKey(secondAlbumCoverArt)
			.diskCacheKey(secondAlbumCoverArt)
			.diskCachePolicy(CachePolicy.ENABLED)
			.memoryCachePolicy(CachePolicy.ENABLED)
			.build()
	}
	Surface(
		modifier = modifier,
		color = colorScheme.primary,
		contentColor = colorScheme.onPrimary,
		shape = MaterialTheme.shapes.medium,
		shadowElevation = 2.dp,
		onClick = {
			ctx.clickSound()
			backStack.add(Screen.Albums(
				nested = true,
				listType = AlbumListType.ByGenre(genre.genre.name)
			))
		}
	) {
		Box {
			Box(Modifier.align(Alignment.CenterEnd)) {
				if (firstAlbumCoverArt != null) {
					AsyncImage(
						model = firstModel,
						contentDescription = null,
						modifier = Modifier
							.padding(top = 10.dp)
							.rotate(10f)
							.size(90.dp)
							.offset(x = 5.dp, y = 5.dp)
							.shadow(3.dp, MaterialTheme.shapes.medium)
							.clip(MaterialTheme.shapes.medium),
						contentScale = ContentScale.Crop
					)
					if (secondAlbumCoverArt != null) {
						AsyncImage(
							model = secondModel,
							contentDescription = null,
							modifier = Modifier
								.padding(top = 13.dp)
								.rotate(25f)
								.size(90.dp)
								.offset(x = 25.dp, y = 15.dp)
								.shadow(10.dp, MaterialTheme.shapes.medium)
								.clip(MaterialTheme.shapes.medium),
							contentScale = ContentScale.Crop
						)
					}
				}
			}
			Column(
				modifier = Modifier.fillMaxWidth().padding(
					start = 8.dp,
					end = 85.dp,
					top = 10.dp,
					bottom = 10.dp
				).align(Alignment.TopStart)
			) {
				Text(
					genre.genre.name,
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight(600),
					fontFamily = defaultFont(round = 100f),
					overflow = TextOverflow.Ellipsis,
					maxLines = 2
				)
				Text(
					pluralStringResource(
						Res.plurals.count_albums,
						genre.genre.albumCount,
						genre.genre.albumCount
					),
					style = MaterialTheme.typography.titleSmall,
				)
			}
		}
	}
}