package com.art.sell.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SysRoleVo implements Serializable {

    private static final long serialVersionUID = 2819824577323218416L;

    private Long id;

    private String roleName;

    private String roleDesc;

    private Date createTime;

}
