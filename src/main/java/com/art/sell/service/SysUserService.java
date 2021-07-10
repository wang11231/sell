package com.art.sell.service;


import com.art.sell.pojo.SysMenu;
import com.art.sell.pojo.SysUser;
import com.art.sell.pojo.SysUserExample;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

/**
 * 用户的业务逻辑
 * @author Administrator
 */
public interface SysUserService {

    /**
     * 查询人员管理用户
     * @param sysUser
     * @return
     */
    public List<SysUser> selectSysUSerList(SysUser sysUser);

    /**
     * 新增人员信息
     * @param sysUser
     * @return
     */
    public int insertSysUser(SysUser sysUser);

    /**
     * 修改人员信息
     * @param sysUser
     * @return
     */
    public int updateSysUser(SysUser sysUser);

    /**
     * 删除人员信息
     * @param id
     * @return
     */
    public int deleteSysUser(String id);
}





