plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:profile:domain"))
            api(project(":feature:profile:data"))
            api(project(":feature:profile:presentation"))
        }
    }
}
