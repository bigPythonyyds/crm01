layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    //监听提交
    form.on('submit(saveBtn)', function(data){
        // layer.msg(JSON.stringify(data.field));
        var fieldData = data.field;

        //发送ajax
        $.ajax({
            type:"post",
            url:ctx+"/user/updataPwd",
            data:{
                "oldPassword":fieldData.old_password,
                "newPassword":fieldData.new_password,
                "comfirmPwd":fieldData.again_password
            },
            dataType:"json",
            success:function (data){
                layer.msg(data.code)
                if (data.code===200){
                    layer.msg("修改密码成功了，系统三秒后退出", function(){
                        console.log("ok");
                        //   清空Cookie信息
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        //    跳转父类页面
                        window.parent.location.href = ctx + "/index";
                    });
                }else {
                //    登录失败的提示
                    layer.msg(data.msg);
                }
            }
        });
        //取消默认行为
        return false;
    });
});
