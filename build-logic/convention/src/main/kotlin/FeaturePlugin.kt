import extension.commonMainImplementation
import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("convention.cmp.library")
            }
            dependencies {
                commonMainImplementation(libs.findLibrary("koin-core").get())
                commonMainImplementation(libs.findLibrary("koin-compose").get())
                commonMainImplementation(libs.findLibrary("koin-compose-viewmodel").get())
                commonMainImplementation(libs.findLibrary("navigation-compose").get())
            }
        }
    }
}
