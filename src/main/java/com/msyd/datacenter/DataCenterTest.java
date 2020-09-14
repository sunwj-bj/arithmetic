package com.msyd.datacenter;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.msyd.base.HttpClient;
import com.msyd.base.Response;
import com.util.ImageBase64Util;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 数据中心调用测试
 * @author sunwj
 */
public class DataCenterTest {

    @Test
    public void ifNeedCheckFace(){
        String testUrl = "http://172.30.15.20/anti-fraud/verify/checkFaceCache";
        StringBuffer sb = new StringBuffer();
        sb.append("name=张三");
        sb.append("&appId=YKD");
        sb.append("&idCard=110101198001010037");
        sb.append("&mobile=18763256958");
        sb.append("&productType=YKD");
        Response response = HttpClient.httpPostRequest(testUrl,sb.toString());
        printResult(response);
    }
    @Test
    public void getCompanyList() throws Exception {
        String testUrl = "http://172.30.15.20:8080/anti-fraud/white/getCooperationCompany";
        Map response = tradePost(testUrl,"");
        System.out.println("合作公司列表返回公司json:"+response.toString());

    }
    @Test
    public void checkThreeElement(){
        String testUrl = "http://172.30.15.20/anti-fraud/userinfo/query3Element";
        StringBuffer sb = new StringBuffer();
        sb.append("name=陆苇");
        sb.append("&idCard=520327200102170022");
        sb.append("&mobile=18685633207");
        Response response = HttpClient.httpPostRequest(testUrl,sb.toString());
        printResult(response);
    }

    @Test
    public void getLatestFaceImage() throws IOException {
        String testUrl = "http://172.30.15.20/anti-fraud/tencentface/getLastImage";
        StringBuffer sb = new StringBuffer();
        sb.append("idcard=110228199212164925");
        Response response = HttpClient.httpPostRequest(testUrl,sb.toString());
        if (response!=null){
            System.out.println("获取最新刷脸照片数据中心返回原始信息========>"+response.asString());
            JSONObject json = JSONObject.parseObject(response.asString());
            System.out.println("获取最新刷脸照片接口返回信息========>"+json);
            ImageBase64Util.base64ToImageFile(json.getJSONObject("returnResult").getString("imageByte"),"D:\\test.jpg");
        }else{
            System.out.println("获取最新刷脸照片接口数据中心未返回信息！");
        }
    }

    private void printResult(Response response) {
        if (response!=null){
            System.out.println("数据中心返回原始信息========>"+response.asString());
            JSONObject json = JSONObject.parseObject(response.asString());
            System.out.println("数据中心返回信息JSON化========>"+json);
        }else{
            System.out.println("数据中心未返回信息！");
        }
    }
    public static Map tradePost(String reqUrl, String reqParam) throws Exception {
            Map retMap;
            URL url = new URL(reqUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("contentType", "UTF-8");
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestProperty("Content-Length",String.valueOf(reqParam.getBytes().length));

            httpURLConnection.getOutputStream().write(reqParam.getBytes("UTF-8"));
            httpURLConnection.getOutputStream().flush();
            httpURLConnection.getOutputStream().close();
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line;
             while ((line = bufferedReader.readLine()) != null){
                    buffer.append(line);
                }
            Gson gson = new Gson();
            retMap = gson.fromJson(buffer.toString(), Map.class);
            return retMap;
        }
    }
