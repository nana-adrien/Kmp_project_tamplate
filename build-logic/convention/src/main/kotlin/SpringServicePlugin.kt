import convention.configureSpringTarget
import org.gradle.api.Plugin
import org.gradle.api.Project

class SpringServicePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("convention.spring.common")
            configureSpringTarget()
        }
    }
}
