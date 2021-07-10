package com.art.sell.controller;

import com.art.sell.pojo.Msg;
import com.art.sell.pojo.SysUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/admin")
public class LoginController {

    @PostMapping("/login")
    public Msg login(@RequestBody SysUser sysUser, HttpServletRequest request){
        return new Msg(Msg.SUCCESS_CODE, "成功");
    }
}
