package com.msyd.wireless;

import com.alibaba.fastjson.JSONObject;
import com.http.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 车全项目短信测试
 */
public class SendSmsTest {
    private static String url="http://172.30.13.64:8080/sms/sender";
    private static String key="6ozLNGz20j8gfQqZ+H2S4Q==";
    private static String mobile="18756676767";
    private static String platformCode="YD";
    private static String serviceCode="HKDQTX";
    private static String templateJson="{\"test\":\"test\"}";

    private static void sendSms() throws UnsupportedEncodingException {
        String param = genUrlParam();

        String jsonParam = getJsonParam();

        try {
            System.out.println(param);
            String res = HttpUtil.sendHttp(url,param);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getJsonParam() throws UnsupportedEncodingException {
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> subMap = new HashMap<>();
        subMap.put("test","test");
        map.put("key", URLEncoder.encode(key,"UTF-8"));
        map.put("mobile",mobile);
        map.put("platformCode",platformCode);
        map.put("serviceCode",serviceCode);
        map.put("templateJson",subMap);
        return JSONObject.toJSONString(map);
    }

    private static String genUrlParam() throws UnsupportedEncodingException {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("key=").append(URLEncoder.encode(key,"UTF-8")).append("&mobile=").append(mobile)
                .append("&platformCode=").append(platformCode).append("&serviceCode=")
                .append(serviceCode).append("&templateJson=").append(templateJson);
         return stringBuffer.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        sendSms();
    }
}
