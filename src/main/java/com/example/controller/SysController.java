package com.example.controller;

import com.example.common.lang.Result;
import com.example.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys")
public class SysController extends BaseController {

    @Autowired
    SysUserService sysUserService;

    // userInfo 接口已移至 AuthController，避免路由冲突

}
