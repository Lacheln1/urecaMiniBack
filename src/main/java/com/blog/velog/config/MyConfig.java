package com.blog.velog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfig implements WebMvcConfigurer{
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("http://127.0.0.1:5500/")
		.allowedMethods("*")
		.allowedHeaders("*")
		.allowCredentials(true); //쿠키, 세션 정보도 허용
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploads/** 요청을 프로젝트 루트의 uploads 폴더와 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/Users/psion/Documents/GitHub/urecaMiniBack/uploads/");
    }
	
}