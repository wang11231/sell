package com.art.sell.filter;


import com.art.sell.pojo.Msg;
import com.art.sell.util.JsonUtil;
import com.art.sell.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 校验session是否过期
 *
 * @author Administrator
 * @Description:
 * @Date: Created in 11:37 2018/6/26
 * @Modified By:
 * <p>
 * //跨域问题解决
 */

@Slf4j
public class SessionValidateFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(SessionValidateFilter.class);

    private static final String LOGIN = "/admin/login";


    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token,Authorization,X-UA-Compatible");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
        response.setCharacterEncoding("UTF-8");
        String uri = request.getRequestURI();
        logger.debug("请求的uri为：" + uri);

        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()){
            log.debug(request.getHeader(names.nextElement()));
        }
        log.info("请求的uri为：" + uri);
        if (uri.startsWith(LOGIN)) {
            log.debug("这里的请求路径都不拦截...");
        } else {
            //判断session是否存在
            /*HttpSession session = request.getSession(false);
            if (session == null) {
                Msg msg = new Msg(Msg.SESSSION_TIME_OUT, "超时，请重新登录");
                response.getWriter().write(JsonUtil.objectTojson(msg));
                return;
            }*/
            String token = request.getHeader("Authorization");
            log.info("Authorization:{}", token);
            if (token == null) {
                Msg msg = new Msg(Msg.ILLEGAL_SESSSION, "登录异常，请重新登录");
                response.getWriter().write(JsonUtil.objectTojson(msg));
                return;
            }
            RedisTemplate redisTemplate = (RedisTemplate) SpringUtils.getBean("redisTemplate");
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String result = valueOperations.get(token);
            //判断用户是否发token
            if (StringUtils.isBlank(result)) {
                Msg msg = new Msg(Msg.ILLEGAL_SESSSION, "登录异常，请重新登录");
                response.getWriter().write(JsonUtil.objectTojson(msg));
                return;
            }
            request.setAttribute("username",result);
           /* //根据token获取用户，判断用户信息是否在session里面
            Object o = session.getAttribute(token);
            if (o == null) {
                Msg msg = new Msg(Msg.ILLEGAL_SESSSION, "登录异常，请重新登录");
                response.getWriter().write(JsonUtil.objectTojson(msg));
                return;
            }

            //判断session存储的对象信息是否为用户信息
            if (o.getClass() != SysUser.class) {
                Msg msg = new Msg(Msg.ILLEGAL_SESSSION, "登录异常，请重新登录");
                response.getWriter().write(JsonUtil.objectTojson(msg));
                return;
            }*/
        }
        chain.doFilter(request, response);
    }
}

