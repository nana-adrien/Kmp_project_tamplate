plugins {
    alias(libs.plugins.springBoot)
    id("convention.spring.service")
    alias(libs.plugins.springDependencyManagement)
}

dependencies {
    implementation(project(":server:common"))
    implementation(project(":server:user"))
    implementation(project(":server:settings"))
    implementation(project(":server:notifications"))
    implementation(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.postgresql)
    developmentOnly(libs.spring.boot.devtools)
    annotationProcessor(libs.spring.boot.processor)
}
