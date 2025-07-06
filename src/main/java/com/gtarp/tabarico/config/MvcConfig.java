package com.gtarp.tabarico.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // Injecte le chemin d'upload que tu as défini dans application.properties
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Enregistre un gestionnaire de ressources pour le chemin web "/images/identities/"
        registry.addResourceHandler("/images/identities/**")
                // Mappe ce chemin web à l'emplacement physique des fichiers
                // "file:" est crucial pour indiquer qu'il s'agit d'un chemin système de fichiers
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
