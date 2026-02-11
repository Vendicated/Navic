package paige.navic.widgets.turntable

import android.content.Context
import android.graphics.Bitmap
import android.view.KeyEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionSendBroadcast
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.unit.ColorProvider
import paige.navic.R
import paige.navic.widgets.nowplaying.NowPlayingWidget

class TurnTableWidget : NowPlayingWidget() {

	override val sizeMode = SizeMode.Exact

	@Composable
	override fun Content(
		context: Context,
		isPlaying: Boolean,
		title: String,
		artist: String,
		bitmap: Bitmap?
	) {
		val size = LocalSize.current
		Box(
			modifier = GlanceModifier
				.size(minOf(size.width, size.height))
				.padding(12.dp)
				.clickable(actionStartActivity(launchIntent(context))),
			contentAlignment = Alignment.Center
		) {
			Image(
				provider = bitmap?.let { ImageProvider(it) }
					?: ImageProvider(R.drawable.ic_note),
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = GlanceModifier
					.fillMaxSize()
					.background(GlanceTheme.colors.surfaceVariant)
					.cornerRadius(300.dp)
			)
			CornerButton(
				imageProvider = ImageProvider(R.drawable.ic_star),
				backgroundColor = GlanceTheme.colors.tertiary,
				foregroundColor = GlanceTheme.colors.onTertiary,
				size = 45.dp,
				radius = 100.dp,
				context = context,
				alignment = Alignment.TopEnd
			)
			CornerButton(
				imageProvider = ImageProvider(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
				backgroundColor = GlanceTheme.colors.primary,
				foregroundColor = GlanceTheme.colors.onPrimary,
				size = 55.dp,
				radius = 13.dp,
				keycode = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
				context = context,
				alignment = Alignment.BottomStart
			)
		}
	}

	@Composable
	private fun CornerButton(
		imageProvider: ImageProvider,
		backgroundColor: ColorProvider,
		foregroundColor: ColorProvider,
		size: Dp,
		radius: Dp,
		keycode: Int? = null,
		context: Context,
		alignment: Alignment
	) {
		Box(
			modifier = GlanceModifier.fillMaxSize(),
			contentAlignment = alignment
		) {
			Image(
				provider = imageProvider,
				contentDescription = null,
				colorFilter = ColorFilter.tint(foregroundColor),
				modifier = GlanceModifier
					.background(backgroundColor)
					.size(size)
					.padding(12.dp)
					.cornerRadius(radius)
					.then(
						if (keycode != null) {
							GlanceModifier.clickable(
								actionSendBroadcast(
									createMediaIntent(
										context,
										keycode
									)
								)
							)
						} else GlanceModifier
					)
			)
		}
	}
}