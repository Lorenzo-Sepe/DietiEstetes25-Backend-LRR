package it.unina.dietiestates25.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "UNINA"
                ),
                description = "OpenApi documentation for DietiEstates25.",
                title = "OpenApi specification - UNINA",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://my-url.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "DEV ENV",
                        url = "http://localhost:${server.port}${server.servlet.context-path}"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "http://93.51.15.74:${server.port}${server.servlet.context-path}"

                )
        },
        security = {
        @SecurityRequirement(
                name = "bearerAuth"
        )
}
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT for Dieti Estates 25",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
