plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            api(libs.konform)
        }
    }
}
