package com.example.controller;

import com.example.service.SysMenuService;
import com.example.service.SysRoleMenuService;
import com.example.service.SysRoleService;
import com.example.service.SysUserRoleService;
import com.example.service.SysUserService;
import com.example.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

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

}
