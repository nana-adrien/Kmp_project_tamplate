package empire.digiprem.kmptemplate.core.design_system.components.buttons

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import empire.digiprem.kmptemplate.core.design_system.components.icon.AppIcon
import empire.digiprem.kmptemplate.core.design_system.components.icon.AppIconResource

@Composable
fun AppIconButton(
    icon: AppIconResource,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    iconSize: Dp = 24.dp,
    enabled: Boolean = true,
) {
    IconButton(
        onClick  = onClick,
        modifier = modifier,
        enabled  = enabled,
    ) {
        AppIcon(
            icon               = icon,
            contentDescription = contentDescription,
            tint               = tint,
            size               = iconSize,
        )
    }
}
