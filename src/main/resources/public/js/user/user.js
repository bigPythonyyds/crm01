layui.use(['table','layer'],function() {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 用户列表展示
     */
    var tableIns = table.render({
        elem: '#userList',
        url: ctx + '/user/list',
        cellMinWidth: 95,
        page: true,
        height: "full-125",
        limits: [10, 15, 20, 25],
        limit: 10,
        toolbar: "#toolbarDemo",
        id: "userListTable",
        cols: [[
            {type: "checkbox", fixed: "left", width: 50},
            {field: "id", title: '编号', fixed: "true", width: 80},
            {field: 'userName', title: '用户名', minWidth: 50, align: "center"},
            {field: 'email', title: '用户邮箱', minWidth: 100, align: 'center'},
            {field: 'phone', title: '用户电话', minWidth: 100, align: 'center'},
            {field: 'trueName', title: '真实姓名', align: 'center'},
            {field: 'createDate', title: '创建时间', align: 'center', minWidth: 150},
            {field: 'updateDate', title: '更新时间', align: 'center', minWidth: 150},
            {
                title: '操作', minWidth: 150,
                templet: '#userListBar', fixed: "right", align: "center"
            }
        ]]
    });
    // 实现搜索功能，页面重载
    $(".search_btn").click(function () {
        //    这里以搜索为例
        //固定不变，精确查找上面变量进行重载
        tableIns.reload({
            //设定异步数据接口的额外参数，任意设置
            where: {
                userName: $("input[name=userName]").val(),
                phone: $("input[name=phone]").val(),
                email: $("#input[name=email]").val()
            }
            , page: {
                //数字，代表从第几页开始
                curr: 1
            }
        });
    });
    /**
     * 头部工具栏事件
     */
    table.on("toolbar(users)", function (obj) {
        var checkStatus = table.checkStatus(obj.config.id);
        console.log(checkStatus.data)
        switch (obj.event) {
            case "add":
                openAddOrUpdateUserDialog();
                break;
            case 'del':
                // layer.msg("dels");
                deleterUser(checkStatus.data);
                break;
        }
        ;
    });

    function deleterUser(datas) {
        if (datas.length == 0) {
            layer.msg("请选择要删除的数据");
            return;
        }
        layer.confirm("你确定要删除吗?", {
            btn: ["确认", "取消"]
        }, function (index) {
            //    关闭
            layer.close(index);
            //    收集数据
            var ids = "&ids=";
            for (var i = 0; i < datas.length; i++) {
                if (i < datas.length - 1) {
                    ids = ids + datas[i].id + "&ids="
                } else {
                    ids = ids + datas[i].id;
                }
            }
            console.log(ids);
            //    发送ajax删除数据
            $.post(ctx + "/user/delete", ids, function (result) {
                if (result.code == 200) {
                    //    重新加载数据
                    tableIns.reload();
                } else {
                    //    提示一下
                    layer.msg(result.msg, {icon: 5});
                }
            }, "json");

        });
    }

    /**
     * 打开用户添加或更新对话框
     */
    function openAddOrUpdateUserDialog(userId) {
        var url = ctx + "/user/addOrUpdateUserPage";
        var title = "用户管理-用户添加";
        if (userId) {
            url = url + "?id=" + userId;
            title = "用户管理-用户更新";
        }
        layui.layer.open({
            title: title,
            type: 2,
            area: ["650px", "400px"],
            maxmin: true,
            content: url
        });
    }

    /**
     * 行监听事件
     */
    table.on("tool(users)", function (obj) {
        var layEvent = obj.event;
// 监听编辑事件
        if (layEvent === "edit") {
            openAddOrUpdateUserDialog(obj.data.id);
        } else if (layEvent === "del") {
// 监听删除事件
            layer.confirm('确定删除当前用户？', {icon: 3, title: "用户管理"}, function
                (index) {
                $.post(ctx + "/user/delete", {ids: obj.data.id}, function (data) {
                    if (data.code == 200) {
                        layer.msg("操作成功！");
                        tableIns.reload() ;
                    } else {
                        layer.msg(data.msg, {icon: 5});
                    }
                });
            });
        }
    });
});