package com.yjxxt.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.yjxxt.crm.base.BaseService;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.bean.UserRole;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.mapper.UserRoleMapper;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.utils.AssertUtil;
import com.yjxxt.crm.utils.Md5Util;
import com.yjxxt.crm.utils.PhoneUtil;
import com.yjxxt.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> {


    @Resource
    private UserMapper userMapper;


    @Resource
    private UserRoleMapper userRoleMapper;


    public UserModel userlogin(String userName, String userPwd) {
//         用户非空
//        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
//       密码非空
//        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
//        用户是否存在
        checkUserLoginParam(userName, userPwd);
        User temp = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(temp == null, "用户不存在");
//        用户密码是否正确
        checkUserPwd(userPwd, temp.getUserPwd());
//        构建返回对象
        return buildUserInfo(temp);

    }

    /**
     * 构造返回目标对象
     *
     * @param user
     * @return
     */
    private UserModel buildUserInfo(User user) {
//      实例化目标对象
        UserModel userModel = new UserModel();
//        加密
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
//       返回目标对象
        return userModel;
    }

    /**
     * 校验用户密码
     *
     * @param userName
     * @param userPwd
     */
    private void checkUserLoginParam(String userName, String userPwd) {
//        用户非空
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空");
//       密码非空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "用户密码不能为空");
    }

    /**
     * 验证密码
     *
     * @param userPwd
     * @param userPwd1
     */
    private void checkUserPwd(String userPwd, String userPwd1) {
//        对输入的密码加密
        userPwd = Md5Util.encode(userPwd);
//       加密的密码和数据中的密码对比
        AssertUtil.isTrue(!userPwd.equals(userPwd1), "用户密码不正确");
    }

    /**
     * 验证用户是否存在和是否修改密码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param comfirmwPwd
     */
    public void changeUserPwd(Integer userId, String oldPassword, String newPassword, String comfirmwPwd) {
//        用户存在，用户登录修改cookie中有id就代表着登录
        User user = userMapper.selectByPrimaryKey(userId);
//        密码验证
        checkPasswordParams(user, oldPassword, newPassword, comfirmwPwd);

//        真正的修改密码
        user.setUserPwd(Md5Util.encode(newPassword));
//        确认密码修改是否成功
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "修改失败");


    }

    /**
     * 修改密码的验证
     *
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param comfirmPwd
     */
    private void checkPasswordParams(User user, String oldPassword, String newPassword, String comfirmPwd) {
        AssertUtil.isTrue(user == null, "用户未登录或者不存在");
        //        原始密码非空
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword), "请输入原始密码");
//       原始密码是否正确
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPassword)), "原始密码不正确");
        //        新密码非空
        AssertUtil.isTrue(StringUtils.isBlank(newPassword), "新密码不能为空");
        //        新密码不能和原始密码一致
        AssertUtil.isTrue(newPassword.equals(oldPassword), "新密码和原始密码不能相同");

//        确认密码非空
        AssertUtil.isTrue(StringUtils.isBlank(comfirmPwd), "确认密码不能为空");
//        确认密码和新密码一致
        AssertUtil.isTrue(!comfirmPwd.equals(newPassword), "确认密码和新密码是否一致");
    }

    //    查询所有的销售人员
    public List<Map<String, Object>> querySales() {

        List<Map<String, Object>> maps = userMapper.selectSales();
        return maps;

    }

    /**
     * 用户模块的列表查询
     *
     * @param userQuery 条件
     * @return
     */
//准备数据
    public Map<String, Object> findUserByParams(UserQuery userQuery) {
        //实例化Map
        Map<String, Object> map = new HashMap<String, Object>();
//      初始化分页单位
        PageHelper.startPage(userQuery.getPage(), userQuery.getLimit());
//        开始分页
        PageInfo<User> plist = new PageInfo<User>(selectByParams(userQuery));

        map.put("code", 0);
        map.put("msg", "succsee");
        map.put("count", plist.getTotal());
        map.put("data", plist.getList());
//返回目标map
        return map;
    }

    /**
     * 添加用户
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUser(User user) {
// 1. 参数校验
        checkParams(user.getUserName(), user.getEmail(), user.getPhone());
// 2. 设置默认参数
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
// 3. 执行添加，判断结果
       // AssertUtil.isTrue(userMapper.insertHasKey(user) < 1, "用户添加失败！");
        AssertUtil.isTrue(userMapper.insertSelective(user) < 1, "用户添加失败！");
        relaiouUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 操作中间表
     * @param userId   用户id
     * @param roleIds   角色id 1,2,4；
     *                  原来的角色数量
     *                  没有角色  添加新的而角色
     *                  有角色   新增角色  减少角色
     *                  最好的方法：把之前的角色删了，再添加新的角色
     *                  1、先删除
     *                  2、新添加角色
     *
     */
    private void relaiouUserRole(Integer userId, String roleIds) {
        /**
         * 用户角色分配
         * 原始角色不存在 添加新的角色记录
         * 原始角色存在 添加新的角色记录
         * 原始角色存在 清空所有角色
         * 原始角色存在 移除部分角色
         * 如何进行角色分配???
         * 如果用户原始角色存在 首先清空原始所有角色 添加新的角色记录到用户角色表
         */
        int count = userRoleMapper.countUserRoleNum(userId);
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUSerID(userId) != count,
                    "用户角色分配失败!");
        }
        if (StringUtils.isNotBlank(roleIds)) {
//重新添加新的角色
            List<UserRole> userRoles = new ArrayList<UserRole>();
            for (String s : roleIds.split(",")) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles) <
                    userRoles.size(), "用户角色分配失败!");
        }
    }


    /**
     * 参数校验
     *
     * @param userName
     * @param email
     * @param phone
     */
    private void checkParams(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空！");
// 验证用户名是否存在
        User temp = userMapper.selectUserByName(userName);
        AssertUtil.isTrue(null != temp, "该用户已存在！");
        AssertUtil.isTrue(StringUtils.isBlank(email), "请输入邮箱地址！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号码格式不正确！");
    }
    private void checkParamsByName(String userName, String email, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空！");
// 验证用户名是否存在
        User temp = userMapper.selectUserByName(userName);
//        AssertUtil.isTrue(null != temp, "该用户已存在！");

        if(null != temp){
            temp.setUserName(userName);
            temp.setEmail(email);
            temp.setPhone(phone);
            temp.setUpdateDate(new Date());
            AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(temp)<1,"失败");
        }
        AssertUtil.isTrue(StringUtils.isBlank(email), "请输入邮箱地址！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone), "手机号码格式不正确！");
        //这里用户记录更新代码省略
/**
 * 用户角色分配
 * userId
 * roleIds
 */
        Integer userId = userMapper.selectUserByName(temp.getUserName()).getId();
        relaiouUserRole(userId, temp.getRoleIds());

    }
    /**
     * 更新用户
     *
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
// 1. 参数校验
// 通过id查询用户对象
        User temp = userMapper.selectByPrimaryKey(user.getId());
// 判断对象是否存在
        AssertUtil.isTrue(temp == null, "待更新记录不存在！");
// 验证参数
        checkParamsByName(user.getUserName(), user.getEmail(), user.getPhone());
// 2. 设置默认参数
        temp.setUpdateDate(new Date());
// 3. 执行更新，判断结果
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户更新 失败！");

        relaiouUserRole(user.getId(),user.getRoleIds());
    }

//    批量删除数据
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeUserIds(Integer[] ids){
//        验证
        AssertUtil.isTrue(ids == null || ids.length == 0, "请选择删除数据");
//        遍历对象
        for (Integer userId: ids){
//            统计当前用户有多少角色
            int count = userRoleMapper.countUserRoleNum(userId);
//            删除当前用户的角色
            if (count>0){
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUSerID(userId)!=count,"用户角色删除失败");
            }
        }
//        判断删除是否成功
        AssertUtil.isTrue(userMapper.deleteBatch(ids)<1,"删除失败了");
    }
}
