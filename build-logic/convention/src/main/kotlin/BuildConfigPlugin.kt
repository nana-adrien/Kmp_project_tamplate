import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.Properties

class BuildConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.codingfeline.buildkonfig")

            val localProps = Properties()
            val localFile = rootProject.file("local.properties")
            if (localFile.exists()) localFile.inputStream().use { localProps.load(it) }

            val isRelease = project.hasProperty("release")

            val supabaseUrl = if (isRelease)
                localProps["SUPABASE_URL_PROD"] as? String ?: ""
            else
                localProps["SUPABASE_URL_DEV"] as? String ?: "http://127.0.0.1:54321"

            val supabaseKey = if (isRelease)
                localProps["SUPABASE_KEY_PROD"] as? String ?: ""
            else
                localProps["SUPABASE_KEY_DEV"] as? String ?: ""

            extensions.configure<BuildKonfigExtension>("buildkonfig") {
                packageName = "empire.digiprem.kmptemplate.config"
                defaultConfigs {
                    buildConfigField(STRING, "SUPABASE_URL", supabaseUrl)
                    buildConfigField(STRING, "SUPABASE_KEY", supabaseKey)
                    buildConfigField(STRING, "ENV", if (isRelease) "release" else "debug")
                }
            }
        }
    }
}
