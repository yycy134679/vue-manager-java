package com.example.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.lang.Result;
import com.example.entity.SysUser;
import com.example.service.SysUserService;
import com.example.service.impl.UserDetailServiceImpl;
import com.example.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private static final Set<String> URL_WHITELIST = new HashSet<>(Arrays.asList(
            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico"
    ));

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailServiceImpl userDetailsService;

    @Autowired
    SysUserService sysUserService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String uri = request.getRequestURI();
        // 放行白名单请求，避免未登录场景下验证码等接口被 JWT 拦截
        if (URL_WHITELIST.contains(uri)) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = request.getHeader(jwtUtils.getHeader());

        // 注意：图片中写的是 isBlankOrUndefined，如果是标准 Hutool 可能是 isBlank
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Claims claim = jwtUtils.getClaimByToken(jwt);

            if (claim == null) {
                throw new JwtException("token 异常");
            }

            if (jwtUtils.isTokenExpired(claim)) {
                throw new JwtException("token 已过期");
            }

            String username = claim.getSubject();
            SysUser sysUser = sysUserService.getByUsername(username);
            if (sysUser == null) {
                throw new JwtException("用户不存在");
            }

            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(username, null, userDetailsService.getUserAuthority(sysUser.getId()));

            SecurityContextHolder.getContext().setAuthentication(token);
            chain.doFilter(request, response);
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Result result = Result.fail(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), null);
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
        }
    }
}
