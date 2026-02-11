package paige.navic.widgets.miniplayer

import android.content.Context
import android.graphics.Bitmap
import android.view.KeyEvent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import paige.navic.R
import paige.navic.utils.appWidgetInnerCornerRadius
import paige.navic.widgets.nowplaying.NowPlayingWidget

class MiniPlayerWidget : NowPlayingWidget() {

	override val sizeMode = SizeMode.Exact
	override val stateDefinition = PreferencesGlanceStateDefinition

	@Composable
	override fun Content(
		context: Context,
		isPlaying: Boolean,
		title: String,
		artist: String,
		bitmap: Bitmap?
	) {
		val size = LocalSize.current
		val rowPadding = 12.dp
		val imageSize = size.height - (rowPadding * 2)

		Row(
			modifier = GlanceModifier
				.background(GlanceTheme.colors.surface)
				.fillMaxSize()
				.padding(rowPadding)
				.clickable(actionStartActivity(launchIntent(context))),
			verticalAlignment = Alignment.CenterVertically
		) {
			Image(
				provider = bitmap?.let { ImageProvider(it) } ?: ImageProvider(R.drawable.ic_note),
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = GlanceModifier
					.size(imageSize)
					.background(GlanceTheme.colors.surfaceVariant)
					.appWidgetInnerCornerRadius(rowPadding)
			)

			Column(modifier = GlanceModifier.defaultWeight().padding(horizontal = rowPadding)) {
				Text(
					text = title,
					style = TextStyle(color = GlanceTheme.colors.onSurface, fontSize = 16.sp),
					maxLines = 1
				)
				Text(
					text = artist,
					style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant, fontSize = 14.sp),
					maxLines = 1
				)
			}

			MediaControl(context, R.drawable.ic_previous, KeyEvent.KEYCODE_MEDIA_PREVIOUS)
			MediaControl(
				context,
				if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
				KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
			)
			MediaControl(context, R.drawable.ic_next, KeyEvent.KEYCODE_MEDIA_NEXT)
		}
	}

	@Composable
	private fun MediaControl(context: Context, resId: Int, keyCode: Int) {
		Image(
			provider = ImageProvider(resId),
			contentDescription = null,
			modifier = GlanceModifier
				.size(40.dp)
				.padding(4.dp)
				.cornerRadius(100.dp)
				.clickable(actionSendBroadcast(createMediaIntent(context, keyCode)))
		)
	}
}