package com.sys.controller.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sys.common.HttpBaseRequest;
import com.sys.common.MySqlHelper;
import com.sys.common.RespData;
import com.sys.common.interceptor.api.ApiAnnotation;
import com.sys.common.interceptor.api.ApiContext;
import com.sys.common.interceptor.api.ApiLoginModel;
import com.sys.dao.SysMemberMapper;
import com.sys.dao.SysPetOrderMapper;
import com.sys.dao.SysPetShopMapper;
import com.sys.entity.SysMember;
import com.sys.entity.SysPetOrder;
import com.sys.entity.SysPetShop;
import com.sys.modules.JsonModel;
import com.sys.modules.ListGageModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商城的api
 */
@Controller
@RequestMapping("/api/home/shop")
@ApiAnnotation
public class ApiShopController {
    @Autowired
    private SysPetShopMapper sysPetShopMapper;
    @Autowired
    private SysPetOrderMapper sysPetOrderMapper;
    @Autowired
    private SysMemberMapper sysMemberMapper;
    //查询我的订单
    @RequestMapping("/createOrder")
    @ResponseBody
    public JsonModel createOrder(HttpServletRequest request){
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        SysMember sysMember = loginModel.sysMember;
        long shopId = baseRequest.getLong("shopId");
        if(shopId <= 0){
            return RespData.JsonError("请选择购买的产品");
        }
        SysPetShop sysPetShop = sysPetShopMapper.selectByPrimaryKey(shopId);
        if(sysPetShop == null){
            return RespData.JsonError("不存在该记录");
        }
        if(sysMember.getMoneys().doubleValue() < sysPetShop.getMoneys().doubleValue()){
            return RespData.JsonError("对不起，您的金额不足");
        }
        //判断金额够
        SysPetOrder sysPetOrder = new SysPetOrder();
        sysPetOrder.setCreateTime(new Date());
        sysPetOrder.setStatus(1);
        sysPetOrder.setMemberId(sysMember.getMemberId());
        sysPetOrder.setMoneys(sysPetShop.getMoneys());
        sysPetOrder.setOrderCode(new Date().getTime()+"");
        sysPetOrder.setPetShopId(sysPetShop.getPetShopId());
        int result = sysPetOrderMapper.insertSelective(sysPetOrder);
        if(result > 0){
            //扣除金额
            sysMember.setMoneys(new BigDecimal(sysMember.getMoneys().doubleValue() - sysPetOrder.getMoneys().doubleValue()));
            sysMemberMapper.updateByPrimaryKeySelective(sysMember);
            return RespData.JsonOK("下单成功");
        }else {
            return RespData.JsonError("下单失败");
        }
    }

    //获取我的订单
    @RequestMapping("/orderList")
    @ResponseBody
    public JsonModel orderList(HttpServletRequest request){
        ApiLoginModel loginModel = ApiContext.LOCAL.get();
        HttpBaseRequest baseRequest = new HttpBaseRequest(request);
        int pageIndex = baseRequest.getInt("pageIndex", 1);
        int pageSize = baseRequest.getInt("length", 10);
        //创建查询条件
        MySqlHelper mySqlHelper = new MySqlHelper();
        List<SysPetOrder> list = new ArrayList<>();
        try {
            String strWhere = " 1 = 1 ";
            strWhere += " and a.member_id = "+loginModel.sysMember.getMemberId()+" ";
            String strOrder = " a.pet_order_id desc";
            String strTable = " sys_pet_order a left join sys_member b on a.member_id = b.member_id";
            String strQuery = " a.pet_order_id as petOrderId, order_code as orderCode, a.pet_shop_id as petShopId, a.moneys as moneys, a.member_id as memberId, a.status as status, a.create_time as createTime, a.shop_name as shopName, " +
                    "b.account as account,b.user_name as userName ";

            ListGageModule dataModule = mySqlHelper.getListForPage(pageSize, pageIndex, strQuery, strWhere, strOrder, strTable);
            List<Map<String, Object>> listModule = dataModule.getList();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            String json = gson.toJson(listModule);
            list = gson.fromJson(json, new TypeToken<List<SysPetOrder>>() {
            }.getType());//   JSONObject.parseArray(JSON.toJSONString(list_tagmap), cms_keyword_itemDO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespData.JsonOK(list);
    }
}
