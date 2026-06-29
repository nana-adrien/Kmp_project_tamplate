plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:domain"))
            api(project(":core:presentation"))
            implementation(project(":core:design_system"))
            // Dashboard needs access to all inner feature screens
            implementation(project(":feature:profile:presentation"))
            implementation(project(":feature:notifications:presentation"))
            implementation(project(":feature:settings:presentation"))
        }
    }
}
