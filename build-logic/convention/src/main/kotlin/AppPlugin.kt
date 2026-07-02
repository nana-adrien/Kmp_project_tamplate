import convention.configureKotlin
import convention.configureKotlinMultiplatform
import extension.commonMainImplementation
import extension.kmpTargets
import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

// Entry point for the shared composeApp module — KMP + CMP + Koin + Navigation
class AppPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val targets = kmpTargets
            with(pluginManager) {
                if (targets.android) apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            if (targets.android) {
                plugins.withId("com.android.kotlin.multiplatform.library") {
                    configureKotlinMultiplatform()
                }
            } else {
                configureKotlinMultiplatform()
            }
            configureKotlin()
            // Dépendances communes à TOUS les targets (KMP + CMP + navigation)
            dependencies {
                commonMainImplementation(libs.findLibrary("kotlinx-coroutines-core").get())
                commonMainImplementation(libs.findLibrary("kermit").get())
                "commonTestImplementation"(libs.findLibrary("kotlin-test").get())
                commonMainImplementation(libs.findLibrary("compose-runtime").get())
                commonMainImplementation(libs.findLibrary("compose-foundation").get())
                commonMainImplementation(libs.findLibrary("compose-material3").get())
                commonMainImplementation(libs.findLibrary("compose-ui").get())
                commonMainImplementation(libs.findLibrary("compose-components-resources").get())
                commonMainImplementation(libs.findLibrary("compose-uiToolingPreview").get())
                commonMainImplementation(libs.findLibrary("androidx-lifecycle-viewmodelCompose").get())
                commonMainImplementation(libs.findLibrary("androidx-lifecycle-runtimeCompose").get())
                commonMainImplementation(libs.findLibrary("compose-materialIconsCore").get())
                commonMainImplementation(libs.findLibrary("compose-materialIconsExtended").get())
                commonMainImplementation(libs.findLibrary("koin-core").get())
                commonMainImplementation(libs.findLibrary("koin-compose").get())
                commonMainImplementation(libs.findLibrary("koin-compose-viewmodel").get())
                commonMainImplementation(libs.findLibrary("navigation-compose").get())
                // Dépendance propre au target Android
                if (targets.android) {
                    "androidMainImplementation"(libs.findLibrary("compose-uiToolingPreview").get())
                }
            }
        }
    }
}
