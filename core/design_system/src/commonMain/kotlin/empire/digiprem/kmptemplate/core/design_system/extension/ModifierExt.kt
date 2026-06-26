package empire.digiprem.kmptemplate.core.design_system.extension

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ContentScale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntSize

@Composable
fun Modifier.clearFocusOnTap(): Modifier {
    val focusManager = LocalFocusManager.current
    return pointerInput(Unit) {
        detectTapGestures(onTap = { focusManager.clearFocus() })
    }
}

fun Modifier.watermark(
    painter: Painter,
    contentScale: ContentScale = ContentScale.Crop,
): Modifier = drawWithContent {
    val painterSize = painter.intrinsicSize
    val containerSize = IntSize(size.width.toInt(), size.height.toInt())
    val scaledSize = contentScale.computeScaleFactor(painterSize, size)
    val scaledWidth = painterSize.width * scaledSize.scaleX
    val scaledHeight = painterSize.height * scaledSize.scaleY
    with(painter) {
        draw(
            size = androidx.compose.ui.geometry.Size(scaledWidth, scaledHeight)
        )
    }
    drawContent()
}
