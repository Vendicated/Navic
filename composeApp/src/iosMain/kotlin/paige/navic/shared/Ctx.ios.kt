package paige.navic.shared

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIDevice

@OptIn(
	ExperimentalMaterial3WindowSizeClassApi::class,
	ExperimentalMaterial3ExpressiveApi::class
)
@Composable
actual fun rememberCtx(): Ctx {
	val darkTheme = isSystemInDarkTheme()
	val sizeClass = calculateWindowSizeClass()
	return remember {
		object : Ctx {
			override fun clickSound() {
				// none for iOS
			}

			override val name = (UIDevice.currentDevice.systemName()
				+ " " + UIDevice.currentDevice.systemVersion)
			override val colorScheme
				get() = if (darkTheme)
					darkColorScheme()
				else expressiveLightColorScheme()
			override val sizeClass = sizeClass
		}
	}
}
