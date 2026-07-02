package convention

import extension.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureDesktopTarget() {
    extensions.configure<KotlinMultiplatformExtension> {
        jvm(name = "desktop")
    }
    dependencies {
        "desktopMainImplementation"(libs.findLibrary("kotlinx-coroutinesSwing").get())
    }
}
