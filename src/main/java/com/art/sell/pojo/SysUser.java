package com.art.sell.pojo;


import com.art.sell.vo.SysRoleVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SysUser implements UserDetails, Serializable {

    private static final long serialVersionUID = 916913492916869934L;
    private Long id;

    private String username;

    private transient String password;

    private String telephone;

    private String email;

    private String desc;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Long[] roles;

    private List<SysRoleVo> userRoles = new ArrayList<>();

    private List<SysMenu> userMenu = new ArrayList<>();

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


}