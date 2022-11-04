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
    <jsp:include page="../../common/static.jsp"/>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 系统管理 <span class="c-gray en">&gt;</span> <a class="btn btn-success radius r" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="page-container">
    <div class="text-c">
        <input type="text" class="input-text" style="width:250px" placeholder="输入用户真实姓名搜索" id="userName">
        <input type="text" class="input-text" style="width:250px" placeholder="输入订单号搜索" id="orderCode">
        <button type="submit" class="btn btn-success radius" id="search" name=""><i class="Hui-iconfont">&#xe665;</i> 搜记录</button>
    </div>
    <div class="mt-20">
        <table class="table table-border table-bordered table-bg table-sort">
            <thead>
            <tr class="text-c">
                <th width="70">用户登录名</th>
                <th width="80">用户真实姓名</th>
                <th width="100">订单编号</th>
                <th width="100">商品名称</th>
                <th width="100">下单时间</th>
                <th width="100">付费金额</th>
                <th width="100">状态</th>
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
            url: "/admin/home/shop/orderList",
            type: "post",
            data: function (data) {
                data.userName = $('#userName').val();
                data.orderCode = $('#orderCode').val();
            }
        },
        "retrieve": true,
        "columns": [
            { "data": "account" },
            { "data": "userName" },
            { "data": "orderCode" },
            { "data": "shopName" },
            { "data": "createTime" },
            { "data": "moneys" },
            { "data": "status" , "render": function (data, type, full, meta) {
                    if(data == 0){
                        return "未付款";
                    }else if(data == 1){
                        return "已付款";
                    }else {
                        return "已发货";
                    }
                }},
            {
                "data": "petOrderId", "render": function (data, type, full, meta) {
                    var opt = "";
                    if(full.status == 1){
                        opt += "<a title='发货' href='javascript:;' onclick=\"confirmOrder("+data+")\" class='ml-5' style='text-decoration:none'>发货</a>";
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

    function confirmOrder(id) {
        layer.confirm('确认确认发货？',function(index){
            $.ajax({
                type: 'POST',
                url: '/admin/home/shop/confirmOrder',
                data:{
                    petOrderId:id
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
