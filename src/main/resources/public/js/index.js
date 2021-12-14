layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
//    监听提交
    form.on('submit(login)',function (data){
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
            url:ctx+"/user/login",
            data:{"userName":fieldData.username,"userPwd":fieldData.password},
            dataType:"json",
            success:function (msg){
            //    resultInfo
                if (msg.code==200){
                    //成功的提示
                    // layer.msg("登录成功了",{icon:5},function (){
                    //     window.location.href=ctx+"/main";
                    layer.msg("登录成功了",function (){
                    //    将用户的数据存储到Cookie
                        $.cookie("userIdStr",msg.result.userIdStr);
                        $.cookie("userName",msg.result.userName);
                        $.cookie("trueName",msg.result.trueName);
                    //    判断是否选中记住密码
                       if($("input[type='checkbox']").is(":checked")){
                            //{expires:数字}失效时间
                            $.cookie("userIdStr",msg.result.userIdStr,{expires:7});
                            $.cookie("userName",msg.result.userName,{expires:7});
                            $.cookie("trueName",msg.result.trueName,{expires:7});
                        }
                    //跳转页面
                        window.location.href=ctx+"/main";
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