package app.chintan.prasadam.feature.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import app.chintan.prasadam.R
import kotlinx.coroutines.delay

private const val SPLASH_DURATION_MS = 1_500L
private const val FADE_OUT_MS = 400

/**
 * Full-screen branded splash that shows [R.drawable.splash] for ~1.5 s,
 * then fades out and calls [onFinished] so the caller can switch to the
 * main content.  Placed outside the app scaffold so it truly fills the
 * window (edge-to-edge, no inset padding).
 */
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var fadingOut by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (fadingOut) 0f else 1f,
        animationSpec = tween(durationMillis = FADE_OUT_MS),
        label = "splashAlpha",
        finishedListener = { if (fadingOut) onFinished() }
    )

    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MS)
        fadingOut = true          // triggers fade-out; onFinished fires when animation ends
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = null,           // decorative — screen reader skips it
            modifier = Modifier
                .fillMaxSize()
                .alpha(alpha),
            contentScale = ContentScale.Crop
        )
    }
}
