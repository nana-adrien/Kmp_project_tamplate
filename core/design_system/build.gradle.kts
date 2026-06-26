plugins {
    alias(libs.plugins.conventionCmpLibrary)
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "empire.digiprem.kmptemplate.core.design_system.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
            api(project(":core:presentation"))
            implementation(libs.coil.compose)
            implementation(libs.coil.network)
            implementation(libs.compose.materialIconsCore)
        }
    }
}
