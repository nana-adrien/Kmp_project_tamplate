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

@Configuration
class SwaggerConfig {
    @Bean
    fun apiV1(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("v1") // nom du groupe
            .pathsToMatch("/api/v1/**")
            .addOpenApiCustomizer(v1Customizer()) // endpoints à inclure
            .addOpenApiCustomizer(globalResponseCustomiser())
            .build()
    }

    @Bean
    fun apiV2(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("v2")
            .pathsToMatch("/api/v2/**")
            .addOpenApiCustomizer(v2Customizer())
            .build()
    }

    @Bean
    fun v1Customizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi: OpenAPI ->
            openApi.info(
                Info()
                    .title("Immobi-market API v1")
                    .version("1.0")
                    .description("Documentation de l'API Immobi-market API  version 1")
            )
        }
    }

    @Bean
    fun v2Customizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi: OpenAPI ->
            openApi.info(
                Info()
                    .title("Mon API v2")
                    .version("2.0")
                    .description("Documentation de l'API version 2")
            )
        }
    }

    @Bean
    fun globalResponseCustomiser(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi: OpenAPI ->
            // Définition du schéma ApiResponse<T>
            val apiResponseSchema = ObjectSchema()
                .properties(mapOf( "success" to BooleanSchema()))
                .properties(mapOf( "data" to ObjectSchema()))
                .properties(mapOf( "errors" to ArraySchema().items(StringSchema())))
                //.addProperties("success", BooleanSchema())
               // .addProperties("data", ObjectSchema())
              //  .addProperties("errors", ArraySchema().items(StringSchema()))

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

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
            .components(
                Components()
                    .addSecuritySchemes(
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
                    .title("Mon API")
                    .version("1.0")
                    .description("Description de l'API")
            )
    }
}