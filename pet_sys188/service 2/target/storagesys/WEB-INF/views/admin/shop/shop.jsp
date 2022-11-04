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
        <input type="text" class="input-text" style="width:250px" placeholder="输入产品名称搜索" id="shopName">
        <button type="submit" class="btn btn-success radius" id="search" name=""><i class="Hui-iconfont">&#xe665;</i> 搜记录</button>
    </div>
    <div class="cl pd-5 bg-1 bk-gray mt-20"> <span class="l"><a href="javascript:;" onclick="editModel()" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加商品</a></span> </div>
    <div class="mt-20">
        <table class="table table-border table-bordered table-bg table-sort">
            <thead>
            <tr class="text-c">
                <th width="70">商品名称</th>
                <th width="80">宠物类型</th>
                <th width="100">价格</th>
                <th width="100">图片</th>
                <th width="100">创建时间</th>
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
            url: "/admin/home/shop/shopList",
            type: "post",
            data: function (data) {
                data.shopName = $('#shopName').val();
            }
        },
        "retrieve": true,
        "columns": [
            { "data": "shopName" },
            { "data": "petType"  , "render": function (data, type, full, meta) {
                    if(data == 0){
                        return "猫";
                    }else if(data == 1){
                        return "狗";
                    }
                }},
            { "data": "moneys" },
            { "data": "image"  , "render": function (data, type, full, meta) {
                    return "<img src='"+data+"' width='100' height='100' />";
                }},
            { "data": "createTime" },
            {
                "data": "petShopId", "render": function (data, type, full, meta) {
                    var opt = "";
                    opt += "<a title='编辑' href='javascript:;' onclick=\"editModel("+data+")\" class='ml-5' style='text-decoration:none'>编辑</a>";
                    opt += "<a title='删除' href='javascript:;' onclick=\"deleteModel("+data+")\" class='ml-5' style='text-decoration:none'>删除</a>";
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

    function editModel(id) {
        layer_show(id ? '编辑商品' : '添加商品','/admin/home/shop/edit?id='+(id || -1),'','',function () {
            table.fnDraw(false);
        });
    }

    function deleteModel(id) {
        layer.confirm('确认删除该记录？',function(index){
            $.ajax({
                type: 'POST',
                url: '/admin/home/shop/delete',
                data:{
                    pet_shop_id:id
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
