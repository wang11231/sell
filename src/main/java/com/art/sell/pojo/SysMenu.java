package com.art.sell.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 4915965178208693613L;
    private Integer id;

    private Integer seq;

    private String menuClass;

    private String menuName;

    private String menuUrl;

    private String menuDesc;

    private Integer parentId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private List<SysMenu> menus = new ArrayList<>();

}