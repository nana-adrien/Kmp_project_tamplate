package convention

import extension.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureWebTarget() {
    extensions.configure<KotlinMultiplatformExtension> {
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs { browser() }
        js { browser() }
    }
    dependencies {
        "jsMainImplementation"(libs.findLibrary("wrappers-browser").get())
    }
}
