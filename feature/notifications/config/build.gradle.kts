plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:notifications:domain"))
            api(project(":feature:notifications:data"))
            api(project(":feature:notifications:presentation"))
        }
    }
}
