package com.msyd.payrouter.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author 杨龙飞
 *
 */
public class ThirdPayConstant {
    /**
     * 三方支付业务接口访问路径
     */
    public static String THIRD_PAY_PATH = "http://172.30.2.21:8920/msgProcess/acceptXmlReq.do";

    /**
     * 商户号
     */
    public static String MERCHANT_NO = "";

    /**
     * QP接口请求地址
     */
    public static String QP_URL = "";

    /**
     * QP接口请求是否加密
     */
    public static String QP_ENCREPT = "";
    
    /**
     * QP接口，回调地址
     */
    public static String QP_BACK_URL = "";

    /**
     * 代收业务秘钥
     */
    public static String MERCHANT_KEY_DAISHOU = "";

    /**
     * 代付业务秘钥
     */
    public static String MERCHANT_KEY_DAIFU = "";

    /**
     * 代收代付查询业务秘钥(CP0020-CP0025,CP0031)
     */
    public static String MERCHANT_KEY_QUERY = "";

    /**
     * 网关商户号
     */
    public static String GATEWAY_MERCHANT_NO = "";

    /*
     * 前置订单号
     */
    public static String PRE_MERCHANT_NO = "";

    /**
     * 网关查询业务秘钥
     */
    public static String GATEWAY_MERCHANT_KEY_QUERY = "";

    /**
     * 小贷商户号体系(支付路由)
     */
    public static Map<String, Map<String, String>> XD_MERCHANT_MAP = new HashMap<String, Map<String, String>>();

    /**
     * 小贷商户号体系_三方
     */
    public static Map<String, Map<String, String>> XD_MERCHANT_MAP_SF = new HashMap<String, Map<String, String>>();

    /**
     * 信息格式版本
     */
    public static String VERSION = "2.0.0";

    /**
     * 支付路由信息格式版本
     */
    public static String PAY_ROUTE_VERSION = "1.0.0";

    /**
     * 请求报文类型
     */
    public static String MSGTYPE_REQ = "0001";

    /**
     * 应答报文类型
     */
    public static String MSGTYPE_RESP = "0002";

    /**
     * 渠道代号
     */
    public static String CHANNEL_NO = "99";

    /**
     * 成功码
     */
    public static final String SUCCESS_CODE = "C000000000";

    /**
     * 未知码
     */
    public static final String UNKNOWN_CODE = "W000000000";

    /**
     * 证书需要引入的文件路径以及证书名称、私钥密码
     */
    public static String MERCHANT_CERT_PATH = "D:\\cert\\";

    public static String MERCHANT_CERT_FILENAME_CER = "yidai.cer";

    public static String MERCHANT_CERT_FILENAME_PFX = "yidai.pfx";

    public static String MERCHANT_CERT_PWD = "umbpay";

    /**
     * 支付路由商户号
     */
    public static String PAY_ROUTE_MERCHANT_NO = "";

    /**
     * 支付路由小贷商户号
     */
    public static String PAY_ROUTE_XD_MERCHANT_NO = "";

    /**
     * 支付路由代收业务秘钥
     */
    public static String PAY_ROUTE_MERCHANT_KEY_DAISHOU = "";
    
    /**
     * 支付路由小贷代收业务秘钥
     */
    public static String PAY_ROUTE_XD_MERCHANT_KEY_DAISHOU = "";
    

    /**
     * 支付路由代付业务秘钥
     */
    public static String PAY_ROUTE_MERCHANT_KEY_DAIFU = "";

    /**
     * 支付路由支付业务接口访问路径
     */
    public static String PAY_ROUTE_MERCHANT_URL = "";

    /**
     * 三方代收付接口版本开关：1：老接口  2：新接口（加密版）
     */
    public static String MERCHANT_SF_SWITCH = "";

    /**
     * 三方1.0非加密接口
     */
    public static String MERCHANT_SF_V1 = "1";

    /**
     * 三方2.0非加密接口
     */
    public static String MERCHANT_SF_V2 = "2";

    /*
     * 三方代收账号
     */
    public static String MERCHANT_DAISHOU_ACCOUNT_NO = "";

}
