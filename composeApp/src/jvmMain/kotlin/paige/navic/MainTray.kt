package paige.navic

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ApplicationScope
import com.kdroid.composetray.tray.api.Tray
import navic.composeapp.generated.resources.Res
import navic.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource
import paige.navic.icons.Icons
import paige.navic.icons.desktop.Navic
import paige.navic.icons.filled.Play
import paige.navic.icons.filled.SkipNext
import paige.navic.icons.filled.SkipPrevious
import paige.navic.icons.outlined.Exit

@Composable
fun ApplicationScope.MainTray(window: ComposeWindow?) {
	Tray(
		icon = Icons.Desktop.Navic,
		tooltip = stringResource(Res.string.app_name),
		tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
	) {
		Item("Previous", Icons.Filled.SkipPrevious, isEnabled = false)
		Item("Play", Icons.Filled.Play, isEnabled = false)
		Item("Next", Icons.Filled.SkipNext, isEnabled = false)

		Divider()

		Item(label = "Open") {
			window?.isVisible = true
			window?.toFront()
		}
		Item(
			label = "Exit",
			icon = Icons.Outlined.Exit
		) {
			(::exitApplication)()
		}
	}
}
