package com.memorias.diario.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Pega os valores definidos em application.properties
    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${upload.url-path}")
    private String urlPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Converte o caminho para o formato 'file:/' que o Spring entende
        String finalUploadPath = "file:" + uploadDir;

        // Mapeia a URL (/fotos-perfil/**) para o diretório físico (file:./uploads/fotosPerfil/)
        registry.addResourceHandler(urlPath)
                .addResourceLocations(finalUploadPath);
    }
}