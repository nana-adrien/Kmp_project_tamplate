package empire.digiprem.kmptemplate.core.design_system.components.image

import empire.digiprem.kmptemplate.core.domain.model.AppFile
import org.jetbrains.compose.resources.DrawableResource

sealed interface AppImageSource {
    data class UrlSource(val url: String) : AppImageSource
    data class FileSource(val file: AppFile) : AppImageSource
    data class ResourceSource(val resource: DrawableResource) : AppImageSource
    data object Unknown : AppImageSource
}
