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
import javax.net.ssl.X509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static String sendHttp(String url, String sendString) throws Exception {
        HttpURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            // con.setConnectTimeout(300*1000);
            // con.setReadTimeout(300*1000);
            con.setRequestProperty("Content-Type", "application/json");
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
        logger.error("调用接口返回数据：{}",result);
        return result;
    }

    public static void main(String[] args) {
        try {
            String s = sendHttps("https://bcogwdev.sncfc.com.cn/sncfc-oss/yl/", "");
            logger.info("调用返回：",s);
        } catch (Exception e) {
            logger.error("调用异常！",e);
        }
    }
}