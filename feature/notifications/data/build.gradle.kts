plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            api(project(":core:data"))
            implementation(project(":feature:notifications:domain"))
            implementation(libs.kotlinx.serialization.json)
        }
    }
}
