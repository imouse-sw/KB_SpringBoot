package com.example.kidzbraindb; // O tu paquete correspondiente

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Esto expone la carpeta uploads que está en la raíz de tu proyecto
        String projectDir = System.getProperty("user.dir");
        String uploadPath = "file:" + projectDir + File.separator + "uploads" + File.separator;

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);

        System.out.println("Carpeta de recursos expuesta en: " + uploadPath);
    }
}