<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019-01-06
  Time: 16:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的桌面</title>
    <jsp:include page="../common/static.jsp"/>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 系统管理 <span class="c-gray en">&gt;</span> <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="page-container">
    <div class="text-c">
        <input type="text" class="input-text" style="width:250px" placeholder="输入用户真实姓名搜索" id="userName">
        <input type="text" class="input-text" style="width:250px" placeholder="输入宠物昵称搜索" id="petName">
        <button type="submit" class="btn btn-success radius" id="search" name=""><i class="Hui-iconfont">&#xe665;</i> 搜记录</button>
    </div>
    <div class="mt-20">
        <table class="table table-border table-bordered table-bg table-sort">
            <thead>
            <tr class="text-c">
                <th width="70">用户登录名</th>
                <th width="80">用户真实姓名</th>
                <th width="100">宠物编号</th>
                <th width="100">宠物昵称</th>
                <th width="100">申请时间</th>
                <th width="100">洗澡项目</th>
                <th width="100">洗澡金额</th>
                <th width="100">状态</th>
                <th width="100">是否完成</th>
                <th width="100">操作</th>
            </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>
</div>

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/module/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script type="text/javascript">
    var table = $('.table-sort').dataTable({
        "aaSorting": [[1, "desc"]],//默认第几个排序
        "bStateSave": true,//状态保存
        "aoColumnDefs": [
            { "orderable": false, "aTargets": [0, 2, 3] }// 制定列不参与排序
        ],
        "serverSide": true,
        "bProcessing": true,
        "bPaginate": true,
        "bLengthChange": true,
        "ajax": {
            url: "/admin/home/petShowerList",
            type: "post",
            data: function (data) {
                data.userName = $('#userName').val();
                data.petName = $('#petName').val();
            }
        },
        "retrieve": true,
        "columns": [
            { "data": "account" },
            { "data": "userName" },
            { "data": "petId" },
            { "data": "petName" },
            { "data": "createTime" },
            { "data": "petShoweType" },
            { "data": "moneys" },
            { "data": "status" , "render": function (data, type, full, meta) {
                    if(data == 0){
                        return "未付款";
                    }else if(data == 1){
                        return "已付款";
                    }
                }},
            { "data": "isOver" , "render": function (data, type, full, meta) {
                    if(data == 0){
                        return "等待洗澡";
                    }else if(data == 1){
                        return "开始洗澡";
                    }else{
                        return "洗澡完成";
                    }
                }},
            {
                "data": "petShowerId", "render": function (data, type, full, meta) {
                    var opt = "";
                    if(full.isOver == 0){
                        opt += "<a title='开始洗澡' href='javascript:;' onclick=\"confirmShower("+data+")\" class='ml-5' style='text-decoration:none'>开始洗澡</a>";
                    }
                    return opt;
                }
            }
        ]
    });

    $('#search').click(function () {
        if (table) {
            table.fnDraw(false);
        }
    });

    function confirmShower(id) {
        layer.confirm('确认开始洗澡？',function(index){
            $.ajax({
                type: 'POST',
                url: '/admin/home/confirmShower',
                data:{
                    petShowerId:id
                },
                dataType: 'json',
                success: function(data){
                    if(data.State == 0){
                        alertS("操作成功");
                        table.fnDraw(false);
                    }else{
                        alertE("操作失败");
                    }
                },
                error:function(data) {
                    console.log(data.msg);
                },
            });
        });
    }
</script>
</body>
</html>
