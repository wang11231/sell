package com.art.sell.controller;

import com.art.sell.pojo.Msg;
import com.art.sell.pojo.Page;
import com.art.sell.pojo.SysUser;
import com.art.sell.service.SysUserService;
import com.art.sell.service.impl.SysUserServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    private String prefix = "sysUser";
    @Autowired
    SysUserService service;

    @PostMapping("/list")
    @ResponseBody
    public Msg List(SysUser sysUser){
        PageHelper.startPage(1,10);
        List<SysUser> list = new ArrayList<>();
        try {
            list = service.selectSysUSerList(sysUser);
        } catch (Exception e){
            log.error("查询数据异常，请联系管理员!", e.getMessage());
            return new Msg(Msg.FAILURE_CODE, "查询数据异常，请联系管理员!");
        }

        PageInfo<SysUser> info = new PageInfo<>(list);
        int pages = Page.getPages(info.getTotal(), 10);
        Map<String, Object> map = new HashMap<>();
        map.put("total", info.getTotal());
        map.put("pages", pages);
        map.put("list", list);
        return new Msg(Msg.SUCCESS_CODE, map);
    }

    /**
     * 新增添加人员用户
     * @return
     */
    @GetMapping("/add")
    public String add(){
        return prefix + "/add";
    }

    public Msg addSave(){
        try {
            service.insertSysUser(null);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            return new Msg(Msg.FAILURE_CODE, e.getMessage());
        }
        return new Msg(Msg.SUCCESS_CODE,"添加成功");
    }
}
