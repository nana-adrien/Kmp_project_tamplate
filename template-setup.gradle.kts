// ═══════════════════════════════════════════════════════════════════
//  template-setup.gradle.kts
//  Logique de configuration d'un nouveau projet KMP depuis le template.
//
//  Déclenché par : ./gradlew applyTemplateConfig
//  Lu depuis     : template.config.properties (créé par le plugin)
//  Supprimé par  : deleteTemplateFiles() en fin d'exécution
//
//  Pour faire évoluer la configuration → éditer CE fichier dans le template.
//  Le plugin IntelliJ n'a pas besoin d'être mis à jour.
// ═══════════════════════════════════════════════════════════════════

import java.util.Properties

// ── Lecture de la configuration ──────────────────────────────────────

val cfg = Properties().apply {
    val cfgFile = file("template.config.properties")
    require(cfgFile.exists()) {
        "template.config.properties introuvable. Lance ce script depuis le plugin."
    }
    cfgFile.inputStream().use { load(it) }
}

fun prop(key: String): String = cfg.getProperty(key) ?: ""
fun bool(key: String): Boolean = prop(key).trim().lowercase() == "true"

// ── Tâche principale ──────────────────────────────────────────────────

tasks.register("applyTemplateConfig") {
    group = "template"
    description = "Configure le projet depuis template.config.properties"

    doLast {
        println("\n🔧 Démarrage de la configuration du projet…")
        println("   App     : ${prop("app.name")}")
        println("   Package : ${prop("app.package")}")
        println("   Préfixe : ${prop("app.prefix")}")
        println("   Cibles  : ${buildTargetsSummary()}")
        println("   Backend : ${prop("backend.type")}")
        println()

        step("1/10 Suppression des dossiers inutiles")     { cleanupUnusedDirs() }
        step("2/10 Nettoyage des build.gradle.kts")        { cleanModuleBuildFiles() }
        step("3/10 Réécriture de settings.gradle.kts")    { rewriteSettings() }
        step("4/10 Réécriture de build.gradle.kts racine"){ rewriteRootBuildGradle() }
        step("5/10 Réécriture de build-logic")             { rewriteBuildLogicConvention() }
        step("6/10 Réécriture de PathUtil.kt")             { rewritePathUtil() }
        step("7/10 Nettoyage de libs.versions.toml")       { cleanToml() }
        step("8/10 Renommage du package")                  { renamePackage() }
        step("9/10 Renommage du préfixe UI")               { renamePrefix() }
        step("10/10 Écriture des propriétés")              {
            writeGradleProperties()
            writeLocalProperties()
            deleteTemplateFiles()
        }

        println("\n✅ Projet configuré avec succès !")
        println("   Prochaine étape : git add -A && git commit -m \"chore: init from template\"\n")
    }
}

// ── Utilitaires généraux ──────────────────────────────────────────────

fun step(label: String, block: () -> Unit) {
    print("   $label… ")
    try { block(); println("✓") } catch (e: Exception) { println("✗"); throw e }
}

fun buildTargetsSummary(): String = buildList {
    if (bool("target.android")) add("Android")
    if (bool("target.ios"))     add("iOS")
    if (bool("target.desktop")) add("Desktop")
    if (bool("target.web"))     add("Web")
}.ifEmpty { listOf("aucune") }.joinToString(", ")

fun deleteDir(path: String)  { file(path).takeIf { it.exists() }?.deleteRecursively() }
fun deleteFile(path: String) { file(path).takeIf { it.exists() }?.delete() }

fun removeEmptyDirs() {
    var changed = true
    while (changed) {
        changed = false
        fileTree(".") { exclude(".git/**") }
            .filter { f -> f.isDirectory && f != projectDir && f.listFiles()?.isEmpty() == true }
            .forEach { it.delete(); changed = true }
    }
}

fun replaceInFiles(extensions: List<String>, oldValue: String, newValue: String) {
    projectDir.walkTopDown()
        .filter { it.isFile && it.extension in extensions }
        .filter { f -> !f.path.contains("/.git/") && !f.path.contains("/build/") }
        .filter { f -> f.name != "template-setup.gradle.kts" }
        .forEach { f ->
            val content = f.readText()
            if (content.contains(oldValue)) f.writeText(content.replace(oldValue, newValue))
        }
}

// ══════════════════════════════════════════════════════════════════════
// ÉTAPE 1 — Suppression des dossiers inutiles
// ══════════════════════════════════════════════════════════════════════

fun cleanupUnusedDirs() {
    if (!bool("target.android")) deleteDir("androidApp")
    if (!bool("target.ios"))     deleteDir("iosApp")
    if (!bool("target.desktop")) deleteDir("desktopApp")
    if (!bool("target.web"))     deleteDir("webApp")

    val activeSets = mutableSetOf("commonMain", "commonTest")
    if (bool("target.android")) activeSets += listOf("androidMain", "androidUnitTest", "androidInstrumentedTest")
    if (bool("target.ios"))     activeSets += listOf("iosMain", "iosTest")
    if (bool("target.desktop")) activeSets += listOf("jvmMain", "jvmTest", "desktopMain", "desktopTest")
    if (bool("target.web"))     activeSets += listOf("wasmJsMain", "wasmJsTest", "jsMain", "jsTest")

    fileTree(".") {
        include("**/src/*/")
        exclude(".git/**", "**/build/**", "template-setup.gradle.kts")
    }.filter { f ->
        f.isDirectory &&
        f.parentFile.name == "src" &&
        (f.name.endsWith("Main") || f.name.endsWith("Test") ||
         f.name.endsWith("UnitTest") || f.name.endsWith("InstrumentedTest")) &&
        f.name !in activeSets
    }.forEach { orphan ->
        println("      ↳ suppression source set : ${orphan.relativeTo(projectDir)}")
        orphan.deleteRecursively()
    }

    when (prop("backend.type")) {
        "ktor"        -> { deleteDir("server"); deleteDir("supabase") }
        "ktor-server" -> deleteDir("supabase")
        "supabase"    -> deleteDir("server")
    }

    if (prop("push.type") != "firebase") {
        deleteFile("androidApp/google-services.json")
        deleteFile("iosApp/GoogleService-Info.plist")
        deleteFile("iosApp/GoogleService-Info.plist.sample")
    }

    removeEmptyDirs()
}

// ══════════════════════════════════════════════════════════════════════
// ÉTAPE 2 — Nettoyage des build.gradle.kts individuels
// ══════════════════════════════════════════════════════════════════════

fun cleanModuleBuildFiles() {
    val toRemove = mutableListOf<String>()
    if (!bool("target.android")) toRemove += listOf("androidMain", "androidTest", "androidUnitTest", "androidInstrumentedTest")
    if (!bool("target.ios"))     toRemove += listOf("iosMain", "iosTest")
    if (!bool("target.desktop")) toRemove += listOf("jvmMain", "jvmTest", "desktopMain", "desktopTest")
    if (!bool("target.web"))     toRemove += listOf("jsMain", "jsTest", "wasmJsMain", "wasmJsTest")
    if (toRemove.isEmpty()) return

    fileTree(".") {
        include("**/build.gradle.kts")
        exclude(
            "build-logic/**", "build/**", ".gradle/**",
            "build.gradle.kts", "settings.gradle.kts", "template-setup.gradle.kts"
        )
    }.forEach { buildFile -> cleanSourceSetsInFile(buildFile, toRemove) }
}

fun cleanSourceSetsInFile(buildFile: File, sourceSetNames: List<String>) {
    var content = buildFile.readText()
    var changed = false

    sourceSetNames.forEach { ssName ->
        val escaped = Regex.escape(ssName)
        val patterns = listOf(
            Regex("""^[ \t]*$escaped\.dependencies\s*\{[^}]*\}\n?""",    setOf(RegexOption.MULTILINE)),
            Regex("""^[ \t]*$escaped\s*\{[^}]*\}\n?""",                   setOf(RegexOption.MULTILINE)),
            Regex("""^[ \t]*val\s+$escaped\s+by\s+\w+\s*\{[\s\S]*?\}\n?""", setOf(RegexOption.MULTILINE)),
            Regex("""^[ \t]*(?:getByName|create)\s*\(\s*"$escaped"\s*\)\s*\{[^}]*\}\n?""", setOf(RegexOption.MULTILINE))
        )
        patterns.forEach { pattern ->
            val replaced = content.replace(pattern, "")
            if (replaced != content) { content = replaced; changed = true }
        }
    }

    if (changed) {
        content = content.replace(Regex("""sourceSets\s*\{\s*commonMain\.dependencies\s*\{\s*\}\s*\}"""), "")
        content = content.replace(Regex("""sourceSets\s*\{\s*\}"""), "")
        content = content.replace(Regex("""\n{3,}"""), "\n\n")
        buildFile.writeText(content)
    }
}

// ══════════════════════════════════════════════════════════════════════
// ÉTAPE 3 — Réécriture de settings.gradle.kts
// ══════════════════════════════════════════════════════════════════════

fun rewriteSettings() {
    val content = buildString {
        appendLine("""rootProject.name = "${prop("app.name")}"""")
        appendLine("""enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")""")
        appendLine()
        appendLine("pluginManagement {")
        appendLine("""    includeBuild("build-logic")""")
        appendLine("    repositories {")
        appendLine("        google {")
        appendLine("            mavenContent {")
        appendLine("""                includeGroupAndSubgroups("androidx")""")
        appendLine("""                includeGroupAndSubgroups("com.android")""")
        appendLine("""                includeGroupAndSubgroups("com.google")""")
        appendLine("            }")
        appendLine("        }")
        appendLine("        mavenCentral()")
        appendLine("        gradlePluginPortal()")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("dependencyResolutionManagement {")
        appendLine("    repositories {")
        appendLine("        google {")
        appendLine("            mavenContent {")
        appendLine("""                includeGroupAndSubgroups("androidx")""")
        appendLine("""                includeGroupAndSubgroups("com.android")""")
        appendLine("""                includeGroupAndSubgroups("com.google")""")
        appendLine("            }")
        appendLine("        }")
        appendLine("        mavenCentral()")
        appendLine("    }")
        appendLine("}")
        appendLine()
        appendLine("plugins {")
        appendLine("""    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"""")
        appendLine("}")
        appendLine()

        appendLine("""include(":shared")""")
        appendLine("""include(":shared-contracts")""")
        if (bool("target.android")) appendLine("""include(":androidApp")""")
        if (bool("target.desktop")) appendLine("""include(":desktopApp")""")
        if (bool("target.web"))     appendLine("""include(":webApp")""")
        appendLine()

        if (prop("backend.type") == "ktor-server") {
            appendLine("include(")
            appendLine("""    ":server:common",""")
            appendLine("""    ":server:user",""")
            appendLine("""    ":server:settings",""")
            appendLine("""    ":server:notifications",""")
            appendLine("""    ":server:app",""")
            appendLine(")")
            appendLine()
        }

        appendLine("include(")
        appendLine("""    ":core:domain",""")
        appendLine("""    ":core:data",""")
        appendLine("""    ":core:presentation",""")
        appendLine("""    ":core:design_system",""")
        appendLine("""    ":core:config",""")
        appendLine(")")
        appendLine()

        appendLine("include(")
        appendLine("""    ":feature:auth:domain",""")
        appendLine("""    ":feature:auth:data",""")
        appendLine("""    ":feature:auth:presentation",""")
        appendLine("""    ":feature:auth:config",""")
        appendLine()
        appendLine("""    ":feature:profile:domain",""")
        appendLine("""    ":feature:profile:data",""")
        appendLine("""    ":feature:profile:presentation",""")
        appendLine("""    ":feature:profile:config",""")
        appendLine()
        appendLine("""    ":feature:notifications:domain",""")
        appendLine("""    ":feature:notifications:data",""")
        appendLine("""    ":feature:notifications:presentation",""")
        appendLine("""    ":feature:notifications:config",""")
        appendLine()
        appendLine("""    ":feature:settings:domain",""")
        appendLine("""    ":feature:settings:data",""")
        appendLine("""    ":feature:settings:presentation",""")
        appendLine("""    ":feature:settings:config",""")
        appendLine()
        appendLine("""    ":feature:dashboard:presentation",""")
        appendLine("""    ":feature:dashboard:config",""")
        append(")")
    }
    file("settings.gradle.kts").writeText(content)
}

// ══════════════════════════════════════════════════════════════════════
// ÉTAPE 4 — Réécriture de build.gradle.kts racine
// ══════════════════════════════════════════════════════════════════════

fun rewriteRootBuildGradle() {
    val content = buildString {
        appendLine("plugins {")
        if (bool("target.android")) {
            appendLine("    alias(libs.plugins.androidApplication)          apply false")
            appendLine("    alias(libs.plugins.androidMultiplatformLibrary) apply false")
        }
        appendLine("    alias(libs.plugins.composeMultiplatform)        apply false")
        appendLine("    alias(libs.plugins.composeCompiler)             apply false")
        appendLine("    alias(libs.plugins.kotlinJvm)                   apply false")
        appendLine("    alias(libs.plugins.kotlinMultiplatform)         apply false")
        appendLine("    alias(libs.plugins.kotlinSerialization)         apply false")
        appendLine("    alias(libs.plugins.ksp)                         apply false")
        appendLine("    alias(libs.plugins.room)                        apply false")
        if (prop("backend.type") == "ktor-server") {
            appendLine("    alias(libs.plugins.springBoot)                  apply false")
            appendLine("    alias(libs.plugins.springDependencyManagement)  apply false")
            appendLine("    alias(libs.plugins.kotlinSpring)                apply false")
            appendLine("    alias(libs.plugins.kotlinJpa)                   apply false")
        }
        if (prop("backend.type") == "supabase") {
            appendLine("    alias(libs.plugins.buildkonfig)                 apply false")
        }
        append("}")
    }
    file("build.gradle.kts").writeText(content)
}

// ══════════════════════════════════════════════════════════════════════
// ÉTAPE 5 — Réécriture de build-logic/convention/build.gradle.kts
// ══════════════════════════════════════════════════════════════════════

fun rewriteBuildLogicConvention() {
    val f = file("build-logic/convention/build.gradle.kts")
    if (!f.exists()) return

    val content = buildString {
        appendLine("import org.jetbrains.kotlin.gradle.dsl.JvmTarget")
        appendLine()
        appendLine("plugins { `kotlin-dsl` }")
        appendLine()
        appendLine("""group = "${prop("app.package")}.buildlogic"""")
        appendLine()
        appendLine("dependencies {")
        appendLine("    implementation(libs.room.gradlePlugin)")
        appendLine("    compileOnly(libs.kotlin.gradlePlugin)")
        appendLine("    compileOnly(libs.ksp.gradlePlugin)")
        appendLine("    compileOnly(libs.compose.gradlePlugin)")
        if (bool("target.android")) {
            appendLine("    compileOnly(libs.android.gradlePlugin)")
            appendLine("    compileOnly(libs.android.kotlin.multiplatform.library.gradelPlugin)")
            appendLine("    compileOnly(libs.android.tools.common)")
        }
        if (prop("backend.type") == "ktor-server") {
            appendLine("    implementation(libs.kotlin.allopen)")
            appendLine("    implementation(libs.kotlin.noarg)")
        }
        if (prop("backend.type") == "supabase") {
            appendLine("    implementation(libs.buildkonfig.gradlePlugin)")
            appendLine("    implementation(libs.buildkonfig.compiler)")
        }
        appendLine("}")
        appendLine()
        appendLine("java {")
        appendLine("    sourceCompatibility = JavaVersion.VERSION_17")
        appendLine("    targetCompatibility = JavaVersion.VERSION_17")
        appendLine("}")
        appendLine()
        appendLine("kotlin {")
        appendLine("    compilerOptions {")
        appendLine("        jvmTarget = JvmTarget.JVM_17")
        appendLine("    }")
        append("}")
    }
    f.writeText(content)
}

// ══════════════════════════════════════════════════════════════════════
// ÉTAPE 6 — Réécriture de PathUtil.kt
// ══════════════════════════════════════════════════════════════════════

fun rewritePathUtil() {
    val f = file("build-logic/convention/src/main/kotlin/extension/PathUtil.kt")
    if (!f.exists()) return

    val pkg = prop("app.package")
    val content = buildString {
        appendLine("package extension")
        appendLine()
        appendLine("import org.gradle.api.Project")
        appendLine("import java.util.Locale")
        appendLine()
        appendLine("data class KmpTargets(")
        appendLine("    val android: Boolean,")
        appendLine("    val ios: Boolean,")
        appendLine("    val desktop: Boolean,")
        appendLine("    val web: Boolean,")
        appendLine(")")
        appendLine()
        appendLine("val Project.kmpTargets: KmpTargets")
        appendLine("    get() = KmpTargets(")
        appendLine("        android = ${bool("target.android")},")
        appendLine("        ios     = ${bool("target.ios")},")
        appendLine("        desktop = ${bool("target.desktop")},")
        appendLine("        web     = ${bool("target.web")},")
        appendLine("    )")
        appendLine()
        appendLine("fun Project.relativePackageName(): String {")
        appendLine("    return path.replace(':', '.').lowercase()")
        appendLine("}")
        appendLine()
        appendLine("fun Project.pathToPackageName(): String {")
        appendLine("    val base = \"$pkg\"")
        appendLine("    return \"\$base\${relativePackageName()}\"")
        appendLine("}")
        appendLine()
        appendLine("fun Project.pathToResourcePrefix(): String {")
        appendLine("    return path.replace(':', '_').lowercase().drop(1) + \"_\"")
        appendLine("}")
        appendLine()
        appendLine("fun Project.pathToFrameworkName(): String {")
        appendLine("    val parts = path.split(\":\", \"_\", \"-\", \" \")")
        appendLine("    return parts.joinToString(\"\") { part ->")
        appendLine("        part.replaceFirstChar { it.titlecase(Locale.ROOT) }")
        appendLine("    }")
        append("}")
    }
    f.writeText(content)
}

// ══════════════════════════════════════════════════════════════════════
// ÉTAPE 7 — Nettoyage de libs.versions.toml
// ══════════════════════════════════════════════════════════════════════

fun cleanToml() {
    val tomlFile = file("gradle/libs.versions.toml")
    if (!tomlFile.exists()) return

    val aliases = mutableSetOf<String>()

    if (!bool("target.android")) aliases += setOf(
        "agp", "androidTools", "android-compileSdk", "android-minSdk", "android-targetSdk",
        "androidx-activity", "androidx-appcompat", "androidx-core", "androidx-espresso",
        "androidx-testExt", "android-gradlePlugin",
        "android-kotlin-multiplatform-library-gradelPlugin", "android-tools-common",
        "androidx-core-ktx", "androidx-testExt-junit", "androidx-espresso-core",
        "androidx-activity-compose", "ktor-client-okhttp", "koin-android",
        "datastore-preferences", "datastore", "androidApplication", "androidMultiplatformLibrary"
    )

    if (!bool("target.ios"))     aliases += setOf("ktor-client-darwin")

    if (!bool("target.desktop")) aliases += setOf(
        "kotlinx-coroutinesSwing", "ktor-client-cio", "kotlinJvm"
    )

    if (!bool("target.web"))     aliases += setOf(
        "kotlin-wrappers", "wrappers-browser", "ktor-client-js"
    )

    if (prop("backend.type") != "ktor-server") aliases += setOf(
        "spring-boot", "spring-dependency-management", "jwt", "jakarta", "jackson",
        "spring-boot-starter-webmvc", "spring-boot-starter-security",
        "spring-boot-starter-data-jpa", "spring-boot-starter-validation",
        "spring-boot-starter-mail", "spring-boot-starter-test",
        "spring-boot-devtools", "spring-boot-processor", "spring-security-test",
        "jwt-api", "jwt-impl", "jwt-jackson",
        "jakarta-servlet-api", "jackson-module-kotlin", "jackson-datatype-jsr310",
        "kotlin-reflect", "postgresql",
        "kotlin-allopen", "kotlin-noarg",
        "springBoot", "springDependencyManagement", "kotlinSpring", "kotlinJpa",
        "conventionSpringService"
    )

    if (prop("backend.type") != "supabase") aliases += setOf(
        "supabase", "supabase-auth", "supabase-realtime", "supabase-postgrest",
        "buildkonfig", "buildkonfig-gradlePlugin", "buildkonfig-compiler"
    )

    if (prop("push.type") != "firebase") aliases += setOf(
        "firebase", "firebase-bom", "firebase-messaging"
    )

    rewriteToml(tomlFile, aliases)
}

fun rewriteToml(tomlFile: File, aliasesToRemove: Set<String>) {
    val lines  = tomlFile.readLines().toMutableList()
    val result = mutableListOf<String>()
    var i      = 0

    while (i < lines.size) {
        val line    = lines[i]
        val trimmed = line.trimStart()

        val matched = aliasesToRemove.firstOrNull { alias ->
            trimmed.startsWith("$alias ") ||
            trimmed.startsWith("$alias=") ||
            trimmed.startsWith("$alias =")
        }

        if (matched != null) {
            when {
                line.contains("{") && line.contains("}") -> {
                    i++
                    if (result.lastOrNull()?.isBlank() == true) result.removeLastOrNull()
                }
                line.contains("{") && !line.contains("}") -> {
                    while (i < lines.size && !lines[i].contains("}")) i++
                    i++
                    if (result.lastOrNull()?.isBlank() == true) result.removeLastOrNull()
                }
                else -> {
                    i++
                    if (result.lastOrNull()?.isBlank() == true) result.removeLastOrNull()
                }
            }
            continue
        }

        result.add(line)
        i++
    }

    val cleaned = mutableListOf<String>()
    result.forEachIndexed { k, line ->
        if (line.isBlank() && cleaned.lastOrNull()?.isBlank() == true) return@forEachIndexed
        cleaned.add(line)
    }

    tomlFile.writeText(cleaned.joinToString("\n"))
}

// ══════════════════════════════════════════════════════════════════════
// ÉTAPE 8 — Renommage du package
// ══════════════════════════════════════════════════════════════════════

fun renamePackage() {
    val oldPkgLib = "empire.digiprem.kmptemplate"
    val oldPkgApp = "empire.digiprem.kmp_project_tamplate"
    val oldTitle  = "Kmp_project_tamplate"
    val oldCamel  = "KmpTemplate"
    val newPkg    = prop("app.package")
    val newName   = prop("app.name")

    val ext = listOf("kt", "kts", "xml", "swift", "xcconfig", "properties", "toml", "gradle", "html", "css")

    replaceInFiles(ext, oldPkgLib, newPkg)
    replaceInFiles(ext, oldPkgApp, newPkg)
    replaceInFiles(ext, oldTitle,  newName)
    replaceInFiles(ext, oldCamel,  newName)

    renamePackageDirs(oldPkgLib, newPkg)
    renamePackageDirs(oldPkgApp, newPkg)
}

fun renamePackageDirs(oldPkg: String, newPkg: String) {
    val sep        = File.separatorChar
    val oldPathSep = oldPkg.replace('.', sep)
    val newPathSep = newPkg.replace('.', sep)
    val oldDirName = oldPkg.substringAfterLast('.')

    projectDir.walkTopDown()
        .filter { it.isDirectory && it.name == oldDirName && it.absolutePath.contains(oldPathSep) }
        .filter { !it.absolutePath.contains("${sep}.git${sep}") && !it.absolutePath.contains("${sep}build${sep}") }
        .toList()
        .forEach { dir ->
            val newDir = File(dir.absolutePath.replace(oldPathSep, newPathSep))
            if (dir.absolutePath != newDir.absolutePath) {
                newDir.parentFile?.mkdirs()
                dir.renameTo(newDir)
            }
        }

    File(projectDir, "empire").let { if (it.exists()) cleanEmptyDirs(it) }
}

fun cleanEmptyDirs(dir: File) {
    if (!dir.exists() || !dir.isDirectory) return
    dir.listFiles()?.forEach { if (it.isDirectory) cleanEmptyDirs(it) }
    if (dir.listFiles()?.isEmpty() == true) dir.delete()
}

// ══════════════════════════════════════════════════════════════════════
// ÉTAPE 9 — Renommage du préfixe UI
// ══════════════════════════════════════════════════════════════════════

fun renamePrefix() {
    val oldPrefix = "App"
    val newPrefix = prop("app.prefix")
    if (oldPrefix == newPrefix) return

    val components = listOf(
        "AnimatedVisibility", "SlideInVisibility",
        "PasswordTextField", "OtpTextField", "TextField",
        "IconButton", "IconResource", "Icon",
        "ButtonType", "Button",
        "ImageSource", "Image",
        "ErrorDialog", "AlertDialog", "ConfirmDialog",
        "ErrorLevel",
        "LoadingSpinner", "StateContent", "EmptyContent",
        "PageWrapper", "PageState",
        "CardWrapper",
        "Theme", "TopBar", "Divider",
        "Settings"
    )

    val ext = listOf("kt", "kts", "xml")
    components.forEach { suffix ->
        replaceInFiles(ext, "$oldPrefix$suffix", "$newPrefix$suffix")
    }
}

// ══════════════════════════════════════════════════════════════════════
// ÉTAPE 10 — Écriture des propriétés & suppression des fichiers template
// ══════════════════════════════════════════════════════════════════════

fun writeGradleProperties() {
    val propsFile = file("gradle.properties")
    val props     = Properties()
    if (propsFile.exists()) propsFile.inputStream().use { props.load(it) }

    listOf(
        "kmp.target.android", "kmp.target.ios", "kmp.target.desktop", "kmp.target.web",
        "backend.type",
        "supabase.url", "supabase.anon.key",
        "server.supabase.db.url", "server.supabase.db.user",
        "server.supabase.db.password", "server.jwt.secret"
    ).forEach { props.remove(it) }

    props["app.name"]             = prop("app.name")
    props["app.package"]          = prop("app.package")
    props["app.component.prefix"] = prop("app.prefix")
    props["push.provider"]        = prop("push.type")

    propsFile.outputStream().use { props.store(it, "Generated by kmp-template-plugin") }
}

fun writeLocalProperties() {
    val lines = mutableListOf<String>()

    when (prop("backend.type")) {
        "ktor-server" -> {
            lines += "server.supabase.db.url=${prop("server.db.url")}"
            lines += "server.supabase.db.user=${prop("server.db.user")}"
            lines += "server.supabase.db.password=${prop("server.db.password")}"
            lines += "server.jwt.secret=${prop("server.jwt.secret")}"
        }
        "supabase" -> {
            lines += "supabase.url=${prop("supabase.url")}"
            lines += "supabase.anon.key=${prop("supabase.anon.key")}"
        }
    }

    val mapbox = prop("mapbox.token")
    if (mapbox.isNotBlank()) lines += "MAPBOX_ACCESS_TOKEN=$mapbox"

    if (lines.isNotEmpty()) {
        file("local.properties").writeText(lines.joinToString("\n"))
        val gitignore = file(".gitignore")
        val existing  = if (gitignore.exists()) gitignore.readText() else ""
        if (!existing.contains("local.properties")) {
            gitignore.appendText("\nlocal.properties\n")
        }
    }
}

fun deleteTemplateFiles() {
    deleteFile("template-setup.gradle.kts")
    deleteFile("template.config.properties")
    deleteFile("setup.sh")
    deleteFile("rename-project.sh")
    deleteFile("create-feature.sh")
    deleteFile("local.properties.example")
    deleteFile("template-manifest.toml")
}
