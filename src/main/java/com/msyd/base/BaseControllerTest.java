package com.msyd.base;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunwj
 */
public class BaseControllerTest {
    public final static String MD5_SALT_ANDROID = "FQBLZPQ3PWJ48AV63DWEWTRE345";

    public final static String APPKEY_KEY_ANDROID = "D2T3Y4H5";

    public final static String MD5_SALT_IOS = "HXWcjvQWVG1wI4FQBLZpQ3pWj48AV63d";

    public final static String APPKEY_KEY_IOS = "5E29C2DD";

    private static String BASE_URL = "http://172.30.10.70:8806/";
    //	private static String BASE_URL = "https://m.msyidai.com/";

    private String requestUrl = null;

    private Map<String, String> params;

    public BaseControllerTest() {
        params = new HashMap<>();
        params.put("timestamp", "1407381075247");
        params.put("sourceId", "android");
    }

    /**
     * 设置 token
     *
     * @return
     */
    protected void setToken(String token) {
        params.put("token", token);
    }

    /**
     * 请求
     *
     * @return
     */
    public String doRequest() {
        StringBuffer jsonParams = new StringBuffer();
        String param = StringUtil.object2JSON(params);
        try {
            String str = Des.encryptDES(param,APPKEY_KEY_ANDROID);
            str = str.replaceAll("\\+", "%2B");
            str = str.replaceAll("\\ ", "+");
            System.out.println("加密后的请求报文=======>" + str);
            jsonParams.append("param=" + str);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        jsonParams.append("&");
        jsonParams.append("sign=" + MD5.encodeByMd5AndSalt(param,MD5_SALT_ANDROID));
        jsonParams.append("&");
        jsonParams.append("os=android");

        Response response = null;
        try {
            response = HttpClient.httpPostRequest(BASE_URL + requestUrl, jsonParams.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String res = response.asString();
        System.out.println("http请求返回======>" + res);
        return res;
    }

    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     */
    public void addParam(String key, String value) {
        params.put(key, value);
    }

    public void clearParams() {
        params.clear();
    }

    /**
     * 设置请求的 url
     *
     * @param requestUrl
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

}
