package com.art.sell.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SysRole implements Serializable {
    private static final long serialVersionUID = -7424065454463165574L;
    private Integer id;

    private String roleName;

    private String roleDesc;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    }