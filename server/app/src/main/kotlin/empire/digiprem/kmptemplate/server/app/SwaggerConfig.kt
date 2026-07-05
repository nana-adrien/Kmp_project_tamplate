package empire.digiprem.kmptemplate.server.app

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.ArraySchema
import io.swagger.v3.oas.models.media.BooleanSchema
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.ObjectSchema
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private const val API_TITLE = "Kmp_project_tamplate.Api"
private const val API_VERSION = "1.0"

@Configuration
class SwaggerConfig {
    @Bean
    fun apiV1(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("v1") // nom du groupe
            .pathsToMatch("/api/v1/**")
          //  .addOpenApiCustomizer(v1Customizer()) // endpoints à inclure
            .addOpenApiCustomizer(globalResponseCustomiser())
            .build()
    }

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


    @Bean
    fun globalResponseCustomiser(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi: OpenAPI ->
            // Définition du schéma ApiResponse<T>
            val apiResponseSchema = ObjectSchema()
                .properties(mapOf( "success" to BooleanSchema()))
                .properties(mapOf( "data" to ObjectSchema()))
                .properties(mapOf( "errors" to ArraySchema().items(StringSchema())))
            // Créer une réponse 200 générique
            val response200 = ApiResponse()
                .description("Succès")
                .content(
                    Content().addMediaType(
                        "application/json",
                        MediaType().schema(apiResponseSchema)
                    )
                )

            // Pour chaque path / operation, injecter cette réponse 200
            openApi.paths.values.forEach { pathItem ->
                pathItem.readOperations().forEach { op ->
                    val rs: ApiResponses = op.getResponses()
                    // N'écrase pas si déjà défini
                    if (!rs.containsKey("200")) {
                        rs.addApiResponse("200", response200)
                    }
                }
            }
        }
    }
}
