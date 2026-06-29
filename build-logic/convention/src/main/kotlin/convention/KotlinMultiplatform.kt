package convention

import extension.kmpTargets
import extension.pathToFrameworkName
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinMultiplatform() {
    val targets = kmpTargets

    if (targets.android) configureAndroidTarget()

    extensions.configure<KotlinMultiplatformExtension> {
        if (targets.ios) {
            configureIosTarget(baseName = this@configureKotlinMultiplatform.pathToFrameworkName())
        }
        if (targets.desktop) {
            jvm()
        }
        if (targets.web) {
            @OptIn(ExperimentalWasmDsl::class)
            wasmJs { browser() }
            js { browser() }
        }
        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
}
