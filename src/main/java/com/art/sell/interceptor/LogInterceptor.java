package com.art.sell.interceptor;

import com.art.sell.util.HexUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ThreadLocalRandom;

public class LogInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        byte[] bytes = new byte[16];
        ThreadLocalRandom.current().nextBytes(bytes);
        String traceLogId = HexUtils.byteArrayToHex(bytes, false);
        MDC.put("LOG_ID", traceLogId);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.clear();
        super.afterCompletion(request, response, handler, ex);
    }
}
