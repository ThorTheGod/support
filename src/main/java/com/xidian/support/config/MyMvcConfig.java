package com.xidian.support.config;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    //视图控制器，伪装url，前者为网页url，后者为实际templates
    // /,/index.html等等这些url的优先级在拦截器之后
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/main.html").setViewName("main");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:upload/");//配置图片物理存储路径和虚拟访问路径
//        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+System.getProperty("user" +
//                ".dir")+"/src/main/resources/static/upload/");//配置图片物理存储路径和虚拟访问路径
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    //拦截器：除了/index.html等等等等不拦截而交给视图控制器处理外，其他全部拦截
    //如用户访问main.html时，首先将被拦截器拦截，然后去loginHandler登录状态判断，如果不在线则跳转至登录页面index.html
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns(
//                "/index.html","/","/user/login","/error")
//                .excludePathPatterns("/js/*","/css/*","/img/*","/upload/*")
//                .excludePathPatterns("/swagger-ui/**")
//                .excludePathPatterns("/webjars/**")
//                .excludePathPatterns("/v2/**")
//                .excludePathPatterns("/v3/**")
//                .excludePathPatterns("/swagger-resources/**");
//    }

    @Bean
    public ServletListenerRegistrationBean<SessionListener> servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<SessionListener> slrBean = new ServletListenerRegistrationBean<SessionListener>();
        slrBean.setListener(new SessionListener());
        return slrBean;
    }

}
