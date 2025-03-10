package home.hammefatal.springboot.config;

import home.hammefatal.springboot.util.PerformanceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PerformanceInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/fonts/**", "/plugin/**", "/scripts/**", "/js/**");
    }

}
