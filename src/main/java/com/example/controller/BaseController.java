package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.service.SysMenuService;
import com.example.service.SysRoleMenuService;
import com.example.service.SysRoleService;
import com.example.service.SysUserRoleService;
import com.example.service.SysUserService;
import com.example.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    HttpServletRequest req;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysRoleMenuService sysRoleMenuService;

    //注意这里的Page 类导包是com.baomidou  的包
    public Page getPage(){
        int current = ServletRequestUtils.getIntParameter(req, "current", 1);
        int size = ServletRequestUtils.getIntParameter(req, "size", 10);
        return new Page(current,size);
    }

}
