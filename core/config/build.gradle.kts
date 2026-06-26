plugins {
    alias(libs.plugins.conventionKmpLibrary)
}

fun isTargetEnabled(key: String) = rootProject.findProperty(key) == "true"

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:domain"))
            api(project(":core:data"))
            implementation(libs.koin.core)
            implementation(libs.ktor.client.core)
            implementation(libs.kermit)
        }
        if (isTargetEnabled("kmp.target.android")) {
            androidMain.dependencies {
                implementation(libs.koin.android)
                implementation(libs.ktor.client.okhttp)
            }
        }
        if (isTargetEnabled("kmp.target.ios")) {
            iosMain.dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        if (isTargetEnabled("kmp.target.desktop")) {
            jvmMain.dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
        if (isTargetEnabled("kmp.target.web")) {
            wasmJsMain.dependencies {
                implementation(libs.ktor.client.js)
            }
        }
    }
}
