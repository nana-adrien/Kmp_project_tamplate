package empire.digiprem.kmptemplate.core.design_system.components.image

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import empire.digiprem.kmptemplate.core.design_system.components.icon.AppIcon
import empire.digiprem.kmptemplate.core.design_system.components.icon.AppIconResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppImage(
    source: AppImageSource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    errorIcon: ImageVector = Icons.Default.ImageNotSupported,
) {
    when (source) {
        is AppImageSource.UrlSource -> SubcomposeAsyncImage(
            model              = source.url,
            contentDescription = contentDescription,
            modifier           = modifier,
            contentScale       = contentScale,
        ) {
            val state by painter.state.collectAsState()
            when (state) {
                is AsyncImagePainter.State.Loading -> ShimmerPlaceholder(modifier = Modifier.fillMaxSize())
                is AsyncImagePainter.State.Error   -> Box(
                    modifier        = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    AppIcon(
                        icon               = AppIconResource.VectorResource(errorIcon),
                        contentDescription = null,
                    )
                }
                else -> SubcomposeAsyncImageContent()
            }
        }

        is AppImageSource.FileSource -> AsyncImage(
            model              = source.file.bytes,
            contentDescription = contentDescription,
            modifier           = modifier,
            contentScale       = contentScale,
        )

        is AppImageSource.ResourceSource -> androidx.compose.foundation.Image(
            painter            = painterResource(source.resource),
            contentDescription = contentDescription,
            modifier           = modifier,
            contentScale       = contentScale,
        )

        AppImageSource.Unknown -> Box(
            modifier        = modifier,
            contentAlignment = Alignment.Center,
        ) {
            AppIcon(
                icon               = AppIconResource.VectorResource(errorIcon),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun ShimmerPlaceholder(modifier: Modifier = Modifier) {
    val shimmerColors = listOf(
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue  = 1200f,
        animationSpec = infiniteRepeatable(
            animation  = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerTranslate",
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start  = Offset.Zero,
        end    = Offset(translateAnim, translateAnim),
    )
    Box(modifier = modifier.background(brush))
}
