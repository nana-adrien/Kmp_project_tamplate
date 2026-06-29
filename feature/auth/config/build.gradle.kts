plugins {
    alias(libs.plugins.conventionFeature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":feature:auth:domain"))
            api(project(":feature:auth:data"))
            api(project(":feature:auth:presentation"))
        }
    }
}
