package extension

import org.gradle.api.Project
import java.util.Locale

data class KmpTargets(
    val android: Boolean,
    val ios: Boolean,
    val desktop: Boolean,
    val web: Boolean,
)

val Project.kmpTargets: KmpTargets
    get() = KmpTargets(
        android = rootProject.findProperty("kmp.target.android") == "true",
        ios     = rootProject.findProperty("kmp.target.ios") == "true",
        desktop = rootProject.findProperty("kmp.target.desktop") == "true",
        web     = rootProject.findProperty("kmp.target.web") == "true",
    )

fun Project.relativePackageName(): String {
    return path.replace(':', '.').lowercase()
}

fun Project.pathToPackageName(): String {
    val base = rootProject.findProperty("app.package") as? String
        ?: "empire.digiprem.kmptemplate"
    return "$base${relativePackageName()}"
}

fun Project.pathToResourcePrefix(): String {
    return path.replace(':', '_').lowercase().drop(1) + "_"
}

fun Project.pathToFrameworkName(): String {
    val parts = path.split(":", "_", "-", " ")
    return parts.joinToString("") { part ->
        part.replaceFirstChar { it.titlecase(Locale.ROOT) }
    }
}
