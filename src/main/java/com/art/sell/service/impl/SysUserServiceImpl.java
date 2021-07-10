package com.art.sell.service.impl;

import com.art.sell.mapper.SysUserMapper;
import com.art.sell.pojo.SysUser;
import com.art.sell.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author snow
 */
@EnableTransactionManagement
@Service
public class SysUserServiceImpl implements SysUserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }


    /**
     * 查询人员管理用户
     * @param sysUser
     * @return
     */
    @Override
    public List<SysUser> selectSysUSerList(SysUser sysUser) {
        return sysUserMapper.selectSysUSerList(sysUser);
    }

    /**
     * 新增人员信息
     * @param sysUser
     * @return
     */
    @Override
    public int insertSysUser(SysUser sysUser) {
        return sysUserMapper.insertSysUser(sysUser);
    }

    /**
     * 修改人员信息
     * @param sysUser
     * @return
     */
    @Override
    public int updateSysUser(SysUser sysUser) {
        return sysUserMapper.updateSysUser(sysUser);
    }

    /**
     * 删除人员信息
     * @param id
     * @return
     */
    @Override
    public int deleteSysUser(String id) {
        return sysUserMapper.deleteSysUser(id);
    }
}
