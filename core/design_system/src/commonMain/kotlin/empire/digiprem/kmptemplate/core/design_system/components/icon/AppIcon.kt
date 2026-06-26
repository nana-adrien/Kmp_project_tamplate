package empire.digiprem.kmptemplate.core.design_system.components.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppIcon(
    icon: AppIconResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = 24.dp,
) {
    when (icon) {
        is AppIconResource.VectorResource  -> Icon(
            imageVector        = icon.vector,
            contentDescription = contentDescription,
            modifier           = modifier.size(size),
            tint               = tint,
        )
        is AppIconResource.PainterResource -> Icon(
            painter            = painterResource(icon.resource),
            contentDescription = contentDescription,
            modifier           = modifier.size(size),
            tint               = tint,
        )
    }
}
