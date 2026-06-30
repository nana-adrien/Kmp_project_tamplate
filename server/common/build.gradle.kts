plugins {
    id("convention.spring.service")
    alias(libs.plugins.spring-dependency-management)
}

dependencies {
    implementation(project(":shared-contracts"))
    implementation(libs.spring.boot.starter.security)
    implementation(libs.jwt.api)
    runtimeOnly(libs.jwt.impl)
    runtimeOnly(libs.jwt.jackson)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.datatype.jsr310)
}
