package com.sys.controller.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sys.common.HttpBaseRequest;
import com.sys.common.MySqlHelper;
import com.sys.common.RespData;
import com.sys.common.interceptor.admin.AdminAnnotation;
import com.sys.common.interceptor.admin.AdminLoginModel;
import com.sys.dao.SysNotifyMapper;
import com.sys.dao.SysPetOrderMapper;
import com.sys.dao.SysPetShopMapper;
import com.sys.entity.SysNotify;
import com.sys.entity.SysPet;
import com.sys.entity.SysPetOrder;
import com.sys.entity.SysPetShop;
import com.sys.modules.HuiPageModel;
import com.sys.modules.JsonModel;
import com.sys.modules.ListGageModule;
import com.sys.modules.WebUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/home/shop")
@AdminAnnotation()
public class ShopController {
    @Autowired
    private SysPetShopMapper sysPetShopMapper;
    @Autowired
    private SysPetOrderMapper sysPetOrderMapper;
    @Autowired
    private SysNotifyMapper sysNotifyMapper;
    //获取商品
    @RequestMapping("/shop")
    public ModelAndView shop(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/admin/shop/shop");
        return view;
    }

    @RequestMapping("/shopList")
    @ResponseBody
    public HuiPageModel<SysPetShop> shopList(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        HuiPageModel<SysPetShop> page = new HuiPageModel<>();
        String shopName = baseRequest.getString("shopName");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();

        try {
            String strWhere = " 1 = 1 ";
            String strOrder = " a.pet_shop_id desc";
            String strTable = " sys_pet_shop a";
            String strQuery = " a.pet_shop_id as petShopId, a.shop_name as shopName, a.pet_type as petType, a.moneys as moneys, a.image as image, a.create_time as createTime ";
            if (!shopName.equals(""))
                strWhere += " and a.shop_name like '%" + shopName + "%'";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            List<SysPetShop> list = gson.fromJson(json, new TypeToken<List<SysPetShop>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
            page.setRecordsFiltered(dataModule.getTotalcount());
            page.setRecordsTotal(dataModule.getTotalcount());
            page.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    //编辑商品
    @RequestMapping("/edit")
    public ModelAndView edit(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long pet_shop_id = baseRequest.getLong("id");
        ModelAndView view = new ModelAndView();
        if(pet_shop_id > 0){
            SysPetShop sysPetShop = sysPetShopMapper.selectByPrimaryKey(pet_shop_id);
            view.addObject("model",sysPetShop);
        }else {
            view.addObject("model",new SysPetShop());
        }
        view.setViewName("/admin/shop/edit");
        return view;
    }

    @RequestMapping("/saveShop")
    @ResponseBody
    public JsonModel saveShop(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petShopId = baseRequest.getLong("petShopId");
        String shopName = baseRequest.getString("shopName");
        int petType = baseRequest.getInt("petType",0);
        double moneys = baseRequest.getDouble("moneys",0);
        String image = baseRequest.getString("image");
        if(petShopId > 0){
            SysPetShop sysPetShop = sysPetShopMapper.selectByPrimaryKey(petShopId);
            if(sysPetShop == null){
                return RespData.JsonError("不存在该记录");
            }
            sysPetShop.setShopName(shopName);
            sysPetShop.setPetType(petType);
            sysPetShop.setMoneys(new BigDecimal(moneys));
            sysPetShop.setImage(image);
            int result = sysPetShopMapper.updateByPrimaryKeySelective(sysPetShop);
            if(result > 0){
                return RespData.JsonOK("保存成功");
            }else {
                return RespData.JsonError("保存失败");
            }
        }else {
            //新增
            SysPetShop sysPetShop = new SysPetShop();
            sysPetShop.setShopName(shopName);
            sysPetShop.setPetType(petType);
            sysPetShop.setMoneys(new BigDecimal(moneys));
            sysPetShop.setImage(image);
            int result = sysPetShopMapper.insertSelective(sysPetShop);
            if(result > 0){
                return RespData.JsonOK("保存成功");
            }else {
                return RespData.JsonError("保存失败");
            }
        }
    }

    //删除商品
    @RequestMapping("/delete")
    @ResponseBody
    public JsonModel delete(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long pet_shop_id = baseRequest.getLong("pet_shop_id");
        if(pet_shop_id <= 0){
            return RespData.JsonError("不存在该数据");
        }
        int result = sysPetShopMapper.deleteByPrimaryKey(pet_shop_id);
        if(result > 0){
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }

    //获取订单

    @RequestMapping("/order")
    public ModelAndView order(){
        ModelAndView view = new ModelAndView();
        view.setViewName("/admin/shop/order");
        return view;
    }

    @RequestMapping("/orderList")
    @ResponseBody
    public HuiPageModel<SysPetOrder> orderList(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        HuiPageModel<SysPetOrder> page = new HuiPageModel<>();
        String userName = baseRequest.getString("userName");
        String orderCode = baseRequest.getString("orderCode");
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();

        try {
            String strWhere = " 1 = 1 ";
            String strOrder = " a.pet_order_id desc";
            String strTable = " sys_pet_order a left join sys_member b on a.member_id = b.member_id";
            String strQuery = " a.pet_order_id as petOrderId, order_code as orderCode, a.pet_shop_id as petShopId, a.moneys as moneys, a.member_id as memberId, a.status as status, a.create_time as createTime, a.shop_name as shopName, " +
                    "b.account as account,b.user_name as userName ";
            if (!userName.equals(""))
                strWhere += " and b.user_name like '%" + userName + "%'";
            if (!orderCode.equals(""))
                strWhere += " and a.order_code like '%" + orderCode + "%'";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            List<SysPetOrder> list = gson.fromJson(json, new TypeToken<List<SysPetOrder>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
            page.setRecordsFiltered(dataModule.getTotalcount());
            page.setRecordsTotal(dataModule.getTotalcount());
            page.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page;
    }

    //发货
    @RequestMapping("/confirmOrder")
    @ResponseBody
    public JsonModel confirmOrder(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        long petOrderId = baseRequest.getInt("petOrderId", 0);
        if(petOrderId <= 0){
            return RespData.JsonError("请选择需要操作的数据");
        }
        SysPetOrder sysPetOrder = sysPetOrderMapper.selectByPrimaryKey(petOrderId);
        if(sysPetOrder == null){
            return RespData.JsonError("不存在该数据");
        }
        sysPetOrder.setStatus(2);
        sysPetOrder.setCreateTime(new Date());
        int res = sysPetOrderMapper.updateByPrimaryKeySelective(sysPetOrder);
        if(res > 0){
            SysNotify sysNotify = new SysNotify();
            sysNotify.setKeyId(sysPetOrder.getPetOrderId());
            sysNotify.setType(1);
            sysNotify.setTitle("宠物治疗通知");
            sysNotify.setStatus(0);
            sysNotify.setMemberId(sysPetOrder.getMemberId());
            sysNotify.setMessage("您的宠物【"+sysPetOrder.getShopName()+"】已完成治疗");
            sysNotifyMapper.insertSelective(sysNotify);
            return RespData.JsonOK("操作成功");
        }else {
            return RespData.JsonError("操作失败");
        }
    }
}
