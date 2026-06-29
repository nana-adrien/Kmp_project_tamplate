package convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import extension.libs
import extension.pathToPackageName
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureAndroidTarget() {
    extensions.configure<KotlinMultiplatformExtension> {
        extensions.configure<KotlinMultiplatformAndroidLibraryTarget> {
            namespace = pathToPackageName()
            compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()
            minSdk = libs.findVersion("android-minSdk").get().toString().toInt()
            androidResources.enable = true
        }
    }
}
