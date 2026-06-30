plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    // JVM is always included so Spring Boot server can consume these DTOs
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            api(libs.konform)
        }
    }
}
