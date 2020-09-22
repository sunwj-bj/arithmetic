package com.msyd.payrouter;

import cfca.util.Base64;
import cfca.x509.certificate.X509Cert;
import com.alibaba.fastjson.JSON;
import com.msyd.base.StringUtil;
import com.msyd.payrouter.bean.MsgInfo;
import com.msyd.payrouter.bean.MsgInfoNode;
import com.msyd.payrouter.bean.ThirdPayConstant;
import com.msyd.payrouter.bean.TrancodeType;
import com.msyd.payrouter.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Node;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sunwj
 */
public class PayRouterTest {

    private static final RsaP1Util rsa = new RsaP1Util();
    private static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    public static Logger log = LoggerFactory.getLogger(PayRouterTest.class);

    String cp0032xml = XML_HEADER + "<message>" + genHead("CP0032","CF4000038450") + genCP0032_MsgBody()+ "</message>";
    String cp0030xml = XML_HEADER + "<message>" + genHead("CP0030","CF4000038450") + genCP0030_MsgBody()+ "</message>";
    String cp0001xml = XML_HEADER + "<message>" + genHead("CP0001","CF4000038450") + genCP0001_MsgBody()+ "</message>";
    String cp0002xml = XML_HEADER + "<message>" + genHead("CP0002","CF4000038450") + genCP0002_MsgBody()+ "</message>";
    String cp0005xml = XML_HEADER + "<message>" + genHead("CP0005","CF4000038450") + genCP0005_MsgBody()+ "</message>";

    /**
     * 鉴权发短信
     */
    @Test
    public void testCP0032(){
        MsgInfo msgInfo = this.accquireResult(cp0032xml, "2dc2651bb6d7a06ce6144ce001312f28");
        System.out.println(JSON.toJSONString(msgInfo));
    }

    /**
     * 鉴权确认
     */
    @Test
    public void testCP0030(){
        MsgInfo msgInfo = this.accquireResult(cp0030xml, "2dc2651bb6d7a06ce6144ce001312f28");
        System.out.println(JSON.toJSONString(msgInfo));
    }

    /**
     * 代收
     */
    @Test
    public void testCP0001(){
        MsgInfo msgInfo = this.accquireResult(cp0001xml, "2dc2651bb6d7a06ce6144ce001312f28");
        System.out.println(JSON.toJSONString(msgInfo));
    }

    /**
     * 代收查询
     */
    @Test
    public void testCP0002(){
        MsgInfo msgInfo = this.accquireResult(cp0002xml, "2dc2651bb6d7a06ce6144ce001312f28");
        System.out.println(JSON.toJSONString(msgInfo));
    }
    /**
     * 分账接口
     */
    @Test
    public void testCP0005(){
        MsgInfo msgInfo = this.accquireResult(cp0005xml, "2dc2651bb6d7a06ce6144ce001312f28");
        System.out.println(JSON.toJSONString(msgInfo));
    }

    private MsgInfo accquireResult(String xml, String merchanKey) {
        MsgInfo msgInfo = new MsgInfo();

        log.info("xml信息：{}",xml);
        int startIndex = xml.indexOf("<merchantno>") + "<merchantno>".length();
        int endIndex = xml.indexOf("</merchantno>");
        String merchantNo = xml.substring(startIndex, endIndex);
        if (StringUtil.isEmpty(merchantNo)) {
            log.error("获取merchantno失败！");
            return msgInfo;
        }
        int start = xml.indexOf("<trancode>") + "<trancode>".length();
        int end = xml.indexOf("</trancode>");
        String trancode = xml.substring(start, end);
        if (StringUtil.isEmpty(trancode)) {
            log.error("获取trancode失败！");
            return msgInfo;
        }
        //默认三方
        String payChannelCode = "AgentPay";
        try {
            int payChannel_startIndex = xml.indexOf("<payChannelCode>") + "<payChannelCode>".length();
            int payChannel_endIndex = xml.indexOf("</payChannelCode>");
            payChannelCode = xml.substring(payChannel_startIndex, payChannel_endIndex);
        } catch (Exception e) {
            log.error("获取payChannelCode失败",e);
        }

        log.info("merchantNo=" + merchantNo + ",payChannelCode=" + payChannelCode + ",三方代收付接口版本开关：1：老接口  2：新接口（加密版）="
                + ThirdPayConstant.MERCHANT_SF_SWITCH);
        //过滤payChannelCode标签
        xml = xml.replace("<payChannelCode>" + payChannelCode + "</payChannelCode>", "");
        //支付路由 或者三方接口开关控制为走老接口
        if (merchantNo.equals(ThirdPayConstant.PAY_ROUTE_MERCHANT_NO) || payChannelCode.equals("PayRouter")
                || merchantNo.equals(ThirdPayConstant.PAY_ROUTE_XD_MERCHANT_NO)
                || ThirdPayConstant.MERCHANT_SF_V1.equals(ThirdPayConstant.MERCHANT_SF_SWITCH)) {
            return acquireResultPayRouter(xml, merchanKey, merchantNo, payChannelCode);
        }

        //过滤xml标签
        xml = xml.replace(XML_HEADER, "").replace("<message>", "").replace("</message>", "");
        StringBuffer data = new StringBuffer();
        data.append("<data>");
        data.append(xml);
        data.append("</data>");
        String srcData = data.toString();

        String envelopdata;
        //构建发送报文
        StringBuffer sb = new StringBuffer();
        try {
            //易贷三方商户，并且是加密版本
            if (ThirdPayConstant.MERCHANT_NO.equals(merchantNo) || ThirdPayConstant.GATEWAY_MERCHANT_NO.equals(merchantNo)) {
                merchanKey = ThirdPayConstant.MERCHANT_CERT_PWD;
            }
            String sign = rsa.rsaP1Sign(srcData, getMerchantPfxFile(merchantNo), merchanKey);
            log.info("签名：" + sign);
            sb.append(XML_HEADER);
            sb.append(getXmlSt("message"));
            sb.append(srcData);
            sb.append(getXmlSt("sign"));
            sb.append(sign);
            sb.append(getXmlEd("sign"));
            sb.append(getXmlEd("message"));

            log.info("报文原文：" + sb.toString());
            envelopdata = new String(rsa.envelopMessage(sb.toString(), getMerchantCerFile(merchantNo)));
            log.info("加密：" + envelopdata);
        } catch (Exception e) {
            log.error("上送三方报文加密出现异常!", e);
            return msgInfo;
        }
        Map<String, String> params = new HashMap<>();
        params.put("xml", envelopdata);
        params.put("mac", "");
        log.info("上送三方报文：" + params);
        //记录发送报文内容
        msgInfo.setSendMsg(sb.toString());
        log.info("上送三方报文地址：" + ThirdPayConstant.THIRD_PAY_PATH);
        String result = null;
        try {
            //利用httpClient发送xml，mac到三方支付，执行交易
            result = HttpClient431Util.doPost(params, ThirdPayConstant.THIRD_PAY_PATH);
        } catch (Exception e) {
            log.error("调用三方支付接口出现异常！", e);
            return msgInfo;
        }
        log.info("三方返回报文：" + result);
        //解析处理结果,获取主要的交易结果信息，赋值给MsgInfo中的对应属性，将该对象返回给商户
        msgInfo = parseReturnMsgInfo(result, merchanKey, msgInfo, merchantNo, trancode);
        return msgInfo;
    }

    private String genHead(String interfaceName,String merchantNo) {
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = simpleDateFormat.format(new Date());
        sb.append("<head>");
        sb.append("<trancode>").append(interfaceName).append("</trancode>");
        sb.append("<msgtype>").append("0001").append("</msgtype>");
        sb.append("<bussflowno>").append(merchantNo+String.valueOf(System.currentTimeMillis())).append("</bussflowno>");
        sb.append("<merchantno>").append(merchantNo).append("</merchantno>");
        sb.append("<version>").append("1.0.0").append("</version>");
        sb.append("<channelno>").append("99").append("</channelno>");
        sb.append("<payChannelCode>").append("PayRouter").append("</payChannelCode>");
        sb.append("<trandate>").append(date, 0, 8).append("</trandate>");
        sb.append("<trantime>").append(date, 8,14).append("</trantime>");
        sb.append("</head>");
        return sb.toString();
    }
    private String genCP0032_MsgBody() {
        String queryParam = "accountType=00&certType=01&certNo=111111111111111111&" +
                "accountNo=6210984910004641329&bankName=中国邮政储蓄银行&" +
                "currentTranCode=CP0032&accountName=啦啦啦&mobileNo=11111111111&" +
                "authenticationType=01";
        return GenBodyUtil.genBody(queryParam);
    }
    private String genCP0030_MsgBody() {
        String queryParam = "accountType=00&" +
                "accountNo=6210984910004641329&bankName=中国邮政储蓄银行&" +
                "mobileNo=11111111111&authenticationType=01&phoneVerCode=700354";
        return GenBodyUtil.genBody(queryParam);
    }
    private String genCP0001_MsgBody() {
        String queryParam = "accountType=00&accountName=啦啦啦&tranAmt=0.01&curType=CNY&" +
                "bsnType=11203&accountNo=6210984910004641329&bankName=中国邮政储蓄银行&" +
                "mobileNo=11111111111&authenticationType=01&phoneVerCode=000000&" +
                "certType=01&certNo=111111111111111111";
        return GenBodyUtil.genBody(queryParam);
    }
    private String genCP0005_MsgBody() {
        String shareInfo="[{\"ledgerno\": \"10000449592\",\"dividemode\": \"AMOUNT\",\"dividevalue\": \"0.01\"}]";
        String queryParam = "accountType=00&accountName=啦啦啦&tranAmt=1&curType=CNY&" +
                "bsnType=11203&accountNo=6210984910004641329&bankName=中国邮政储蓄银行&" +
                "mobileNo=18738197893&authenticationType=01&phoneVerCode=000000&msgExt=YBCOLL&" +
                "certType=01&certNo=111111111111111111&shareInfo="+shareInfo;
        return GenBodyUtil.genBody(queryParam);
    }
    private String genCP0002_MsgBody() {
        String queryParam = "orgTranFlow=CF40000384501600329884267";
        return GenBodyUtil.genBody(queryParam);
    }

    private MsgInfo acquireResultPayRouter(String xml, String merchanKey, String merchantNo, String payChannelCode) {
        MsgInfo msgInfo = new MsgInfo();
        String MERCHANT_URL;
        //对请求报文执行一次加密运算，得到加密摘要
        String mac = DigestUtils.sha256Hex(xml + merchanKey);
        Map<String, String> params = new HashMap<String, String>();
        params.put("xml", xml);
        params.put("mac", mac);
        log.info("上送支付渠道报文：" + params);
        //记录发送报文内容
        msgInfo.setSendMsg(params.toString());
        MERCHANT_URL = ThirdPayConstant.THIRD_PAY_PATH;
        String result;
        try {
            //利用httpClient发送xml，mac到三方支付，执行交易
            result = HttpClient431Util.doPost(params, MERCHANT_URL);
        } catch (Exception e) {
            log.error("调用支付接口出现异常！", e);
            return msgInfo;
        }
        log.info("支付渠道返回报文：" + result);
        //解析处理结果,获取主要的交易结果信息，赋值给MsgInfo中的对应属性，将该对象返回给商户
        msgInfo = parseReturnMsgInfoPayRouter(result, merchanKey, msgInfo);
        //记录返回报文内容
        msgInfo.setRecieveMsg(result);
        return msgInfo;
    }

    private MsgInfo parseReturnMsgInfoPayRouter(String result, String merchanKey, MsgInfo msgInfo) {
        // TODO Auto-generated method stub
        //截取报文内容，分别获得xml和mac
        String[] respResult = result.split("&");
        if (respResult == null || "".equals(respResult) || respResult.length < 2) {
            return msgInfo;
        }
        String xml = result.split("&")[0].substring(4);
        String mac = result.split("&")[1].substring(4);
        Node msgHeadNode = null;
        Node msgBodyNode = null;
        Node timeoutNode = null;
        Node srvtypeNode = null;
        Node respcodeNode = null;
        Node respmsgNode = null;
        Node tranStateNode = null;
        Node phoneTokenNode = null;
        Node phoneVerCodeNode = null;
        Node tranRespMsgNode = null;
        Node tranRespCodeNode = null;
        Node totalNumNode = null;
        Node hashCodeNode = null;
        Node dataNode = null;
        Node queryTimeNode = null;
        Node acctNoNode = null;
        Node balanceNode = null;
        Node merOrderIdNode = null;
        Node custIdNode = null;
        Node allNumNode = null;
        Node accountNameNode = null;
        Node accountNoNode = null;
        Node bankNameNode = null;
        Node openBankUnionNoNode = null;
        Node accountTypeNode = null;

        /*
         * 连连支付添加结节
         */
        Node oid_partner = null;
        Node sign_type = null;
        Node sign = null;
        Node user_id = null;
        Node busi_partner = null;
        Node no_order = null;
        Node dt_order = null;
        Node money_order = null;
        Node name_goods = null;
        Node notify_url = null;
        Node bank_code = null;
        Node valid_order = null;
        Node channel_abbr = null;
        Node risk_item = null;
        Node channelName = null;
        Node channelCode = null;

        Node bankTypeNode = null;
        Node cardAttrNode = null;
        Node bankCodeNode = null;
        Node netBankUnionCodeNode = null;

        //Node channelCode;//渠道名
        Node channelNoNode = null;//渠道号
        Node channelOrderIdNode = null;//渠道流水号
        Node tradeTimeNode = null;//交易时间
        /**
         * 短信认证添加结节
         */
        Node protocolNoNode = null;
        Node hasNextPageNode = null;
        Node pageNoNode = null;
        Node pageSizeNode = null;

        //白名单新加
        Node channelNameNode = null;
        Node channelCodeNode = null;

        //验证三方返回摘要是否为空(摘要校验)
        if (StringUtil.isEmpty(mac)) {
            log.info("返回摘要为空！");
            throw new RuntimeException("返回摘要为空！");
        }
        //验证三方返回摘要是否与发送报文摘要一致(摘要校验)
        String msgMac = DigestUtils.sha256Hex(xml + merchanKey);
        if (!msgMac.equals(mac)) {
            log.info("返回摘要与交易生成摘要不一致！");
            throw new RuntimeException("摘要验证失败:返回摘要与交易生成摘要不一致！交易摘要:" + mac + "返回摘要:" + msgMac);
        }
        //MsgInfo msgInfo=new MsgInfo();

        int start = xml.indexOf("<trancode>") + "<trancode>".length();
        int end = xml.indexOf("</trancode>");
        String trancode = xml.substring(start, end);
        try {
            //记录返回报文内容

            if (TrancodeType.TRAN_CODE_CP0022.equals(trancode)) {
                if (xml.indexOf("<hashCode>") > 0) {//判断是否成功
                    String reshashCode = xml.substring(xml.indexOf("<hashCode>") + 10, xml.indexOf("</hashCode>"));
                    log.info("reshashCode：" + reshashCode);
                    if (!StringUtil.isEmpty(reshashCode)) {
                        String resdat = xml.substring(xml.indexOf("<data>") + 6, xml.indexOf("</data>") + 7);
                        String resdata = resdat.substring(resdat.indexOf("<data>") + 6, resdat.indexOf("</data>"));
                        log.info("data域：" + resdata);
                        String uzipdata = ZipUtils.gunzip(resdata);
                        log.info("解压后：" + uzipdata);
                        String hashCode = DigestUtils.sha256Hex(uzipdata);
                        log.info("解压后进行哈希" + hashCode);
                        boolean verify = reshashCode.equals(hashCode);
                        if (!verify) {
                            log.info("返回验签与交易生成验签不一致！");
                            throw new RuntimeException("验签验证失败:返回验签与交易生成验签不一致！");
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("上送三方返回报文解析出现异常：", e);
            return msgInfo;
        }
        try {
            //解析报文头结点
            msgHeadNode = XmlUtil.getXmlNode(xml, MsgInfoNode.MSG_XML_PATH_HEAD);
            //解析报文体结点
            msgBodyNode = XmlUtil.getXmlNode(xml, MsgInfoNode.MSG_XML_PATH_BODY);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.info("返回报文解析异常！！！");
            throw new RuntimeException("支付路由返回报文解析异常");
        }
        if (msgHeadNode != null) {
            timeoutNode = msgHeadNode.selectSingleNode(MsgInfoNode.MSG_HEAD_FILED_TIMEOUT);//超时节点
            srvtypeNode = msgHeadNode.selectSingleNode(MsgInfoNode.MSG_HEAD_FILED_SRVTYPE);
            respcodeNode = msgHeadNode.selectSingleNode(MsgInfoNode.MSG_HEAD_FILED_RESPCODE);//交易返回码节点
            respmsgNode = msgHeadNode.selectSingleNode(MsgInfoNode.MSG_HEAD_FILED_RESPMSG);//交易返回信息描述节点
        }
        if (ThirdPayConstant.SUCCESS_CODE.equals(respcodeNode.getText())
                || ThirdPayConstant.UNKNOWN_CODE.equals(respcodeNode.getText())) {
            //头结点中的respcode状态为该值，说明交易成功，才去解析body节点,另外CP0002和CP0004交易无论状态如何都会解析报文体
            if (msgBodyNode != null) {

                /**
                 * 账户名称
                 * 账户号
                 * 开户行
                 * 行号
                 * 账户类型
                 */
                accountNameNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_ACCOUNTNAME);

                accountNoNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_ACCOUNTNO);

                bankNameNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_BANKNAME);

                openBankUnionNoNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_OPENBANKUNIONNO);

                accountTypeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_ACCOUNTTYPE);

                //交易状态的描述节点
                tranStateNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_TRANSTATE);
                //手机验证码令牌
                phoneTokenNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_PHONETOKEN);
                //验证码
                phoneVerCodeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_PHONEVERCODE);
                //交易响应吗
                tranRespCodeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_TRANRESPCODE);
                //交易返回信息描述
                tranRespMsgNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_TRANRESPMSG);
                //总记录数
                totalNumNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_TOTALNUM);
                //内容摘要
                hashCodeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_HASHCODE);
                //结果内容
                dataNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_DATA);
                //查询时间
                queryTimeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_QUERYTIME);
                //虚拟账号
                acctNoNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_ACCTNO);
                //账户余额
                balanceNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_BALANCE);
                //订单号
                merOrderIdNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_MERORDERID);
                //客户号
                custIdNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_CUSTID);
                //总记录数
                allNumNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_ALLNUM);

                oid_partner = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_OID_PARTNER);
                sign_type = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_SIGN_TYPE);
                sign = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_SIGN);
                user_id = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_USER_ID);
                busi_partner = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_BUSI_PARTNER);
                no_order = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_NO_ORDER);
                dt_order = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_DT_ORDER);
                money_order = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_MONEY_ORDER);
                name_goods = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_NAME_GOODS);
                bank_code = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_BANK_CODE);
                notify_url = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_NOTIFY_URL);
                valid_order = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_VALID_ORDER);
                channel_abbr = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_CHANNEL_ABBR);
                risk_item = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_RISK_ITEM);
                channelName = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_CHANNELNAME);
                channelCode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_CHANNELCODE);
                /*   System.out.println("00000000" + MsgInfoNode.MSG_BODY_FILED_RISK_ITEM + "00000000000000000");
                 */
                bankTypeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_CARDBIN_BANKTYPE);
                cardAttrNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_CARDBIN_CARDATTR);
                bankCodeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_CARDBIN_BANKCODE);
                netBankUnionCodeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_CARDBIN_NETBANKUNIONCODE);
                protocolNoNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_PROTOCOLNO);

                channelNoNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_CHANNELNO);//渠道号
                channelOrderIdNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_CHANNELORDERID);//渠道流水号
                tradeTimeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_TRADETIME);//交易时间

                hasNextPageNode = msgBodyNode.selectSingleNode(MsgInfoNode.HASNEXTPAGE);
                pageNoNode = msgBodyNode.selectSingleNode(MsgInfoNode.PAGENO);
                pageSizeNode = msgBodyNode.selectSingleNode(MsgInfoNode.PAGESIZE);

                //白名单新加
                channelCodeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_CHANNELCODE);
                channelNameNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_CHANNELNAME);

            }

            if (hasNextPageNode != null) {
                msgInfo.setHasNextPage(hasNextPageNode.getText());
            }
            if (pageNoNode != null) {
                if (pageNoNode.getText() != null) {
                    msgInfo.setPageNo(Integer.valueOf(pageNoNode.getText()));
                }
            }
            if (pageSizeNode != null) {
                if (pageSizeNode.getText() != null) {
                    msgInfo.setPageSize(Integer.valueOf(pageSizeNode.getText()));
                }
            }

            if (accountNameNode != null) {
                msgInfo.setAccountName(accountNameNode.getText());
            }
            if (accountNoNode != null) {
                msgInfo.setAccountNo(accountNoNode.getText());
            }
            if (bankNameNode != null) {
                msgInfo.setBankName(bankNameNode.getText());
            }
            if (openBankUnionNoNode != null) {
                msgInfo.setOpenBankUnionNo(openBankUnionNoNode.getText());
            }
            if (accountTypeNode != null) {
                msgInfo.setAccountType(accountTypeNode.getText());
            }

            if (tranStateNode != null) {
                msgInfo.setTranState(tranStateNode.getText());
            }
            if (phoneTokenNode != null) {
                msgInfo.setPhoneToken(phoneTokenNode.getText());
            }
            if (phoneVerCodeNode != null) {
                msgInfo.setPhoneVerCode(phoneVerCodeNode.getText());
            }
            if (tranRespCodeNode != null) {
                msgInfo.setTranRespCode(tranRespCodeNode.getText());
            }
            if (tranRespMsgNode != null) {
                msgInfo.setTranRespMsg(tranRespMsgNode.getText());
            }
            if (totalNumNode != null) {
                msgInfo.setTotalNum(totalNumNode.getText());
            }
            if (hashCodeNode != null) {
                msgInfo.setHashCode(hashCodeNode.getText());
            }
            if (dataNode != null) {
                msgInfo.setData(dataNode.getText());
            }
            if (queryTimeNode != null) {
                msgInfo.setQueryTime(queryTimeNode.getText());
            }
            if (acctNoNode != null) {
                msgInfo.setAcctNo(acctNoNode.getText());
            }
            if (balanceNode != null) {
                msgInfo.setBalance(balanceNode.getText());
            }
            if (merOrderIdNode != null) {
                msgInfo.setMerOrderId(merOrderIdNode.getText());
            }
            if (custIdNode != null) {
                msgInfo.setCustId(custIdNode.getText());
            }
            if (allNumNode != null) {
                msgInfo.setAllNum(allNumNode.getText());
            }
            //卡bin信息
            if (bankTypeNode != null) {
                msgInfo.setBankType(bankTypeNode.getText());
            }
            if (cardAttrNode != null) {
                msgInfo.setCardAttr(cardAttrNode.getText());
            }
            if (bankCodeNode != null) {
                msgInfo.setBankCode(bankCodeNode.getText());
            }
            if (netBankUnionCodeNode != null) {
                msgInfo.setNetBankUnionCode(netBankUnionCodeNode.getText());
            }

            if (protocolNoNode != null) {
                msgInfo.setProtocolNo(protocolNoNode.getText());
            }

            if (channelNoNode != null) {
                msgInfo.setChannelNo(channelNoNode.getText());
            }

            if (channelOrderIdNode != null) {
                msgInfo.setChannelOrderId(channelOrderIdNode.getText());
            }
            if (tradeTimeNode != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ParsePosition pos = new ParsePosition(0);
                msgInfo.setTradeTime(formatter.parse(tradeTimeNode.getText(), pos));
            }

            //白名单新加
            if (channelCodeNode != null) {
                msgInfo.setChannelCode(channelCodeNode.getText());
            }

            if (channelNameNode != null) {
                msgInfo.setChannelName(channelNameNode.getText());
            }

        }
        if (timeoutNode != null) {
            msgInfo.setTimeOut(timeoutNode.getText());
        }
        if (srvtypeNode != null) {
            msgInfo.setSrvType(srvtypeNode.getText());
        }
        if (respcodeNode != null) {
            msgInfo.setRespcode(respcodeNode.getText());
        }
        if (respmsgNode != null) {
            msgInfo.setRespmsg(respmsgNode.getText());
        }

        if (oid_partner != null) {
            msgInfo.setOid_partner(oid_partner.getText());

        }
        if (sign_type != null) {
            msgInfo.setSign_type(sign_type.getText());

        }
        if (sign != null) {
            msgInfo.setSign(sign.getText());

        }
        if (user_id != null) {
            msgInfo.setUser_id(user_id.getText());

        }

        if (busi_partner != null) {
            msgInfo.setBusi_partner(busi_partner.getText());

        }
        if (no_order != null) {
            msgInfo.setNo_order(no_order.getText());

        }

        if (dt_order != null) {
            msgInfo.setDt_order(dt_order.getText());

        }
        if (money_order != null) {
            msgInfo.setMoney_order(money_order.getText());

        }
        if (name_goods != null) {

            msgInfo.setName_goods(name_goods.getText());
        }

        if (bank_code != null) {

            msgInfo.setBank_code(bank_code.getText());
        }
        if (notify_url != null) {
            msgInfo.setNotify_url(notify_url.getText());
        }
        if (valid_order != null) {
            msgInfo.setValid_order(valid_order.getText());
        }

        if (channel_abbr != null) {
            msgInfo.setChannel_abbr(channel_abbr.getText());
        }

        if (risk_item != null) {
            //            System.out.println("-------------" + risk_item.getText() + "---------");
            msgInfo.setRiskItem(risk_item.getText());
        }
        if (channelName != null) {
            msgInfo.setChannelName(channelName.getText());
        }
        if (channelCode != null) {
            msgInfo.setChannelCode(channelCode.getText());
        }

        return msgInfo;
    }
    private MsgInfo parseReturnMsgInfo(String result, String merchanKey, MsgInfo msgInfo, String merchantNo,
                                       String trancode) {
        //截取报文内容，分别获得xml和mac
        if (result == null) {
            return msgInfo;
        }
        String xml;
        try {
            xml = new String(rsa.openMessage(result.substring(4), getMerchantPfxFile(merchantNo), merchanKey));
            log.info("解密：" + xml);
            //记录返回报文内容
            msgInfo.setRecieveMsg(xml);

            if (TrancodeType.TRAN_CODE_CP0022.equals(trancode) || TrancodeType.TRAN_CODE_CP0023.equals(trancode)
                    || TrancodeType.TRAN_CODE_CP0024.equals(trancode)
                    || TrancodeType.TRAN_CODE_CP0025.equals(trancode)) {
                //判断是否成功
                if (xml.indexOf("<hashCode>") > 0) {
                    String reshashCode = xml.substring(xml.indexOf("<hashCode>") + 10, xml.indexOf("</hashCode>"));
                    log.info("reshashCode：" + reshashCode);
                    if (!StringUtil.isEmpty(reshashCode)) {
                        String resdat = xml.substring(xml.indexOf("<data>") + 6, xml.indexOf("</data>") + 7);
                        String resdata = resdat.substring(resdat.indexOf("<data>") + 6, resdat.indexOf("</data>"));
                        log.info("data域：" + resdata);

                        String uzipdata = ZipUtils.gunzip(resdata);
                        log.info("解压后：" + uzipdata);
                        String hashCode = DigestUtils.sha256Hex(uzipdata);
                        log.info("解压后进行哈希" + hashCode);
                        boolean verify = reshashCode.equals(hashCode);
                        if (!verify) {
                            log.info("返回验签与交易生成验签不一致！");
                            throw new RuntimeException("验签验证失败:返回验签与交易生成验签不一致！");
                        }
                    }
                }
            } else {
                String sign = xml.substring(xml.indexOf("<sign>") + 6, xml.indexOf("</sign>"));
                log.info("签名：" + sign);
                String resdata = xml.substring(xml.indexOf("<data>"), xml.indexOf("</data>") + 7);
                log.info("data域：" + resdata);
                FileInputStream is = null;
                is = new FileInputStream(getMerchantCerFile(merchantNo));
                X509Cert x509Cert = new X509Cert(is);
                String cert = new String(Base64.encode(x509Cert.getEncoded()), "UTF-8");
                boolean verify = rsa.rsaP1Verify(sign, resdata, cert.getBytes());
                log.info("验签：" + verify);
                if (StringUtil.isEmpty(sign)) {
                    log.info("返回验签为空！");
                    throw new RuntimeException("返回验签为空！");
                }
                if (!verify) {
                    log.info("返回验签与交易生成验签不一致！");
                    throw new RuntimeException("验签验证失败:返回验签与交易生成验签不一致！");
                }
            }

        } catch (Exception e) {
            log.error("上送三方返回报文解析出现异常：", e);
            return msgInfo;
        }
        Node msgHeadNode = null;
        Node msgBodyNode = null;
        Node timeoutNode = null;
        Node srvtypeNode = null;
        Node respcodeNode = null;
        Node respmsgNode = null;
        Node tranStateNode = null;

        Node phoneTokenNode = null;
        Node phoneVerCodeNode = null;
        Node tranRespMsgNode = null;
        Node tranRespCodeNode = null;
        Node totalNumNode = null;
        Node hashCodeNode = null;
        Node dataNode = null;
        Node queryTimeNode = null;
        Node acctNoNode = null;
        Node balanceNode = null;
        Node merOrderIdNode = null;
        Node custIdNode = null;

        /**
         * 白名单认证——短信认证CP0030添加返回信息
         */
        Node protocolNoNode = null;
        Node channelNameNode = null;
        Node channelCodeNode = null;

        //Node channelCode;//渠道名
        Node channelNoNode = null;//渠道号
        Node channelOrderIdNode = null;//渠道流水号
        Node tradeTimeNode = null;//交易时间

        Node hasNextPageNode = null;
        Node pageNoNode = null;
        Node pageSizeNode = null;

        //MsgInfo msgInfo=new MsgInfo();
        try {
            //解析报文头结点
            msgHeadNode = XmlUtil.getXmlNode(xml, MsgInfoNode.MSG_XML_PATH_DATA_HEAD);
            //解析报文体结点
            msgBodyNode = XmlUtil.getXmlNode(xml, MsgInfoNode.MSG_XML_PATH_DATA_BODY);
        } catch (Exception e) {
            log.info("返回报文解析异常！！！");
            throw new RuntimeException("三方返回报文解析异常");
        }
        if (msgHeadNode != null) {
            timeoutNode = msgHeadNode.selectSingleNode(MsgInfoNode.MSG_HEAD_FILED_TIMEOUT);//超时节点
            srvtypeNode = msgHeadNode.selectSingleNode(MsgInfoNode.MSG_HEAD_FILED_SRVTYPE);
            respcodeNode = msgHeadNode.selectSingleNode(MsgInfoNode.MSG_HEAD_FILED_RESPCODE);//交易返回码节点
            respmsgNode = msgHeadNode.selectSingleNode(MsgInfoNode.MSG_HEAD_FILED_RESPMSG);//交易返回信息描述节点
        }
        if (ThirdPayConstant.SUCCESS_CODE.equals(respcodeNode.getText())
                || ThirdPayConstant.UNKNOWN_CODE.equals(respcodeNode.getText())) {
            //头结点中的respcode状态为该值，说明交易成功，才去解析body节点,另外CP0002和CP0004交易无论状态如何都会解析报文体
            if (msgBodyNode != null) {
                //交易状态的描述节点
                tranStateNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_TRANSTATE);

                //手机验证码令牌
                phoneTokenNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_PHONETOKEN);
                //验证码
                phoneVerCodeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_PHONEVERCODE);
                //交易响应吗
                tranRespCodeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_TRANRESPCODE);
                //交易返回信息描述
                tranRespMsgNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_TRANRESPMSG);
                //总记录数
                totalNumNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_TOTALNUM);
                //内容摘要
                hashCodeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_HASHCODE);
                //结果内容
                dataNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_DATA);
                //查询时间
                queryTimeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_QUERYTIME);
                //虚拟账号
                acctNoNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_ACCTNO);
                //账户余额
                balanceNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_BALANCE);
                //订单号
                merOrderIdNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_MERORDERID);
                //客户号
                custIdNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_CUSTID);
                //签约协议号
                protocolNoNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_PROTOCOLNO);

                channelNoNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_CHANNELNO);//渠道号
                channelOrderIdNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_CHANNELORDERID);//渠道流水号
                tradeTimeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_TRADETIME);//交易时间

                hasNextPageNode = msgBodyNode.selectSingleNode(MsgInfoNode.HASNEXTPAGE);
                pageNoNode = msgBodyNode.selectSingleNode(MsgInfoNode.PAGENO);
                pageSizeNode = msgBodyNode.selectSingleNode(MsgInfoNode.PAGESIZE);

                channelCodeNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_CHANNELCODE);
                channelNameNode = msgBodyNode.selectSingleNode(MsgInfoNode.MSG_BODY_FILED_CHANNELNAME);

            }

            if (hasNextPageNode != null) {
                msgInfo.setHasNextPage(hasNextPageNode.getText());
            }
            if (pageNoNode != null) {
                if (pageNoNode.getText() != null) {
                    msgInfo.setPageNo(Integer.valueOf(pageNoNode.getText()));
                }
            }
            if (pageSizeNode != null) {
                if (pageSizeNode.getText() != null) {
                    msgInfo.setPageSize(Integer.valueOf(pageSizeNode.getText()));
                }
            }

            if (tranStateNode != null) {
                msgInfo.setTranState(tranStateNode.getText());
            }

            if (phoneTokenNode != null) {
                msgInfo.setPhoneToken(phoneTokenNode.getText());
            }
            if (phoneVerCodeNode != null) {
                msgInfo.setPhoneVerCode(phoneVerCodeNode.getText());
            }
            if (tranRespCodeNode != null) {
                msgInfo.setTranRespCode(tranRespCodeNode.getText());
            }
            if (tranRespMsgNode != null) {
                msgInfo.setTranRespMsg(tranRespMsgNode.getText());
            }
            if (totalNumNode != null) {
                msgInfo.setTotalNum(totalNumNode.getText());
            }
            if (hashCodeNode != null) {
                msgInfo.setHashCode(hashCodeNode.getText());
            }
            if (dataNode != null) {
                msgInfo.setData(dataNode.getText());
            }
            if (queryTimeNode != null) {
                msgInfo.setQueryTime(queryTimeNode.getText());
            }
            if (acctNoNode != null) {
                msgInfo.setAcctNo(acctNoNode.getText());
            }
            if (balanceNode != null) {
                msgInfo.setBalance(balanceNode.getText());
            }
            if (merOrderIdNode != null) {
                msgInfo.setMerOrderId(merOrderIdNode.getText());
            }
            if (custIdNode != null) {
                msgInfo.setCustId(custIdNode.getText());
            }
            if (protocolNoNode != null) {
                msgInfo.setProtocolNo(protocolNoNode.getText());
            }

            //CP0001新加
            if (channelNoNode != null) {
                msgInfo.setChannelNo(channelNoNode.getText());
            }

            if (channelOrderIdNode != null) {
                msgInfo.setChannelOrderId(channelOrderIdNode.getText());
            }
            if (tradeTimeNode != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ParsePosition pos = new ParsePosition(0);
                msgInfo.setTradeTime(formatter.parse(tradeTimeNode.getText(), pos));
            }

            if (channelCodeNode != null) {
                msgInfo.setChannelCode(channelCodeNode.getText());
            }

            if (channelNameNode != null) {
                msgInfo.setChannelName(channelNameNode.getText());
            }

        }
        if (timeoutNode != null) {
            msgInfo.setTimeOut(timeoutNode.getText());
        }
        if (srvtypeNode != null) {
            msgInfo.setSrvType(srvtypeNode.getText());
        }
        if (respcodeNode != null) {
            msgInfo.setRespcode(respcodeNode.getText());
        }
        if (respmsgNode != null) {
            msgInfo.setRespmsg(respmsgNode.getText());
        }
        return msgInfo;
    }

    private String getMerchantCerFile(String MERCHANT_NO) {
        String MERCHANT_NO_str = MERCHANT_NO;
        //网关账户余额用cf证书加密查询
        if (MERCHANT_NO.equals(ThirdPayConstant.GATEWAY_MERCHANT_NO)) {
            MERCHANT_NO_str = ThirdPayConstant.MERCHANT_NO;
        }
        return ThirdPayConstant.MERCHANT_CERT_PATH + MERCHANT_NO_str + File.separator
                + ThirdPayConstant.MERCHANT_CERT_FILENAME_CER;

    }

    private String getMerchantPfxFile(String MERCHANT_NO) {
        //网关账户余额用cf证书加密查询
        String MERCHANT_NO_str = MERCHANT_NO;
        if (MERCHANT_NO.equals(ThirdPayConstant.GATEWAY_MERCHANT_NO)) {
            MERCHANT_NO_str = ThirdPayConstant.MERCHANT_NO;
        }
        String path=ThirdPayConstant.MERCHANT_CERT_PATH + MERCHANT_NO_str + File.separator
                + ThirdPayConstant.MERCHANT_CERT_FILENAME_PFX;
        log.info("返回pfx文件路径：{}",path);
        return path;

    }

    /**
     * 取XML前标签字符串
     * @param label 标签名
     * @return
     */
    private static String getXmlSt(String label) {
        return "<" + label + ">";
    }

    /**
     * 取XML后标签字符串
     * @param label 标签名
     * @return
     */
    private static String getXmlEd(String label) {
        return "</" + label + ">";
    }

}
