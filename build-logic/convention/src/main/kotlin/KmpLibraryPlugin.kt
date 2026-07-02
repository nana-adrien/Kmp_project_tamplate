import convention.configureKotlin
import convention.configureKotlinMultiplatform
import extension.commonMainImplementation
import extension.kmpTargets
import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val targets = kmpTargets
            with(pluginManager) {
                if (targets.android) apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }
            if (targets.android) {
                plugins.withId("com.android.kotlin.multiplatform.library") {
                    configureKotlinMultiplatform()
                }
            } else {
                configureKotlinMultiplatform()
            }
            configureKotlin()
            // Dépendances communes à TOUS les targets KMP
            dependencies {
                commonMainImplementation(libs.findLibrary("kotlinx-coroutines-core").get())
                commonMainImplementation(libs.findLibrary("kermit").get())
                "commonTestImplementation"(libs.findLibrary("kotlin-test").get())
            }
        }
    }
}
