package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.SysMenu;
import com.example.entity.SysRole;
import com.example.entity.SysUser;
import com.example.mapper.SysUserMapper;
import com.example.service.SysMenuService;
import com.example.service.SysRoleService;
import com.example.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author teacher
 * @since 2025-11-16
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysMenuService sysMenuService;

    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    //ROLE_admin,Role_normal
    @Override
    public String getUserAuthorityInfo(Long userId) {

        String authority = "";
        //获取角色权限信息
        List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
                .inSql("id", "select role_id from sys_user_role where user_id =" + userId));

        if (roles.size() > 0) {
            String roleCodes = roles.stream().map(r -> "ROLE_" + r.getCode())
                    .collect(Collectors.joining(","));
            authority = roleCodes.concat(",");
        }

        //获取菜单权限信息
        List<Long> menuId = sysUserMapper.getNavMenuIds(userId);
        if (menuId.size() > 0) {
            List<SysMenu> menus = sysMenuService.listByIds(menuId);
            String menuPerms = menus.stream().map(m -> m.getPerms())
                    .collect(Collectors.joining(","));
            authority = authority.concat(menuPerms);
        }

        return authority;
    }
}
