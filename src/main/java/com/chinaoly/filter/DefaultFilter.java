package com.chinaoly.filter;

import com.chinaoly.frm.core.filter.JwtAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;


/**
 * 所有 filter 配置文件
 */
//@Configuration
public class DefaultFilter {

    // 默认jwt过滤器，验证token，可更换过滤器
    @Bean
    public FilterRegistrationBean jwtFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        //注入过滤器
        registrationBean.setFilter(new JwtAuthFilter());
        registrationBean.setUrlPatterns(setUrl());
        //设置过滤器名称
        registrationBean.setName("jwt过滤器");
        //设置过滤器顺序
        registrationBean.setOrder(Integer.MIN_VALUE);
        return registrationBean;
    }

    // 控制器过滤器顺序
//    @Bean
//    public FilterRegistrationBean controllerFilterRegistrationBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(new ControllerFilter());
//        //设置过滤器顺序
//        registrationBean.setOrder(Integer.MIN_VALUE + 1);
//        return registrationBean;
//    }

    // 设置需要过滤的路径
    private static final List<String> setUrl() {
        List<String> urlPatterns = new ArrayList<String>();
//      urlPatterns.add("/log/info/*"); //需要过滤的路径

        return urlPatterns;
    }
}
