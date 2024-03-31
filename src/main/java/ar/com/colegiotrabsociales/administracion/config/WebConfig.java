package ar.com.colegiotrabsociales.administracion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOriginPatterns("*") // Permitir solicitudes desde cualquier origen
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permitir estos métodos HTTP
                .allowedHeaders("Content-Type") // Permitir estos encabezados en las solicitudes
                .allowCredentials(true) // Permitir el envío de credenciales (cookies, tokens de autenticación, etc.)
                .maxAge(3600); // Tiempo máximo que los resultados preflight serán cacheados
    }

}
