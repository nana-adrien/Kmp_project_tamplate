plugins {
    id("convention.spring.service")
    alias(libs.plugins.spring-dependency-management)
    alias(libs.plugins.kotlin-jpa)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":shared-contracts"))
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.mail)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.datatype.jsr310)
    runtimeOnly(libs.postgresql)
}
