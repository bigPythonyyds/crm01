layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
//    监听提交
    form.on('submit(saveBtn)',function (data){
        //layer.msg(JSON.stringify(data.field));
        // data.field.username;
        // data.field.password;
        //layer.msg(data.field.username);
        var fieldData = data.field;
        if (fieldData.username=='undefined' || fieldData.username=='') {
            layer.msg("用户名不能为空");
            return;
        }
        if (fieldData.password=='undefined' || fieldData.password=='') {
            layer.msg("密码不能为空");
            return;
        }
        //发送ajax
        $.ajax({
            type:"post",
            url:ctx+"/user/setting",
            data:{userName:data.field.userName,
                phone:data.field.phone,
                email:data.field.email,
                trueName:data.field.trueName,
                id:data.field.id
                },
            dataType:"json",
            success:function (msg){
                if (msg.code==200){
                    layer.msg("保持成功了",function (){
                    //请空Cookie
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                    //跳转页面
                        window.parent.location.href=ctx+"/index";
                    });
                }else {
                //    失败的提示
                    layer.msg(msg.msg);
                }
            }
        });

        //取消默认提交到导航栏行为
        return false;
    });
    
});