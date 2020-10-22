package com.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.net.ssl.X509TrustManager;
import com.other.OrderGenerator;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * http工具类
 * @author sunwj
 */
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static String sendHttp(String url, String sendString) throws Exception {
        HttpURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            //这里区分大小写post不能被识别，必须用大写
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(300*1000);
            con.setReadTimeout(300*1000);
            //请求WEB一般用application/x-www-form-urlencoded 请求API一般用application/json
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(sendString);
            osw.flush();
            osw.close();
            // 读取返回内容
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
            }
            return buffer.toString();
        } catch (Exception e) {
            logger.error("与服务器连接发生错误",e);
            throw new Exception("与服务器连接发生错误:" + e.getMessage());
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendHttps(String url, String sendString) throws Exception {
        // 构建请求参数
        String result = "";
        BufferedReader in = null;
        HttpsURLConnection urlCon = null;
        try {
            trustAllHosts();
            URL url2 = new URL(url);
            urlCon = (HttpsURLConnection) url2.openConnection();
            urlCon.setHostnameVerifier(DO_NOT_VERIFY);
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            //设置超时时间
            urlCon.setConnectTimeout(5 * 1000);
            urlCon.setReadTimeout(5 * 1000);
            // 发送POST请求必须设置如下两行
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            OutputStream os = urlCon.getOutputStream();
            os.write(sendString.getBytes());
            os.flush();
            // 发送请求参数
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("调用异常！",e);
            throw e;
        } finally {// 使用finally块来关闭输出流、输入流
            try {
                if (in != null) {
                    in.close();
                }
                if (urlCon !=null){
                    urlCon.disconnect();
                }
            } catch (IOException ex) {
                logger.error("关闭流异常",ex);
            }

        }
        logger.info("调用接口返回数据：{}",result);
        return result;
    }

    public static String md5LowerCase(String data) {
        return DigestUtils.md5Hex(data).toLowerCase();
    }

    private static String getSignString(Map<String, Object> commonParams) {
        // sign businessParams
        Map<String,Object> signMap = new TreeMap<>(commonParams);
        // 待签名的字符串
        StringBuilder values2SignBuilder = new StringBuilder();
        for(Map.Entry<String,Object> entry : signMap.entrySet()){
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            values2SignBuilder.append(entry.getValue());
        }
        String signLine = values2SignBuilder.toString();
        return signLine;
    }

    private static Map<String, Object> initCommonParams(String interfaceName,String timestamp, String encryptKey, String data) {
        Map<String, Object> commonMap = new HashMap<>();
        // 请求流水号
        commonMap.put("serialNumber", OrderGenerator.INSTANCE.nextOrderId("SN"));
        // 业务参数
        commonMap.put("data", data);
        // AES密钥
        commonMap.put("encryptKey", encryptKey);
        // 业务接口名称
        commonMap.put("serviceId",interfaceName);
        // 时间戳
        commonMap.put("timestamp", timestamp);
        // 版本号
        commonMap.put("version", "1.0");
        return commonMap;
    }

    public static void main(String[] args) {
        String interfaceName = "USER_QUALIFICATION_CHECK";
        String sncfcPubKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqMJtNRvw0wYJGZzqK09OWHVKrWvtcWm/J3kGifsdhA8fjPjhOSHxQUWi361eKdg5LMbsplHBlX/bCsHRI04EPsHJMJ8Cu7khmZgo/4lfoG9FmArI971w5sv23SUeCpGwe6aMhwF31yfGicrzKXIonEY4XvgVHLKOcGLJfyCUcL2+aEMUn9PS3n3Zl76pj1Ec6bFGZAdUsjaJ57QVtGQiAP+jJOOASBZvAy8YDxajBPTB2mW4tknpljRLSparSGa5UbcsL6pN8Z6MdOECi0iA/HFlI2UtVXd5wWmnp40LkimDJ+xmcCHwyNnZLa+X+mZcuyTStCROxs4JP5SGk02vlQIDAQAB";
        String partnerPriKeyStr = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDK+9vVKBGw2XVS1AfDoT7eED9Ed23e+WVWQSOUiX7Nr497aWT6IwvJ0nlvmNLFJDtyaTa8xKB6pW7kZE+0xN5wai51CHgBnUc3ZhbIai5DmMX/0bYxSe0bLR/eVtaENGQY8HeA6u2iYR7qDzeC3XEMfhCaKGii0HSg6XYEXONoJy765j+Z82DJOWHd8oFWG/rGyTSQy3S/PYsoqaI4p1dGjfDKQWV1Dv20P8cFfEJNJ9lT8lLF9VgyRik3JxqovOiPjca2GoK+ccPBTXXqiLhbbYpAGvklmx9EcJfPprKy9dQgAPwJdLKjOdpma5oUqTk7iXmgNfhj7Xgkkw/yO9SBAgMBAAECggEAPfXVa+KYS9iFVKw+VnwQ+a4R8e9WzXppcYPevYee+mA9txk75pLuFUQ3J1aJ9/2QW+M7/zEyH14CVJs+LVru33e6CKV2JGADDikOiUzIJIAs6GL2b2M2ilYi5TayspZfe7FjeSm9MhTdX+sXMc4wpPSSUsxE7HYfM8Mv8DL+g5rvGNB4hCjy+p056dSsVw8IbltdPF4oNn09/lGCRGxla3hQif75Rdei3DBadSMNgPoUQQCyILEZgjCN2YNZrfZJZm7tRqAdhr54mIeJoL9EYOAl+1zvHVi+Z97nRVGvPEOkP03kr1ahFRMpfU00yL8jSB93UttnMtRsI9ocRu6M1QKBgQDtFm1vUMim3z0odfrpId9UoRi5SYBG6kl6XkORt5+4NgfZDcVIVwJdQRpW9lgoOJaoZbyG63pDD8cAtcArkCPRFh6UYfNpd/K4eQy/yz38mz7G/GX44EyGbrg3vLioqj2JMlOc+cGL2YCdCAiqKHiGdGi6oez41pfmjeDgZwluNwKBgQDbLP821M/7ekQ85h+Rzx/msHXj5VD7OZtxCn3Ozl16InsqBNgbQ6J7KYK+ZRcxytzoJ3Al5koReJ5OHUpHiyfIwIQzKo7g++qingCffmy4WE3mMXW6QX7Ritrfp7tK8/CAoyWY8DPu4e6YXF5USd0ih2p2dPdXVc0NhaxPchQ3BwKBgCNa3dTkuhdQYQFgnpsXZwNqxpIS6OMqWuy5k9/t/w5sWD1A9XN1LjAT3kark7fjwGu6SCPih6fqeWWctNyKMR0j7El6Vd8beQGTY5hSSZsa14C68MXtRNwjNKtzJCJayLRl4dwdzoP5WlmQciJVyKtcPqXeMcVl0t8ZtzgfhWN/AoGAV503MF4F/gs0N/vt5sWbhQZJOh6zLpoqrxd4jzEjYO1jurpRASUaUI2ZfC7BBOCJSixpwly6gx1qeuNujAbukmuS7Tk4AGvzsanjqd5J668xBLIE073WykDtmbZdQmsdWu7c2rQ6rmWWkUVrV9pnEaR8RHohXcIoxB0JcoCx4AUCgYBkLqIK5wwQoPE7+1Ac/TnUr77TKQ5hmG0YyWTR7rPWTramjujCPbBaZBABo5fmWX/eTEo6bhZbP466pxaCXh+99+pen263crh9Hc8rQiKNYMPuJnDYGYyAWlEHtDOYDYIRSYkKfBjZ21Pxr/He55avp1BMVDwqXHT3qF5Hvx7XSw==";
        HashMap requestMap=new HashMap<String,String>(5);
        requestMap.put("productCode","PTCMBC01");
        requestMap.put("certType","0");
        requestMap.put("certNo",md5LowerCase("110101198001010010"));
        requestMap.put("mobile",md5LowerCase("18695632654"));
        requestMap.put("custName",md5LowerCase("张三"));
        requestMap.put("md5Flag","true");


        try {
            String dataJson = objectMapper.writeValueAsString(requestMap);
            // 时间戳
            String timestamp = Long.toString(System.currentTimeMillis() / 1000L);
            // 生成AES密钥
            byte[] aesKey = AesEncryptor.randomAesKey();
            String encryptKey = RSACoder.encrypt(aesKey, sncfcPubKeyStr);
            // 公共参数
            Map<String, Object> commonParams = initCommonParams(interfaceName,timestamp, encryptKey, dataJson);
            String signString = getSignString(commonParams);
            // 签名
            String sign = RSACoder.signSha256(signString.getBytes("UTF-8"), partnerPriKeyStr);
            // 加密
            String data = Base64.encodeBase64URLSafeString(AesEncryptor.encrypt(dataJson.getBytes("UTF-8"), aesKey));
            // 将签名、加密后的业务参数放入公共参数
            commonParams.put("sign", sign);
            commonParams.put("data", data);
            String message = objectMapper.writeValueAsString(commonParams);
            String response = sendHttps("https://bcogwdev.sncfc.com.cn/sncfc-oss/yl/CMBC", message);
            logger.info("调用返回：{}",response);
            Map<String, Object> returnMap = objectMapper.readValue(response, Map.class);
            String aesKeyStr = (String) returnMap.get("encryptKey");
            byte[] aesKeyReturn = RSACoder.decrypt(aesKeyStr, partnerPriKeyStr);
            String encryptData = (String) returnMap.get("data");
            String returnDataJson = new String(AesEncryptor.decrypt(Base64.decodeBase64(encryptData), aesKeyReturn),"UTF-8");
            System.out.println("苏宁返回结果："+returnDataJson);
            Map<String, Object> dataParams = (Map<String, Object>) objectMapper.readValue(returnDataJson, Map.class);
            // 验签
            sign = (String) returnMap.get("sign");
            // 公共参数去除sign字段
            returnMap.remove("sign");
            // 将解密后的业务参数放入公共参数
            returnMap.put("data", returnDataJson);
            signString = getSignString(returnMap);
            if (!RSACoder.verifySha256(signString.getBytes("UTF-8"), sncfcPubKeyStr, sign)) {
                System.out.println("苏宁准入接口验签不通过");
                return;
            } else {
                System.out.println("验签通过");
            }
            System.out.println("responseCode="+String.valueOf(dataParams.get("responseCode")));
            System.out.println("responseMsg="+String.valueOf(dataParams.get("responseMsg")));
        } catch (Exception e) {
            logger.error("调用异常！",e);
        }
    }
}