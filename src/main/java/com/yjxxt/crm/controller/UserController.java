package com.yjxxt.crm.controller;

import com.yjxxt.crm.annotation.RequiredPermission;
import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.exceptions.ParamsException;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("toPasswordPage")
    public String updatePwd(){
        return "user/password";
    }


    @RequestMapping("index")
    public String index(){
        return "user/user";
    }


    /**
     * 进入用户添加或更新页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateUserPage")
    public String addUserPage(Integer id, Model model){
        if(null != id){
            model.addAttribute("user",userService.selectByPrimaryKey(id));
        }
        return "user/add_update";
    }


    @RequestMapping("toSettingPage")
    public String setting(HttpServletRequest req){
//        获取用户Id
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
//        执行调用方法
        User user = userService.selectByPrimaryKey(userId);
//        存储
        req.setAttribute("user",user);
//        转发
        return "user/setting";
    }

    @RequestMapping("login")
    @ResponseBody
    public ResultInfo say(User user){
        System.out.println(user);
        ResultInfo resultInfo=new ResultInfo();
//        try {
            UserModel userModel = userService.userlogin(user.getUserName(), user.getUserPwd());
//           因为继承了base，base中ResultInfo中有结果
////            resultInfo.setCode(200);
///           resultInfo.setMsg("登录成功了");
            resultInfo.setResult(userModel);
//
//        }catch (ParamsException ex) {//自定义异常
//            ex.printStackTrace();
////            设置状态码和提示信息
//            resultInfo.setCode(ex.getCode());
//            resultInfo.setMsg(ex.getMsg());
//        }catch (Exception e){
//            e.printStackTrace();
//            resultInfo.setCode(500);
//            resultInfo.setMsg("操作失败");
//        }
        return resultInfo;
    }
    @RequestMapping("setting")
    @ResponseBody
    public ResultInfo sayUpdate(User user){
        ResultInfo resultInfo = new ResultInfo();
//        修改信息
        userService.updateByPrimaryKeySelective(user);
//        返回目标数据对象
        return resultInfo;
    }



    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveUser(User user) {
        userService.saveUser(user);
        return success("用户添加成功！");
    }



    /**
     * 更新用户
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("用户更新成功！");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer[] ids) {
        userService.removeUserIds(ids);
        return success("批量删除用户ok！");
    }




    @PostMapping("updataPwd")
    @ResponseBody
    public ResultInfo updataPwd(HttpServletRequest req,String oldPassword,String newPassword,String comfirmPwd){
        ResultInfo resultInfo = new ResultInfo();
//        获取Cookie中的userId
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
//        修改密码操作
       // try {
            userService.changeUserPwd(userId,oldPassword,newPassword,comfirmPwd);
//        }catch (ParamsException pe){
//            pe.printStackTrace();
//            resultInfo.setCode(pe.getCode());
//            resultInfo.setMsg(pe.getMsg());
//        }catch (Exception ex){
//            ex.printStackTrace();
//
//            resultInfo.setCode(300);
//            resultInfo.setMsg("操作失败");
//        }
        return resultInfo;
    }


    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String, Object>> findSales(){
        List<Map<String, Object>> list = userService.querySales();
        return list;

    }



    @RequestMapping("list")
    @ResponseBody
    @RequiredPermission(code = "10")
    public Map<String, Object> list(UserQuery userQuery){
        return   userService.findUserByParams(userQuery);


    }
}
