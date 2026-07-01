plugins {
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":shared-contracts"))
    api(libs.spring.boot.starter.security)
    implementation(libs.jwt.api)
    runtimeOnly(libs.jwt.impl)
    runtimeOnly(libs.jwt.jackson)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.datatype.jsr310)
}
