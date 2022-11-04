package com.sys.modules;

public class MsgState {
    /// <summary>
    /// 成功 0
    /// </summary>
    public final static int Success = 0;
    /// <summary>
    /// 失败 1
    /// </summary>
    public final static int Fail= 1;
    /// <summary>
    /// 其他 2
    /// </summary>
    public final static int Other = 2;
    /// <summary>
    /// 未登录 3
    /// </summary>
    public final static int NoLogin = 3;
    /// <summary>
    /// 没有权限 4
    /// </summary>
    public final static int NoAuth = 4;
    /// <summary>
    /// 登录失败
    /// </summary>
    public final static int LoginError = 5;
    ///校验失败
    public final static int CheckFail = 6;
    ///缺少参数
    public final static int NoneParas = 7;
}
