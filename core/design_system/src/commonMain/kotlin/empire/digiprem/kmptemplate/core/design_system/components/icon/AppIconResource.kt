package empire.digiprem.kmptemplate.core.design_system.components.icon

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

sealed interface AppIconResource {
    data class VectorResource(val vector: ImageVector) : AppIconResource
    data class PainterResource(val resource: DrawableResource) : AppIconResource
}
