//package com.shepherd.shep_blog.security.securityConfig;
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.info.License;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.annotations.servers.Server;
//
//@OpenAPIDefinition(
//        info = @Info(
//                contact = @Contact(
//                        name = "David Oso",
//                        email = "osodavid001@gmail.com",
//                        url = "https://some-url.com"
//                ),
//                description = "OpenApi documentation for Sheps Library",
//                title = "OpenApi Specification - Sheps Library",
//                version = "1.0",
//                license = @License(
//                        name = "License name",
//                        url = "https://some-url.com"
//                ),
//                termsOfService = "Terms of service"
//        ),
//        servers = {
//                @Server(
//                        description = "Local environment",
//                        url = "http://localhost:9092"
//                ),
//                @Server(
//                        description = "Development environment",
//                        url = "https://shepslibrary-production.up.railway.app"
//                )
//        },
//        security = {
//                @SecurityRequirement(
//                        name = "bearerAuth"
//                )
//        }
//)
//@SecurityScheme(
//        name = "bearerAuth",
//        description = "JWT auth description",
//        scheme = "bearer",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        in = SecuritySchemeIn.HEADER
//)
//public class OpenApiConfig {
//}




//package com.shepherd.shepslibrary.security.securityConfig;
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.info.License;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.annotations.servers.Server;
//
//@OpenAPIDefinition(
//        info = @Info(
//                title = "Sheps Library API",
//                version = "1.0",
//                description = "REST API documentation for Sheps Library system",
//                contact = @Contact(
//                        name = "Sheps Engineering Team",
//                        email = "engineering@shepslibrary.com",
//                        url = "https://shepslibrary.com"
//                ),
//                license = @License(
//                        name = "Apache 2.0",
//                        url = "https://www.apache.org/licenses/LICENSE-2.0"
//                )
//        ),
//        servers = {
//                @Server(
//                        description = "Local",
//                        url = "http://localhost:9092"
//                ),
//                @Server(
//                        description = "Development",
//                        url = "https://dev.shepslibrary.com"
//                ),
//                @Server(
//                        description = "Production",
//                        url = "https://api.shepslibrary.com"
//                )
//        },
//        security = {
//                @SecurityRequirement(name = "bearerAuth")
//        }
//)
//@SecurityScheme(
//        name = "bearerAuth",
//        description = "JWT authentication using Bearer token",
//        scheme = "bearer",
//        bearerFormat = "JWT",
//        type = SecuritySchemeType.HTTP,
//        in = SecuritySchemeIn.HEADER
//)
//public class OpenApiConfig {
//}