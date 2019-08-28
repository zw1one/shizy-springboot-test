package com.shizy.config;


import com.shizy.common.filter.DebugFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 过滤器配置
 *
 * 若使用@WebFilter注册过滤器，无法指定过滤器执行顺序，故使用config
 */
@Configuration
public class FilterConfig {

    /**
     * debug
     */
    @Bean
    public Filter DebugFilter() {
        return new DebugFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean1() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(DebugFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);//order的数值越小 则优先级越高
        return filterRegistrationBean;
    }


}