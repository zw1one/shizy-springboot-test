package com.shizy.common.filter;


import com.shizy.utils.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 配置见FilterConfig.java
 */
public class DebugFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(DebugFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        logger.info("=========debug request headers==========");
        logger.info(HttpUtil.getHeadersInfo((HttpServletRequest) request));

        long time1 = System.currentTimeMillis();

        chain.doFilter(request, response);

        long time2 = System.currentTimeMillis();
        logger.info("========================================");
        logger.info("--server request process time: " + (time2 - time1) + "ms");
        logger.info("========================================");

    }

    @Override
    public void destroy() {

    }
}
