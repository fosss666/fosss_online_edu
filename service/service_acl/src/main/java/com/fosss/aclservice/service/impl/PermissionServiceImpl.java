package com.fosss.aclservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.aclservice.entity.Permission;
import com.fosss.aclservice.entity.RolePermission;
import com.fosss.aclservice.entity.User;
import com.fosss.aclservice.helper.MemuHelper;
import com.fosss.aclservice.helper.PermissionHelper;
import com.fosss.aclservice.mapper.PermissionMapper;
import com.fosss.aclservice.service.PermissionService;
import com.fosss.aclservice.service.RolePermissionService;
import com.fosss.aclservice.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;
    
    @Autowired
    private UserService userService;
    
    //获取全部菜单
    @Override
    public List<Permission> queryAllMenu() {

        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);

        List<Permission> result = bulid(permissionList);

        return result;
    }

    //根据角色获取菜单
    @Override
    public List<Permission> selectAllMenu(String roleId) {
        List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));

        //根据角色id获取角色权限
        List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id",roleId));
        //转换给角色id与角色权限对应Map对象
//        List<String> permissionIdList = rolePermissionList.stream().map(e -> e.getPermissionId()).collect(Collectors.toList());
//        allPermissionList.forEach(permission -> {
//            if(permissionIdList.contains(permission.getId())) {
//                permission.setSelect(true);
//            } else {
//                permission.setSelect(false);
//            }
//        });
        for (int i = 0; i < allPermissionList.size(); i++) {
            Permission permission = allPermissionList.get(i);
            for (int m = 0; m < rolePermissionList.size(); m++) {
                RolePermission rolePermission = rolePermissionList.get(m);
                if(rolePermission.getPermissionId().equals(permission.getId())) {
                    permission.setSelect(true);
                }
            }
        }


        List<Permission> permissionList = bulid(allPermissionList);
        return permissionList;
    }

    //给角色分配权限
    @Override
    public void saveRolePermissionRealtionShip(String roleId, String[] permissionIds) {

        rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id", roleId));

  

        List<RolePermission> rolePermissionList = new ArrayList<>();
        for(String permissionId : permissionIds) {
            if(StringUtils.isEmpty(permissionId)) continue;
      
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionList.add(rolePermission);
        }
        rolePermissionService.saveBatch(rolePermissionList);
    }

    //递归删除菜单
    @Override
    public void removeChildById(String id) {
        List<String> idList = new ArrayList<>();
        this.selectChildListById(id, idList);

        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }

    //根据用户id获取用户菜单
    @Override
    public List<String> selectPermissionValueByUserId(String id) {

        List<String> selectPermissionValueList = null;
        if(this.isSysAdmin(id)) {
            //如果是系统管理员，获取所有权限
            selectPermissionValueList = baseMapper.selectAllPermissionValue();
        } else {
            selectPermissionValueList = baseMapper.selectPermissionValueByUserId(id);
        }
        return selectPermissionValueList;
    }

    @Override
    public List<JSONObject> selectPermissionByUserId(String userId) {
        List<Permission> selectPermissionList = null;
        if(this.isSysAdmin(userId)) {
            //如果是超级管理员，获取所有菜单
            selectPermissionList = baseMapper.selectList(null);
        } else {
            selectPermissionList = baseMapper.selectPermissionByUserId(userId);
        }

        List<Permission> permissionList = PermissionHelper.bulid(selectPermissionList);
        List<JSONObject> result = MemuHelper.bulid(permissionList);
        return result;
    }

    /**
     * 判断用户是否系统管理员
     * @param userId
     * @return
     */
    private boolean isSysAdmin(String userId) {
        User user = userService.getById(userId);

        if(null != user && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }

    /**
     *	递归获取子节点
     * @param id
     * @param idList
     */
    private void selectChildListById(String id, List<String> idList) {
        List<Permission> childList = baseMapper.selectList(new QueryWrapper<Permission>().eq("pid", id).select("id"));
        childList.stream().forEach(item -> {
            idList.add(item.getId());
            this.selectChildListById(item.getId(), idList);
        });
    }

    /**
     * 使用递归方法建菜单
     * @param treeNodes
     * @return
     */
    private static List<Permission> bulid(List<Permission> treeNodes) {
        List<Permission> trees = new ArrayList<>();
        for (Permission treeNode : treeNodes) {
            if ("0".equals(treeNode.getPid())) {
                treeNode.setLevel(1);
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    private static Permission findChildren(Permission treeNode,List<Permission> treeNodes) {
        treeNode.setChildren(new ArrayList<Permission>());

        for (Permission it : treeNodes) {
            if(treeNode.getId().equals(it.getPid())) {
                int level = treeNode.getLevel() + 1;
                it.setLevel(level);
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return treeNode;
    }


    //========================递归查询所有菜单================================================
    //获取全部菜单
    @Override
    public List<Permission> queryAllMenuGuli() {
        //1 查询菜单表所有数据
        LambdaQueryWrapper<Permission> wrapper=new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Permission::getId);
        List<Permission> permissionList = baseMapper.selectList(wrapper);

        //2 把查询所有菜单list集合按照要求进行封装
        //创建封装后的最终集合
        List<Permission> finalList=bulidPermission(permissionList);

        return finalList;
    }

    //把返回所有菜单list集合进行封装的方法
    public static List<Permission> bulidPermission(List<Permission> permissionList) {

        //创建list集合，用于数据最终封装
        List<Permission> finalList=new ArrayList<>();
        //把所有菜单list集合遍历，得到顶层菜单 pid=0菜单，设置level是1
        for (Permission permission : permissionList) {
            if("0".equals(permission.getPid())){
                permission.setLevel(1);
                //向顶层菜单中添加子菜单
                finalList.add(selectChildren(permission,permissionList));
            }
        }
        return finalList;
    }

    //递归查询并封装子菜单
    private static Permission selectChildren(Permission permissionNode, List<Permission> permissionList) {
        List<Permission> list=new ArrayList<>();//这个new的集合必须放在循环外边，保证能添加到所有的permission
        //遍历所有数据，找到子菜单pid=父菜单id的数据
        for (Permission permission : permissionList) {

            if(permission.getPid().equals( permissionNode.getId())){
                //进行封装
                //设置level为父菜单level+1
                permission.setLevel(permissionNode.getLevel()+1);
                list.add(permission);

                //查找该子菜单的子菜单
                selectChildren(permission,permissionList);
            }
        }
        permissionNode.setChildren(list);//设置子菜单这部分，设置成最终添加完毕的list
        return permissionNode;
    }

    //============递归删除菜单==================================
    @Override
    public void removeChildByIdGuli(String id) {
        //将要删除的id封装为一个集合
        List<String> list=new ArrayList<>();

        //向集合中添加要删除的id
        this.selectPermissionChildById(id,list);
        //添加当前菜单的id
        list.add(id);

//        baseMapper.deleteBatchIds(list);//直接调用删除方法不能实现逻辑删除
        //采用逐个遍历修改的方式
        for (String s : list) {
            Permission permission = baseMapper.selectById(s);
            permission.setIsDeleted(1);
            LambdaQueryWrapper<Permission> wrapper=new LambdaQueryWrapper<>();
            wrapper.eq(Permission::getId,s);
            baseMapper.update(permission,wrapper);
        }
    }

    //2 根据当前菜单id，查询菜单里面子菜单id，封装到list集合
    private void selectPermissionChildById(String id, List<String> idList) {
        LambdaQueryWrapper<Permission> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPid,id);
        //查询所有子菜单
        List<Permission> permissionList = baseMapper.selectList(wrapper);
        //遍历子菜单集合及其子菜单，封装id
        for (Permission permission : permissionList) {
            idList.add(permission.getId());
            selectPermissionChildById(permission.getId(),idList);
        }
    }

    //=========================给角色分配菜单=======================
    @Override
    public void saveRolePermissionRealtionShipGuli(String roleId, String[] permissionIds) {
        //roleId角色id
        //permissionId菜单id 数组形式

        //先将该roleId对应的权限删除，再重新添加
        LambdaQueryWrapper<RolePermission> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId,roleId);
        rolePermissionService.remove(wrapper);

        //1 创建list集合，用于封装添加数据
        List<RolePermission> rolePermissionList = new ArrayList<>();
        //遍历所有菜单数组
        for(String perId : permissionIds) {
            //RolePermission对象
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(perId);
            //封装到list集合
            rolePermissionList.add(rolePermission);
        }
        //添加到角色菜单关系表
        RolePermission specialPer = new RolePermission();
        specialPer.setRoleId(roleId);
        specialPer.setPermissionId("1");
        rolePermissionList.add(specialPer);
        rolePermissionService.saveBatch(rolePermissionList);
    }
}
