import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class SpringServicePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("convention.spring.common")
            val springVersion = libs.findVersion("spring-boot").get().toString()
            dependencies {
                "implementation"(platform("org.springframework.boot:spring-boot-dependencies:$springVersion"))
                "implementation"(libs.findLibrary("spring-boot-starter-webmvc").get())
                "implementation"(libs.findLibrary("kotlin-reflect").get())
                "testImplementation"(libs.findLibrary("spring-boot-starter-test").get())
            }
        }
    }
}
