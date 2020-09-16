package com.msyd.payrouter.bean;

import java.util.Date;

/**
 * 接收宝易互通各种后端交易返回的处理结果，将返回的参数用该bean进行接收，封装
 * @author 杨龙飞
 *	
 */
public class MsgInfo extends BaseMsgInfo {

    private String srvType;//

    private String timeOut;//交易超时返回信息

    private String tranState;//交易状态

    private String phoneToken;//手机令牌

    private String phoneVerCode;//手机验证码

    private String tranRespCode;//交易响应吗

    private String tranRespMsg;//交易响应吗说明

    private String totalNum;//总记录数(必输)

    private String hashCode;//内容摘要(非必输)

    private String data;//结果内容(非必输)

    private String queryTime;//查询时间(必输)

    private String acctNo;//虚拟账号(必输)

    private String balance;//账户余额(必输)

    private String merOrderId;//订单号(必输)

    private String custId;//客户号(必输)

    private String accountName;//账户名称

    private String accountNo;//账户号

    private String bankName;//开户行

    private String openBankUnionNo;//行号

    private String accountType;//账户类型

    private String allNum;//总笔数

    private String oid_partner;//商户号

    private String sign_type;//签名方式

    private String sign;//签名

    private String user_id;//商户用户唯一标志

    private String busi_partner;//商户业务类型

    private String no_order;//商户唯一订单号

    private String dt_order;//商户订单时间

    private String money_order;//商户订单金额

    private String name_goods;//商品名称

    private String notify_url;//异步通知地址

    private String bank_code;//银行编号

    private String valid_order;//订单有效时间

    String channel_abbr;//渠道

    String riskItem;// 风控参数

    private String channelName;

    private String channelCode;

    //卡bin
    private String bankType;

    private String cardAttr;

    private String bankCode;

    private String netBankUnionCode;

    private String protocolNo;//签约协议号

    //private String channelCode;//渠道名

    private String channelNo;//渠道号

    private String channelOrderId;//渠道流水号

    private Date tradeTime;//交易时间

    private String hasNextPage;

    private Integer pageNo;

    private Integer pageSize;

    public String getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(String hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannel_abbr() {
        return channel_abbr;
    }

    public void setChannel_abbr(String channel_abbr) {
        this.channel_abbr = channel_abbr;
    }

    public String getRiskItem() {
        return riskItem;
    }

    public void setRiskItem(String riskItem) {
        this.riskItem = riskItem;
    }

    public String getValid_order() {
        return valid_order;
    }

    public void setValid_order(String valid_order) {
        this.valid_order = valid_order;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getOid_partner() {
        return oid_partner;
    }

    public void setOid_partner(String oid_partner) {
        this.oid_partner = oid_partner;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBusi_partner() {
        return busi_partner;
    }

    public void setBusi_partner(String busi_partner) {
        this.busi_partner = busi_partner;
    }

    public String getNo_order() {
        return no_order;
    }

    public void setNo_order(String no_order) {
        this.no_order = no_order;
    }

    public String getDt_order() {
        return dt_order;
    }

    public void setDt_order(String dt_order) {
        this.dt_order = dt_order;
    }

    public String getMoney_order() {
        return money_order;
    }

    public void setMoney_order(String money_order) {
        this.money_order = money_order;
    }

    public String getName_goods() {
        return name_goods;
    }

    public void setName_goods(String name_goods) {
        this.name_goods = name_goods;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOpenBankUnionNo() {
        return openBankUnionNo;
    }

    public void setOpenBankUnionNo(String openBankUnionNo) {
        this.openBankUnionNo = openBankUnionNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }

    public String getSrvType() {
        return srvType;
    }

    public void setSrvType(String srvType) {
        this.srvType = srvType;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getPhoneToken() {
        return phoneToken;
    }

    public void setPhoneToken(String phoneToken) {
        this.phoneToken = phoneToken;
    }

    public String getPhoneVerCode() {
        return phoneVerCode;
    }

    public void setPhoneVerCode(String phoneVerCode) {
        this.phoneVerCode = phoneVerCode;
    }

    public String getTranRespCode() {
        return tranRespCode;
    }

    public void setTranRespCode(String tranRespCode) {
        this.tranRespCode = tranRespCode;
    }

    public String getTranRespMsg() {
        return tranRespMsg;
    }

    public void setTranRespMsg(String tranRespMsg) {
        this.tranRespMsg = tranRespMsg;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getMerOrderId() {
        return merOrderId;
    }

    public void setMerOrderId(String merOrderId) {
        this.merOrderId = merOrderId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getAllNum() {
        return allNum;
    }

    public void setAllNum(String allNum) {
        this.allNum = allNum;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getNetBankUnionCode() {
        return netBankUnionCode;
    }

    public void setNetBankUnionCode(String netBankUnionCode) {
        this.netBankUnionCode = netBankUnionCode;
    }

    public String getCardAttr() {
        return cardAttr;
    }

    public void setCardAttr(String cardAttr) {
        this.cardAttr = cardAttr;
    }

    public String getProtocolNo() {
        return protocolNo;
    }

    public void setProtocolNo(String protocolNo) {
        this.protocolNo = protocolNo;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getChannelOrderId() {
        return channelOrderId;
    }

    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }
}
