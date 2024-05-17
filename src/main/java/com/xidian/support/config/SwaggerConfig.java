package com.xidian.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author thornoir
 * @date 2021/12/13
 * @apiNote
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket(Environment environment){
        //多环境 开关swagger  environment
        //设置要显示swagger 的环境
        Profiles profiles=Profiles.of ("dev","test");
        //判断当前环境是否是profiles中显示swagger的环境
        boolean b = environment.acceptsProfiles (profiles);


        return new Docket (DocumentationType.SWAGGER_2)
                .apiInfo (apiInfo ())
                .groupName ("jsp")   //配置Api文档分组   多个Docket分组
                .enable (b)   //是否启动swagger 如果为false则swagger不能再浏览器中访问
                .select ()
//                .apis (RequestHandlerSelectors.basePackage("com.ji.controller"))
//                .paths (PathSelectors.ant ("/ji/**"))
                .build ();
    }
    @Bean
    public Docket docket1(){
        return new Docket (DocumentationType.SWAGGER_2)
                .apiInfo (apiInfo ())
                .groupName ("hcy");   //配置Api文档分组
    }
    @Bean
    public Docket docket2(){
        return new Docket (DocumentationType.SWAGGER_2)
                .apiInfo (apiInfo ())
                .groupName ("zzz");   //配置Api文档分组
    }
    //配置swagger信息
    private ApiInfo apiInfo(){
        return new ApiInfo ("Jsp SwaggerLogger",
                "学习",
                "17.0",
                "https://www.cnblogs.com/Liuyunsan/",
                new Contact("jsp", "https://www.cnblogs.com/Liuyunsan/", "2315510122@qq.com"),   //作者信息
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());
    }
}
