<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019-01-06
  Time: 16:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>我的桌面</title>
    <jsp:include page="../../common/static.jsp"/>
</head>
<body>
<article class="cl pd-20">
    <form:form action="" method="post" commandName="model" class="form form-horizontal" id="form-admin-add">
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>商品类型：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <span class="select-box" style="width:150px;">
				<form:select id="petType" path="petType" class="select">
                    <form:option value="0" label="猫"></form:option>
                    <form:option value="1" label="狗"></form:option>
                </form:select>
				</span>
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>商品名称：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${model.shopName}" placeholder="商品名称" id="shopName" name="shopName">
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>商品金额：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="text" class="input-text" value="${model.moneys}" placeholder="商品金额" id="moneys" name="moneys">
            </div>
        </div>
        <div class="row cl">
            <label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>商品图片：</label>
            <div class="formControls col-xs-8 col-sm-9">
                <input type="hidden" class="input-text" value="${model.image}" placeholder="" id="image" name="image">
                <input type="file" class="" id="btnFile" name="file" />
                <img src="${model.image}" id="imagePrev" style="width: 100px;height: 100px;">
            </div>
        </div>
        <div class="row cl">
            <div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
                <input class="btn btn-primary radius" id="submit" type="button" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
            </div>
        </div>
    </form:form>
</article>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript">
    $(function(){
        //上传图片
        $("#btnFile").change(function() {
            var formData = new FormData();
            formData.append("file", $("#btnFile")[0].files[0]);
            $.ajax({
                url: "/base/upload",
                type: "post",
                data: formData,
                processData: false, // 告诉jQuery不要去处理发送的数据
                contentType: false, // 告诉jQuery不要去设置Content-Type请求头
                dataType: 'text',
                success: function(data) {
                    var params = JSON.parse(data);
                    if(params.State == 0){
                        $("#imagePrev").attr("src", params.Content);
                        $("#image").val(params.Content);
                    }else {
                        layer.msg(params.Content);
                    }
                },
                error: function(data) {

                }
            });
        });

        //提交数据
        $('#submit').click(function () {
            var id =  $.getUrlParam('id');
            if(!id){
                id = -1;
            }
            var data = {};
            data.petType = $('#petType').val();
            data.shopName = $('#shopName').val();
            data.moneys = $('#moneys').val();
            data.image = $('#image').val();
            data.petShopId = id;
            var load = layer.load(1);
            $.ajax({
                url: "/admin/home/shop/saveShop",
                data: data,
                type: "post",
                dataType: "json",
                success: function (data) {
                    if(data.State == 0){
                        alertS(data.Content);
                    }else{
                        alertE(data.Content);
                    }
                },
                complete:function () {
                    layer.close(load);
                }
            });
        });
    });
</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>
