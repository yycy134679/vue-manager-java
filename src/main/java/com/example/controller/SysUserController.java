package com.example.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Result;
import com.example.entity.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author teacher
 * @since 2025-11-16
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/list")
    public Result list(String username){
        Page<SysUser> pageDate = sysUserService.page(getPage(),
                new QueryWrapper<SysUser>().like(StrUtil.isNotBlank(username), "username", username));

        return Result.succ(pageDate);
    }

}
