package com.art.sell.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserRole implements Serializable {
    private static final long serialVersionUID = -7481857724100092533L;
    private Long id;

    private Long userId;

    private Long roleId;

    private String roleName;

    private String roleDesc;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}