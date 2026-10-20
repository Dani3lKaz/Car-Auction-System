package com.kazmierczak.daniel.car_auction_platform.config;

import com.kazmierczak.daniel.car_auction_platform.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final FileStorageService fileStorageService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(FileStorageService.URL_PREFIX + "**")
                .addResourceLocations(fileStorageService.getUploadDir().toUri().toString());
    }
}
