import extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class SpringCommonPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("org.jetbrains.kotlin.plugin.spring")
            }
            configure<KotlinJvmProjectExtension> {
                jvmToolchain(21)
            }
            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    freeCompilerArgs.addAll(
                        "-Xjsr305=strict",
                        "-Xannotation-default-target=param-property"
                    )
                    jvmTarget.set(JvmTarget.JVM_21)
                }
            }
            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
            }
        }
    }
}
