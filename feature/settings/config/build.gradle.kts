plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:settings:domain"))
            api(project(":feature:settings:data"))
            api(project(":feature:settings:presentation"))
        }
    }
}
