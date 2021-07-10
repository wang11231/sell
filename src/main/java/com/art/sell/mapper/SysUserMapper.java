package com.art.sell.mapper;

import com.art.sell.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 人员管理
 */
@Mapper
public interface SysUserMapper {

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
