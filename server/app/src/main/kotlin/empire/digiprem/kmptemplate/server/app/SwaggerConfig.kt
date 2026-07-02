package empire.digiprem.kmptemplate.server.app

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private const val API_TITLE = "Kmp_project_tamplate.Api"
private const val API_VERSION = "1.0"

@Configuration
class SwaggerConfig {

    @Bean
    fun authGroup(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("auth")
        .pathsToMatch("/api/auth/**")
        .build()

    @Bean
    fun userGroup(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("user")
        .pathsToMatch("/api/profile/**")
        .build()

    @Bean
    fun settingsGroup(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("settings")
        .pathsToMatch("/api/settings/**")
        .build()

    @Bean
    fun notificationsGroup(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("notifications")
        .pathsToMatch("/api/notifications/**")
        .build()

    @Bean
    fun customOpenAPI(): OpenAPI = OpenAPI()
        .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
        .components(
            Components().addSecuritySchemes(
                "bearerAuth",
                SecurityScheme()
                    .name("Authorization")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            )
        )
        .info(
            Info()
                .title(API_TITLE)
                .version(API_VERSION)
                .description("Documentation REST de $API_TITLE")
        )
}
