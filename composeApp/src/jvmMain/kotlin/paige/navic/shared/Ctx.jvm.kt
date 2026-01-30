package paige.navic.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@OptIn(
	ExperimentalMaterial3WindowSizeClassApi::class,
	ExperimentalMaterial3ExpressiveApi::class
)
@Composable
actual fun rememberCtx(): Ctx {
	val darkTheme = isSystemInDarkTheme()
	val sizeClass = WindowSizeClass.calculateFromSize(DpSize(1920.dp, 1080.dp))
	return remember {
		object : Ctx {
			override fun clickSound() {
				// none for jvm
			}

			override val name = "jvm"
			override val appVersion: String = "todo"
			override val colorScheme
				get() = if (darkTheme)
					darkColorScheme()
				else expressiveLightColorScheme()
			override val sizeClass = sizeClass
		}
	}
}

@Composable
actual fun Modifier.systemGesturesExclusion(): Modifier = this
