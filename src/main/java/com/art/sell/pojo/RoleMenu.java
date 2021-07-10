package com.art.sell.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RoleMenu implements Serializable {
    private static final long serialVersionUID = -4701292352616514594L;
    private Integer id;

    private Integer roleId;

    private Integer menuId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}