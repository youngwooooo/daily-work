package com.work.daily.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 서버에 저장된 파일들을 웹에서 접근할 수 있도록 설정
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${custom.path.profile-image}")
    private String profileUploadPath;

    @Value("${custom.path.mission-image}")
    private String missionUploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
            addResourceHandler() : 화면에서 접근하는 url 기본 설정 -> http:localhost:8888/profile/**
            addResourceLocations() : 파일이 저장된 위치 기본 설정
                                     "file:///"을 앞에 붙여줘야 파일로 인식한다.
         */
        registry.addResourceHandler("/profile/**")
                .addResourceLocations("file:///" + profileUploadPath);

        registry.addResourceHandler("/mission/**")
                .addResourceLocations("file:///" + missionUploadPath);
    }
}
