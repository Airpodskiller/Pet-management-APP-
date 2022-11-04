package com.example.petclient.utils.http;

/**
 * API字典
 */
public class ApiConfig {
    /**
     *登录
     */
    public final static String API_LOGIN = "/api/index/login";
    /**
     *注册
     */
    public final static String API_SIGNIN = "/api/index/sign";
    /**
     *查询我的宠物
     */
    public final static String API_PET_LIST = "/api/home/petList";

    /**
     * 宠物信息
     */
    public final static String API_PET_INFO = "/api/home/petInfo";
    /**
     * 添加宠物
     */
    public final static String API_ADD_PET = "/api/home/addPet";
    /**
     * 删除宠物
     */
    public final static String API_DELETE_PET = "/api/home/deletePet";
    /**
     * 发起一条宠物体检
     */
    public final static String API_ADD_PET_TEST = "/api/home/addPetTest";
    /**
     * 消息轮询
     */
    public final static String API_NOTIFY_MESSAGE = "/api/home/notifyMessage";
    /**
     * 获取我的治疗
     */
    public final static String API_PET_ILL_LIST = "/api/home/petIllList";
    /**
     * 获取我的体检
     */
    public final static String API_PET_TEST_LIST = "/api/home/petTestList";
    /**
     * 消息列表
     */
    public final static String API_NOTIFY_LIST = "/api/home/notifyList";

    //新增的接口
    /**
     * 获取商城的商品
     */
    public final static String API_SHOP_LIST = "/api/home/shop/shopList";

    /**
     * 购买商品
     */
    public final static String API_BUY_SHOP = "/api/home/shop/createOrder";

    public final static String API_SHOP_ORDER_LIST = "/api/home/shop/orderList";
    /**
     * 充值余额
     */
    public final static String API_RECHARGE_ORDER_LIST = "/api/home/account/rechargeList";

    /**
     * 充值接口
     */
    public final static String API_RECHARGE = "/api/home/account/recharge";

    /**
     * 个人信息接口
     */
    public final static String API_MIME_INFO = "/api/home/account/info";

    /**
     * 更新信息
     */
    public final static String API_MIME_UPDATE = "/api/home/account/update";

    /**
     * 更新密码
     */
    public final static String API_MIME_UPDATE_PASSWORD = "/api/home/account/updatePassword";
    /**
     * 查询疫苗记录
     */
    public final static String API_PET_VACCINE_LOG_LIST = "/api/home/petVaccineLog";
    /**
     * 宠物洗澡记录
     */
    public final static String API_PET_SHOWER_LIST = "/api/home/petShowerList";

    /**
     * 宠物的绝育记录
     */
    public final static String API_PET_STERILIZATION_LIST = "/api/home/petSterilizationList";
    /**
     * 查询绝育配置
     */
    public final static String API_STERILIZATION_CONFIG = "/api/home/petSterilizationConfig";

    /**
     * 创建绝育的订单
     */
    public final static String API_ADD_STERILIZATION = "/api/home/addSterilization";
    /**
     * 洗澡的项目
     */
    public final static String API_SHOWER_TYPE_LIST = "/api/index/showerTypeList";
    /**
     * 添加洗澡的记录
     */
    public final static String API_ADD_SHOWER = "/api/home/addShower";

    /**
     * 添加疫苗
     */
    public final static String API_ADD_VACCINE = "/api/home/addVaccine";

    /**
     * 添加治疗
     */
    public final static String API_ADD_ILL = "/api/home/addPetIll";

    /**
     * 取消治疗
     */
    public final static String API_CANCEL_ILL = "/api/home/cancelPetIll";

    public final static String API_PET_SHOWER_BASE_MONEYS = "/api/home/showerTimeConfig";
}
