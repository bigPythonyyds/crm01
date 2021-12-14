package com.yjxxt.crm.service;

import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.dto.TreeDto;
import com.yjxxt.crm.mapper.ModuleMapper;
import com.yjxxt.crm.mapper.PermissionMapper;
import org.springframework.stereotype.Service;


import com.yjxxt.crm.bean.Module;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModuleService extends BaseService<Module, Integer> {

    @Resource
    private ModuleMapper moduleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 查询所有的资源信息
     * @return
     */
    public List<TreeDto> findModules(){
        return moduleMapper.selectModules();
    }

    public List<TreeDto> findModulesByRoleId(Integer roleId){
//        获取所有资源信息
        List<TreeDto> tlist = moduleMapper.selectModules();
//        获取当前角色的拥有的资源信息
        List<Integer> roleHasModuls=  permissionMapper.selectModulByRoleId(roleId);
//        遍历
        for (TreeDto treeDto:tlist) {
            if (roleHasModuls.contains(treeDto.getId())){
                treeDto.setChecked(true);
            }
        }
        //        判断对比， checked=true
        return tlist;
    }
}
