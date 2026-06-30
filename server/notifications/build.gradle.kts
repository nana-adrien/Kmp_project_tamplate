plugins {
    id("convention.spring.service")
    alias(libs.plugins.spring-dependency-management)
    alias(libs.plugins.kotlin-jpa)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":shared-contracts"))
    implementation(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.postgresql)
}
