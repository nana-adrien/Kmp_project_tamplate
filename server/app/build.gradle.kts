plugins {
    alias(libs.plugins.spring-boot)
    id("convention.spring.service")
    alias(libs.plugins.spring-dependency-management)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:user"))
    implementation(project(":server:settings"))
    implementation(project(":server:notifications"))
    runtimeOnly(libs.postgresql)
    developmentOnly(libs.spring.boot.devtools)
    annotationProcessor(libs.spring.boot.processor)
}
