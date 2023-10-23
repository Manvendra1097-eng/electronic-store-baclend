package com.m2code.configuration;

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
                title = "OpenApi specification - Electronic Store",
                description = "Cantain api details for Electronic Store",
                contact = @Contact(
                        name = "Manvendra",
                        email = "manvendra1097@gamil.com",
                        url = "http://localhost/8080/manvendra"
                ),
                license = @License(
                        name = "Mit",
                        url = "http://localhost/8080/mit-license"
                ),
                termsOfService = "terms of service",
                version = "1.0.0 V"
        ),
        servers = {
                @Server(
                        description = "Local Env",
                        url = "http://localhost:9090"
                ),
                @Server(
                        description = "Prod Env",
                        url = "https://electronic-store-baclend-production.up.railway.app"
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
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
