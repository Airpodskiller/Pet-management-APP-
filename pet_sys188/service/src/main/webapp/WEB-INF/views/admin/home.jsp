<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018-12-11
  Time: 23:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理系统</title>
    <jsp:include page="../common/static.jsp"/>
</head>
<body>
<header class="navbar-wrapper">
    <div class="navbar navbar-fixed-top">
        <div class="container-fluid cl"> <a class="logo navbar-logo f-l mr-10 hidden-xs" href="/">管理系统</a> <a class="logo navbar-logo-m f-l mr-10 visible-xs" href="/aboutHui.shtml">H-ui</a>
            <span class="logo navbar-slogan f-l mr-10 hidden-xs">v1.0</span>
            <a aria-hidden="false" class="nav-toggle Hui-iconfont visible-xs" href="javascript:;">&#xe667;</a>
            <nav id="Hui-userbar" class="nav navbar-nav navbar-userbar hidden-xs">
                <ul class="cl">
                    <li>${name}</li>
                    <li class="dropDown dropDown_hover">
                        <a href="#" class="dropDown_A">${account} <i class="Hui-iconfont">&#xe6d5;</i></a>
                        <ul class="dropDown-menu menu radius box-shadow">
                            <li><a href="/admin/loginOut">退出</a></li>
                        </ul>
                    </li>
                    <li id="Hui-skin" class="dropDown right dropDown_hover"> <a href="javascript:;" class="dropDown_A" title="换肤"><i class="Hui-iconfont" style="font-size:18px">&#xe62a;</i></a>
                        <ul class="dropDown-menu menu radius box-shadow">
                            <li><a href="javascript:;" data-val="default" title="默认（黑色）">默认（黑色）</a></li>
                            <li><a href="javascript:;" data-val="blue" title="蓝色">蓝色</a></li>
                            <li><a href="javascript:;" data-val="green" title="绿色">绿色</a></li>
                            <li><a href="javascript:;" data-val="red" title="红色">红色</a></li>
                            <li><a href="javascript:;" data-val="yellow" title="黄色">黄色</a></li>
                            <li><a href="javascript:;" data-val="orange" title="橙色">橙色</a></li>
                        </ul>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</header>
<aside class="Hui-aside">
    <div class="menu_dropdown bk_2">
        <dl id="menu-menu_1">
            <dt><i class="Hui-iconfont">&#xe62d;</i> 会员管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a data-href="/admin/home/member" data-title="会员信息" href="javascript:void(0)">会员信息</a></li>
                </ul>
            </dd>
        </dl>
        <dl id="menu-menu_2">
            <dt><i class="Hui-iconfont">&#xe62d;</i> 宠物管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a data-href="/admin/home/pet" data-title="宠物信息" href="javascript:void(0)">宠物信息</a></li>
                    <li><a data-href="/admin/home/petTest" data-title="宠物体检信息" href="javascript:void(0)">宠物体检信息</a></li>
                    <li><a data-href="/admin/home/petIll" data-title="宠物治疗信息" href="javascript:void(0)">宠物治疗信息</a></li>
                    <li><a data-href="/admin/home/petShower" data-title="宠物洗澡信息" href="javascript:void(0)">宠物洗澡信息</a></li>
                    <li><a data-href="/admin/home/petSterilization" data-title="宠物绝育信息" href="javascript:void(0)">宠物绝育信息</a></li>
                    <li><a data-href="/admin/home/petVaccine" data-title="宠物疫苗信息" href="javascript:void(0)">宠物疫苗信息</a></li>
                </ul>
            </dd>
        </dl>
        <dl id="menu-menu_3">
            <dt><i class="Hui-iconfont">&#xe62d;</i> 商城管理<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
            <dd>
                <ul>
                    <li><a data-href="/admin/home/shop/shop" data-title="商城商品" href="javascript:void(0)">商城商品</a></li>
                    <li><a data-href="/admin/home/shop/order" data-title="商城订单" href="javascript:void(0)">商城订单</a></li>
                </ul>
            </dd>
        </dl>
    </div>
</aside>
<div class="dislpayArrow hidden-xs"><a class="pngfix" href="javascript:void(0);" onClick="displaynavbar(this)"></a></div>
<section class="Hui-article-box">
    <div id="Hui-tabNav" class="Hui-tabNav hidden-xs">
        <div class="Hui-tabNav-wp">
            <ul id="min_title_list" class="acrossTab cl">
                <li class="active">
                    <span title='宠物信息' data-href="/admin/home/pet">宠物信息</span>
                    <em></em></li>
            </ul>
        </div>
        <div class="Hui-tabNav-more btn-group"><a id="js-tabNav-prev" class="btn radius btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d4;</i></a><a id="js-tabNav-next" class="btn radius btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d7;</i></a></div>
    </div>
    <div id="iframe_box" class="Hui-article">
        <div class="show_iframe">
            <div style="display:none" class="loading"></div>
            <iframe scrolling="yes" frameborder="0" src="/admin/home/pet"></iframe>
        </div>
    </div>
</section>

<div class="contextMenu" id="Huiadminmenu">
    <ul>
        <li id="closethis">关闭当前 </li>
        <li id="closeall">关闭全部 </li>
    </ul>
</div>

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/module/jquery.contextmenu/jquery.contextmenu.r2.js"></script>
</body>
</html>
