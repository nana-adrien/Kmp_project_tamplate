package extension

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.ksp(dependency: Provider<MinimalExternalModuleDependency>) {
    kspAndroid(dependency)
    kspIosSimulatorArm64(dependency)
    kspIosArm64(dependency)
}

fun DependencyHandlerScope.kspAndroid(dependency: Provider<MinimalExternalModuleDependency>) {
    "kspAndroid"(dependency)
}

fun DependencyHandlerScope.kspIosSimulatorArm64(dependency: Provider<MinimalExternalModuleDependency>) {
    "kspIosSimulatorArm64"(dependency)
}

fun DependencyHandlerScope.kspIosArm64(dependency: Provider<MinimalExternalModuleDependency>) {
    "kspIosArm64"(dependency)
}

fun DependencyHandlerScope.commonMainImplementation(project: Project) {
    "commonMainImplementation"(project)
}

fun DependencyHandlerScope.commonMainImplementation(dependency: Provider<MinimalExternalModuleDependency>) {
    "commonMainImplementation"(dependency)
}

fun DependencyHandlerScope.commonMainApi(dependency: Provider<MinimalExternalModuleDependency>) {
    "commonMainApi"(dependency)
}

fun DependencyHandlerScope.commonMainImplementation(library: String) {
    "commonMainImplementation"(library)
}

fun DependencyHandlerScope.androidMainImplementation(project: Project) {
    "androidMainImplementation"(project)
}

fun DependencyHandlerScope.androidMainImplementation(dependency: Provider<MinimalExternalModuleDependency>) {
    "androidMainImplementation"(dependency)
}

fun DependencyHandlerScope.androidMainImplementation(library: String) {
    "androidMainImplementation"(library)
}
