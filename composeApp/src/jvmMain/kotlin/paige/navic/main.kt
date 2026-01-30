package paige.navic

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowDecoration
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kyant.capsule.ContinuousRoundedRectangle
import navic.composeapp.generated.resources.Res
import navic.composeapp.generated.resources.close_window
import navic.composeapp.generated.resources.minimize
import navic.composeapp.generated.resources.square
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
	val windowState = rememberWindowState()
	val onCloseRequest = ::exitApplication
	Window(
		onCloseRequest = onCloseRequest,
		state = windowState,
		title = "Navic",
		decoration = WindowDecoration.Undecorated(20.dp),
		transparent = true
	) {
		App(
			modifier = Modifier
				.padding(16.dp)
				.dropShadow(
					shape = ContinuousRoundedRectangle(16.dp),
					shadow = Shadow(
						radius = 12.dp,
						spread = 4.dp,
						color = Color.Black.copy(alpha = 0.5f)
					)
				)
				.graphicsLayer {
					shape = ContinuousRoundedRectangle(16.dp)
					clip = true
				},
			decorations = {
				with(this@Window) {
					Decorations(
						windowState = windowState,
						onCloseRequest = onCloseRequest
					)
				}
			}
		)
	}
}

@Composable
private fun WindowScope.Decorations(
	windowState: WindowState,
	onCloseRequest: () -> Unit
) {
	WindowDraggableArea {
		Surface {
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically
			) {
				Text("Navic", Modifier.padding(10.dp).weight(1f))
				Control(
					onClick = { windowState.isMinimized = true },
					{
						Icon(
							vectorResource(Res.drawable.minimize),
							null,
							Modifier
								.size(16.dp)
								.scale(scaleX = 1.25f, scaleY = 1f)
						)
					}
				)
				Control(
					onClick = {},
					{
						Icon(
							vectorResource(Res.drawable.square),
							null,
							Modifier.size(16.dp)
						)
					}
				)
				Control(
					onClick = { onCloseRequest() },
					{
						Icon(
							vectorResource(Res.drawable.close_window),
							null
						)
					}
				)
			}
		}
	}
}

@Composable
private fun Control(
	onClick: () -> Unit,
	content: @Composable () -> Unit
) {
	IconButton(
		onClick = onClick,
		modifier = Modifier.size(50.dp, 44.dp),
		shape = RectangleShape,
		content = content
	)
}
