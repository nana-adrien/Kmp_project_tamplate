import extension.commonMainApi
import extension.commonMainImplementation
import extension.kmpTargets
import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val targets = kmpTargets
            with(pluginManager) {
                apply("convention.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }
            // Dépendances communes à TOUS les targets CMP
            dependencies {
                commonMainImplementation(libs.findLibrary("compose-runtime").get())
                commonMainImplementation(libs.findLibrary("compose-foundation").get())
                commonMainImplementation(libs.findLibrary("compose-material3").get())
                commonMainImplementation(libs.findLibrary("compose-ui").get())
                commonMainImplementation(libs.findLibrary("compose-components-resources").get())
                commonMainImplementation(libs.findLibrary("compose-uiToolingPreview").get())
                commonMainApi(libs.findLibrary("androidx-lifecycle-viewmodelCompose").get())
                commonMainApi(libs.findLibrary("androidx-lifecycle-runtimeCompose").get())
                commonMainImplementation(libs.findLibrary("compose-materialIconsCore").get())
                commonMainImplementation(libs.findLibrary("compose-materialIconsExtended").get())
                // Dépendance propre au target Android (preview dans Android Studio)
                if (targets.android) {
                    "androidMainImplementation"(libs.findLibrary("compose-uiToolingPreview").get())
                }
            }
        }
    }
}
