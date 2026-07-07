package com.shepherd.shep_blog.security.security_config;

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
                title = "Shep Blog API",
                version = "1.0",
                description = "REST API documentation for Shep Blog system",
                contact = @Contact(
                        name = "Sheps Engineering Team",
                        email = "engineering@shepblog.com",
                        url = "https://shepblog.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(
                        description = "Local",
                        url = "http://localhost:9092"
                ),
                @Server(
                        description = "Development",
                        url = "https://dev.shepblog.com"
                ),
                @Server(
                        description = "Production",
                        url = "https://api.shepblog.com"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication using Bearer token",
        scheme = "bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}