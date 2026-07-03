plugins {
    alias(libs.plugins.androidApplication)          apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform)        apply false
    alias(libs.plugins.composeCompiler)             apply false
    alias(libs.plugins.kotlinJvm)                   apply false  // [template:spring-only]
    alias(libs.plugins.kotlinMultiplatform)         apply false
    alias(libs.plugins.kotlinSerialization)         apply false
    alias(libs.plugins.ksp)                         apply false
    alias(libs.plugins.room)                        apply false
}

if (file("template-setup.gradle.kts").exists()) {
    apply(from = "template-setup.gradle.kts")
}
