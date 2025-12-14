package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.dto.SysMenuDto;
import com.example.entity.SysMenu;
import com.example.entity.SysUser;
import com.example.mapper.SysMenuMapper;
import com.example.mapper.SysUserMapper;
import com.example.service.SysMenuService;
import com.example.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author teacher
 * @since 2025-11-16
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Override
    public List<SysMenuDto> getCurrentUserNav() {
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getByUsername(username);
        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
        List<SysMenu> menus = this.listByIds(menuIds);
        //转换成树状结构
        List<SysMenu> menuTree = buildTreeMenu(menus);
        //实体转成DTO
        return convert(menuTree);
    }

    private List<SysMenuDto> convert(List<SysMenu> menuTree) {
        List<SysMenuDto> menuDtos = new ArrayList<>();
        menuTree.forEach(m->{
            SysMenuDto dto = new SysMenuDto();
            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());
            if(m.getChildren().size()>0){
                dto.setChildren(convert(m.getChildren()));
            }
            menuDtos.add(dto);
        });
        return menuDtos;
    }

    private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {
        List<SysMenu> finalMenus = new ArrayList<>();

        for(SysMenu menu :menus){
            for (SysMenu e : menus){
                if(menu.getId() == e.getParentId()){
                    menu.getChildren().add(e);
                }
            }
            if(menu.getParentId() == 0L){
                finalMenus.add(menu);
            }
        }
        return finalMenus;
    }

    @Override
    public List<SysMenu> tree() {
        // 1. 获取全部菜单的信息
        List<SysMenu> menus = this.list(new QueryWrapper<SysMenu>().orderByAsc("orderNum"));
        
        // 2. 转换成树状接口
        return buildTreeMenu(menus);
    }
}
