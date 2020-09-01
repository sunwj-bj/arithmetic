package com.msyd.datacenter;

import com.alibaba.fastjson.JSONObject;
import com.msyd.base.HttpClient;
import com.msyd.base.Response;
import com.util.ImageBase64Util;
import org.junit.Test;

import java.io.IOException;

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
        if (response!=null){
            System.out.println("是否刷脸接口数据中心返回原始信息========>"+response.asString());
            JSONObject json = JSONObject.parseObject(response.asString());
            System.out.println("数据中心是否刷脸接口返回信息========>"+json);
        }else{
            System.out.println("是否刷脸接口数据中心未返回信息！");
        }
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
}