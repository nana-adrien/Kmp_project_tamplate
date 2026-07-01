import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "empire.digiprem.kmptemplate.buildlogic"

dependencies {
    implementation(libs.room.gradlePlugin)
    implementation(libs.buildkonfig.gradlePlugin)
    implementation(libs.buildkonfig.compiler)
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.kotlin.multiplatform.library.gradelPlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.allopen)
    implementation(libs.kotlin.noarg)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "convention.kmp.library"
            implementationClass = "KmpLibraryPlugin"
        }
        register("cmpLibrary") {
            id = "convention.cmp.library"
            implementationClass = "CmpLibraryPlugin"
        }
        register("feature") {
            id = "convention.feature"
            implementationClass = "FeaturePlugin"
        }
        register("room") {
            id = "convention.room"
            implementationClass = "RoomPlugin"
        }
        register("app") {
            id = "convention.app"
            implementationClass = "AppPlugin"
        }
        register("springCommon") {
            id = "convention.spring.common"
            implementationClass = "SpringCommonPlugin"
        }
        register("springService") {
            id = "convention.spring.service"
            implementationClass = "SpringServicePlugin"
        }
        register("buildConfig") {
            id = "convention.build.config"
            implementationClass = "BuildConfigPlugin"
        }
    }
}
