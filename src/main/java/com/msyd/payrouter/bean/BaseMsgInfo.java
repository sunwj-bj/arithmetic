package com.msyd.payrouter.bean;

/**
 * 三方返回报文基类bean
 * @author yanglongfei
 *
 */
public class BaseMsgInfo {
    private String respcode;//交易响应吗

    private String respmsg;//交易响应吗说明

    private String sendMsg;//发送至三方的报文

    private String recieveMsg;//三方返回的报文

    public String getRespcode() {
        return respcode;
    }

    public void setRespcode(String respcode) {
        this.respcode = respcode;
    }

    public String getRespmsg() {
        return respmsg;
    }

    public void setRespmsg(String respmsg) {
        this.respmsg = respmsg;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public String getRecieveMsg() {
        return recieveMsg;
    }

    public void setRecieveMsg(String recieveMsg) {
        this.recieveMsg = recieveMsg;
    }

}
