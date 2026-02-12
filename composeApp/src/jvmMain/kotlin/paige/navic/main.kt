package paige.navic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.application

fun main() = application {
	var window by remember { mutableStateOf<ComposeWindow?>(null) }
	System.setProperty("skiko.vsync.enabled", "false")
	MainWindow(
		window = window,
		onSetWindow = { window = it }
	)
	MainTray(
		window = window
	)
}
