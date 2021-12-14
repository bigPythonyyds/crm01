layui.use(['form', 'layer'], function () {
    var form = layui.form, layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    //监听表单事件
    form.on("submit(addOrUpdateSaleChance)", function (obj) {
        console.log(obj.field + "<<")
        //加载层特效
        var index = layer.msg("数据正在提交中，请稍等", {
            icon: 16,
            time: false,
            shade: 0.8
        })
        //判断是添加还是修改，id==null，添加。id！=null  修改
        var url = ctx + "/sale_chance/update";
        //判断当前页面的隐藏域有没有id，有id做修改，否则添加操作
        if ($("input[name=id]").val()) {
            url = ctx + "/sale_chance/update"
        }
        $.ajax({
            type: "post",
            url: url,
            data: obj.field,
            dataType: "json",
            success: function (obj) {
                if (obj.code == 200) {
                    //提示一下
                    layer.msg("添加ok", {icon: 5});
                    //    关闭加载层
                    layer.close(index);
                    //    关闭iframe
                    layer.closeAll("iframe");
                    //    刷新页面
                    window.parent.location.reload();
                } else {
                    layer.msg(obj.msg, {icon: 6})
                }
            }

        });

        //    取消跳转
        return false;

    });

    //取消功能
    $("#closeBtn").click(function (){
        console.log("起飞");
        //获取当前弹出层的索引
        var idx = parent.layer.getFrameIndex(window.name);
        //根据索引关闭
        parent.layer.close(idx);
    });

    //添加下拉框
    var assignMan = $("inpit[name='man']").val();
    $.ajax({
        type: "post",
        url: ctx+"/user/sales",
        dataType: "json",
        success:function (data){
        //    遍历
            for(var x in data) {
                if (data[x].id==assignMan){
                    $('#assignMan').append(" <option selected value= '"+data[x].id+"'>"+data[x].uname+"</option>")
                }else {
                    $('#assignMan').append(" <option value= '"+data[x].id+"'>"+data[x].uname+"</option>")
                }
            }
        //    重新渲染
            layui.form.render("select");
        }
        });
    // /** * 监听submit事件
    //  *  * 实现营销机会的添加与更新
    //  *  */
    // form.on("submit(addOrUpdateSaleChance)", function (data) {
    //     // 提交数据时的加载层（https://layer.layui.com/）
    //     var index = layer.msg("数据提交中,请稍后...",
    //         {icon:16,
    //             // 图标
    //             time:false,
    //             // 不关闭
    //             shade:0.8
    //             // 设置遮罩的透明度
    //             //
    //             });
    //     // 请求的地址
    //     var url = ctx + "/sale_chance/save";
    //     // 发送ajax请求
    //     $.ajax({
    //         type:"post",
    //         url:url,
    //         data:data.field,
    //         dataType:"json",
    //         success:function (msg) {
    //             // 操作成功
    //             if (msg.code == 200) {
    //                 // 提示成功
    //                 layer.msg("操作成功！");
    //                 // 关闭加载层
    //                 layer.close(index);
    //                 // 关闭弹出层
    //                 layer.closeAll("iframe");
    //                 // 刷新父页面，重新渲染表格数据
    //                 parent.location.reload();
    //             } else {
    //                 layer.msg(msg.msg);
    //             }
    //         }
    //     })
    //
    //
    //     // $.post(url, data.field,
    //     //     function (result)
    //     //     {
    //     //         // 操作成功
    //     //         if (result.code == 200) {
    //     //             // 提示成功
    //     //             layer.msg("操作成功！");
    //     //             // 关闭加载层
    //     //             layer.close(index);
    //     //             // 关闭弹出层
    //     //             layer.closeAll("iframe");
    //     //             // 刷新父页面，重新渲染表格数据
    //     //             parent.location.reload();
    //     //         } else {
    //     //             layer.msg(result.msg);
    //     //         }
    //     //     });
    //     return false;
    //     // 阻止表单提交
    //     });
});