package com.msyd.payrouter.bean;

public class MsgInfoNode {
    /**报文参数，报文体*/
    public static final String MSG_FIELD_XML = "xml";

    /**报文参数，签名*/
    public static final String MSG_FIELD_MAC = "mac";

    public static final String MSG_HEAD_VERSION_VALUE = "1.0.0";//版本号

    public static final String MSG_HEAD_MSG_TYPE_REQ_VALUE = "0001";//请求报文类型

    public static final String MSG_HEAD_MSG_TYPE_RSP_VALUE = "0002";//响应报文类型

    /**报文头字段名称-版本*/
    public static final String MSG_HEAD_FILED_VERSION = "version";

    public static final String MSG_HEAD_FILED_MSGTYPE = "msgtype";

    public static final String MSG_HEAD_FILED_CHANID = "channelno";

    public static final String MSG_HEAD_FILED_MERCHANTNO = "merchantno";

    public static final String MSG_HEAD_FILED_TRANDATE = "trandate";

    public static final String MSG_HEAD_FILED_TRANTIME = "trantime";

    public static final String MSG_HEAD_FILED_TIMEOUT = "timeout";

    public static final String MSG_HEAD_FILED_TRANFLOW = "bussflowno";

    public static final String MSG_HEAD_FILED_TRANCODE = "trancode";

    public static final String MSG_HEAD_FILED_RESPCODE = "respcode";

    public static final String MSG_HEAD_FILED_RESPMSG = "respmsg";

    public static final String MSG_HEAD_FILED_SRVTYPE = "srvtype";

    public static final String MSG_BODY_FILED_TRANSTATE = "tranState";

    public static final String MSG_BODY_FILED_BANKNAME = "bankName";

    public static final String MSG_BODY_FILED_OPENBANKUNIONNO = "openBankUnionNo";

    public static final String MSG_BODY_FILED_ACCOUNTTYPE = "accountType";

    public static final String MSG_BODY_FILED_PHONETOKEN = "phoneToken";

    public static final String MSG_BODY_FILED_PHONEVERCODE = "phoneVerCode";

    public static final String MSG_BODY_FILED_TRANRESPCODE = "tranRespCode";

    public static final String MSG_BODY_FILED_TRANRESPMSG = "tranRespMsg";

    public static final String MSG_BODY_FILED_TOTALNUM = "totalNum";

    public static final String MSG_BODY_FILED_HASHCODE = "hashCode";

    public static final String MSG_BODY_FILED_DATA = "data";

    public static final String MSG_BODY_FILED_QUERYTIME = "queryTime";

    public static final String MSG_BODY_FILED_ACCTNO = "acctNo";

    public static final String MSG_BODY_FILED_BALANCE = "balance";

    public static final String MSG_BODY_FILED_MERORDERID = "merOrderId";

    public static final String MSG_BODY_FILED_CUSTID = "custId";

    public static final String MSG_BODY_FILED_ORGTRANFLOW = "orgTranFlow";

    public static final String MSG_BODY_FILED_ACCOUNTNO = "accountNo";

    public static final String MSG_BODY_FILED_ACCOUNTNAME = "accountName";

    public static final String MSG_BODY_FILED_TRANAMT = "tranAmt";

    public static final String MSG_BODY_FILED_ALLNUM = "allNum";

    /**连连支付添加节点
     * */
    public static final String MSG_BODY_FILED_OID_PARTNER = "oid_partner";//商户号

    public static final String MSG_BODY_FILED_SIGN_TYPE = "sign_type";//签名方式

    public static final String MSG_BODY_FILED_SIGN = "sign";//签名

    public static final String MSG_BODY_FILED_USER_ID = "user_id";//商户用户唯一标志

    public static final String MSG_BODY_FILED_BUSI_PARTNER = "busi_partner";//商户业务类型

    public static final String MSG_BODY_FILED_NO_ORDER = "no_order";//商户唯一订单号

    public static final String MSG_BODY_FILED_DT_ORDER = "dt_order";///商户订单时间

    public static final String MSG_BODY_FILED_MONEY_ORDER = "money_order";///商户订单金额

    public static final String MSG_BODY_FILED_NAME_GOODS = "name_goods";///商品名称

    public static final String MSG_BODY_FILED_NOTIFY_URL = "notify_url";//异步通知地址

    public static final String MSG_BODY_FILED_BANK_CODE = "bank_code";//银行编号

    public static final String MSG_BODY_FILED_VALID_ORDER = "valid_order";//时间有效时间段

    public static final String MSG_BODY_FILED_RISK_ITEM = "risk_item";//风控参数

    public static final String MSG_BODY_FILED_CHANNEL_ABBR = "channel_abbr";//渠道

    /**
     * 四要素支付路由返回字段
     * */
    public static final String MSG_BODY_FILED_CHANNELNAME = "channelName";//渠道名称

    public static final String MSG_BODY_FILED_CHANNELCODE = "channelCode";//渠道号

    /**报文XML路径，报文头*/
    public static final String MSG_XML_PATH_HEAD = "/message/head";

    /**报文XML路径，新版报文头*/
    public static final String MSG_XML_PATH_DATA_HEAD = "/message/data/head";

    /**报文XML路径，报文体*/
    public static final String MSG_XML_PATH_BODY = "/message/body";

    /**报文XML路径，新版报文体*/
    public static final String MSG_XML_PATH_DATA_BODY = "/message/data/body";

    /**
     * 卡bin接口返回字段
     */
    public static final String MSG_BODY_CARDBIN_BANKTYPE = "bankType";

    public static final String MSG_BODY_CARDBIN_CARDATTR = "cardAttr";

    public static final String MSG_BODY_CARDBIN_BANKCODE = "bankCode";

    public static final String MSG_BODY_CARDBIN_NETBANKUNIONCODE = "netBankUnionCode";

    /**
     * 短信认证CP0030接口新加签约协议号字段
     */
    public static final String MSG_BODY_PROTOCOLNO = "protocolNo";

    /**
     * 新加字段
     */
    public static final String MSG_BODY_CHANNELNO = "channelNo";

    public static final String MSG_BODY_CHANNELORDERID = "channelOrderId";

    public static final String MSG_BODY_TRADETIME = "tradeTime";

    public static final String HASNEXTPAGE = "hasNextPage";

    public static final String PAGENO = "pageNo";

    public static final String PAGESIZE = "pageSize";

}
