package com.niit.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.niit.blog.interceptor.LoginInterceptor;

@Configuration
public class MyBlogWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;
    
    @Autowired
    private ConstConfiguration constConfig;

    /**
     * @Description: 注册拦截器
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加一个拦截器，拦截以/user为前缀的url路径
        registry.addInterceptor(loginInterceptor)
		        .addPathPatterns("/user/**")
		        .excludePathPatterns("/user/login")
		        .excludePathPatterns("/user/dist/**")
		        .excludePathPatterns("/user/plugins/**");
    }

    /**
     * @Description: 静态资源（图片）访问配置
     * @param registry
     */
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	//log.info("调用了addResourceHandlers方法...");
        registry.addResourceHandler("/upload/**")
        		.addResourceLocations(constConfig.getUploadPath()); // 从配置文件中读取
    }
}
