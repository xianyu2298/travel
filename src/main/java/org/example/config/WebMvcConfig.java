package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 为/api/weather路径添加更宽松的CORS配置
        registry.addMapping("/api/weather/**")
                .allowedOrigins("*")
                .allowedMethods("*")  // 改为允许所有方法
                .allowedHeaders("*")  // 添加允许所有请求头
                .allowCredentials(false)
                .maxAge(3600);

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
