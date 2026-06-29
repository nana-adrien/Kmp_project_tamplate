rootProject.name = "Kmp_project_tamplate"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// ── build-logic composite build ───────────────────────────────────────────────
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// ── Read target flags ─────────────────────────────────────────────────────────
val props = java.util.Properties().apply {
    load(file("gradle.properties").inputStream())
}
fun prop(key: String) = props.getProperty(key) == "true"

// ── Platform app modules ──────────────────────────────────────────────────────
include(":shared")              // main KMP module (always included)
include(":androidApp")          // thin Android wrapper
include(":desktopApp")          // thin Desktop wrapper (conditional at build time)
include(":webApp")              // thin Web/WASM wrapper (conditional at build time)
// iosApp is always managed by Xcode — not included in Gradle

// ── Core modules ──────────────────────────────────────────────────────────────
include(
    ":core:domain",
    ":core:data",
    ":core:presentation",
    ":core:design_system",
    ":core:config",
)

// ── Feature modules ───────────────────────────────────────────────────────────
include(
    // Auth
    ":feature:auth:domain",
    ":feature:auth:data",
    ":feature:auth:presentation",
    ":feature:auth:config",

    // Profile
    ":feature:profile:domain",
    ":feature:profile:data",
    ":feature:profile:presentation",
    ":feature:profile:config",

    // Notifications
    ":feature:notifications:domain",
    ":feature:notifications:data",
    ":feature:notifications:presentation",
    ":feature:notifications:config",

    // Settings
    ":feature:settings:domain",
    ":feature:settings:data",
    ":feature:settings:presentation",
    ":feature:settings:config",

    // Dashboard
    ":feature:dashboard:presentation",
    ":feature:dashboard:config",
)
