package com.art.sell.service.impl;

import com.art.sell.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author snow
 */
@EnableTransactionManagement
@Service
public class SysUserServiceImpl implements SysUserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
