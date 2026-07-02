package convention

import extension.kmpTargets
import extension.pathToFrameworkName
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureKotlinMultiplatform() {
    val targets = kmpTargets

    if (targets.android) configureAndroidTarget()
    if (targets.ios) configureIosTarget(baseName = pathToFrameworkName())
    if (targets.desktop) configureDesktopTarget()
    if (targets.web) configureWebTarget()

    extensions.configure<KotlinMultiplatformExtension> {
        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
}
