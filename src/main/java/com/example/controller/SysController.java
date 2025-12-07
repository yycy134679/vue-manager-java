package com.example.controller;

import com.example.common.lang.Result;
import com.example.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys")
public class SysController extends BaseController {

    @Autowired
    SysUserService sysUserService;

    @GetMapping("/userInfo")
    public Result userInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("id", 0);
        data.put("username", username != null ? username : "用户");
        data.put("avatar", "https://via.placeholder.com/80");
        return Result.succ(data);
    }

}
