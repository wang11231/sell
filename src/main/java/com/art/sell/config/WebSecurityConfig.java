package com.art.sell.config;


import com.art.sell.filter.SessionValidateFilter;
import com.art.sell.handler.MyLogoutSuccessHandler;
import com.art.sell.service.impl.SysUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;


/**
 * @author Administrator
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Bean
    UserDetailsService sysUserService() {
        return new SysUserServiceImpl();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 定制用户认证和授权信息
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        /**
         * 对密码采用 BCryptPasswordEncoder 加密方式
         */
        auth.userDetailsService(sysUserService()).passwordEncoder(getPasswordEncoder());
        //auth.authenticationProvider()
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public LogoutSuccessHandler getLogoutHandler() {
        return new MyLogoutSuccessHandler();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll()
                .and()
                .csrf().disable();
        http.addFilterBefore(new SessionValidateFilter(), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement()
                //session创建策略
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                //允许同一用户拥有多个并发session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(getSessionRegistry())
                .and()
                //防止篡改session
                .sessionFixation().migrateSession();

    }

    @Bean
    public SessionRegistry getSessionRegistry(){
        return new SessionRegistryImpl();
    }

    /**
     * To tell Spring to store sessions in the registry
     * @return
     */
    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {	//(5)
        logger.info("httpSessionEventPublisher 是否起作用。。。。");
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }


}
